package net.prolancer.myweather.network;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;

import net.prolancer.myweather.BuildConfig;
import net.prolancer.myweather.common.constants.AppConstants;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class NetworkManager {
    private static NetworkManager instance;
    private OkHttpClient client;

    /**
     * By using Singleton Pattern we can share cookie, client values.
     */
    private NetworkManager(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        builder.cookieJar(cookieJar);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.followRedirects(true);
        builder.addInterceptor(httpLoggingInterceptor);
        builder.addNetworkInterceptor(httpLoggingInterceptor);

        File cacheDir = new File(context.getCacheDir(), "network");

        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }

        Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);

        builder.cache(cache);

        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);

        client = builder.build();
    }

    /**
     * Get Instance of NetworkManager
     *
     * @return
     */
    public static synchronized NetworkManager getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkManager(context);
        }
        return instance;
    }

    /**
     * Get OkHttpClient
     *
     * @return
     */
    public OkHttpClient getClient() {
        return client;
    }

    /**
     * Call API
     *
     * @param context
     * @param url
     * @param reqMap
     * @param networkResponseListener
     */
    public static void sendToServer(Context context, String url, String method, Map<String, String> reqMap, final NetworkResponseListener networkResponseListener) {
        // Start - Call API
        OkHttpClient client = NetworkManager.getInstance(context).getClient();

        // #2. Create request with post
        url = BuildConfig.BASE_URL + url;

        Request request = null;

        if ("GET".equalsIgnoreCase(method)) {
            HttpUrl.Builder urlBuilder
                    = HttpUrl.parse(url).newBuilder();

            if (reqMap != null) {
                for (Map.Entry<String,String> entry : reqMap.entrySet()) {
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }

            url = urlBuilder.build().toString();

            request = new Request.Builder()
                    .url(url)
                    .build();
        } else {
            Gson gson = new Gson();
            String json = gson.toJson(reqMap);

            RequestBody jsonBody = RequestBody.create(
                    MediaType.parse(AppConstants.JSON_CONTENT_TYPE), json);

            if ("POST".equalsIgnoreCase(method)) {
                request = new Request.Builder()
                        .url(url)
                        .post(jsonBody)
                        .build();
            } else if ("PUT".equalsIgnoreCase(method)) {
                request = new Request.Builder()
                        .url(url)
                        .put(jsonBody)
                        .build();
            } else if ("DELETE".equalsIgnoreCase(method)) {
                request = new Request.Builder()
                        .url(url)
                        .delete(jsonBody)
                        .build();
            }
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                networkResponseListener.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                networkResponseListener.onSuccess(response);
            }
        });
        // End - Call API
    }
}
