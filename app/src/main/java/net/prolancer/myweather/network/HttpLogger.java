package net.prolancer.myweather.network;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.logging.HttpLoggingInterceptor;

public class HttpLogger implements HttpLoggingInterceptor.Logger  {
    private static final String TAG = HttpLogger.class.getSimpleName();

    @Override
    public void log(@NonNull String message) {
        Log.d(TAG, message);
    }
}
