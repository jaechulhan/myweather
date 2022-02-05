package net.prolancer.myweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.prolancer.myweather.R;
import net.prolancer.myweather.domain.WeatherVo;

import java.util.ArrayList;
import java.util.List;

public class WeatherListAdapter extends BaseAdapter {
    public static final String DEFAULT_TEMP_STR_FORMAT = "%.2f (C) %.2f (F)";
    private Context context;
    private List<WeatherVo> dataList;

    private ImageView imgWeatherState;
    private TextView txtDate, txtState, txtMinTemp, txtMaxTemp;

    public WeatherListAdapter(Context context, List<WeatherVo> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View lvView = LayoutInflater.from(context).inflate(R.layout.lv_item, viewGroup, false);

        imgWeatherState = lvView.findViewById(R.id.imgWeatherState);
        txtDate = lvView.findViewById(R.id.txtDate);
        txtState = lvView.findViewById(R.id.txtState);
        txtMinTemp = lvView.findViewById(R.id.txtMinTemp);
        txtMaxTemp = lvView.findViewById(R.id.txtMaxTemp);

        int resId;
        switch (dataList.get(i).getWeather_state_abbr()) {
            case "sn": resId = R.drawable.sn; break;
            case "sl": resId = R.drawable.sl; break;
            case "h": resId = R.drawable.h; break;
            case "t": resId = R.drawable.t; break;
            case "hr": resId = R.drawable.hr; break;
            case "lr": resId = R.drawable.lr; break;
            case "s": resId = R.drawable.s; break;
            case "hc": resId = R.drawable.hc; break;
            case "lc": resId = R.drawable.lc; break;
            default:
                resId = R.drawable.c;
        }

        imgWeatherState.setImageResource(resId);
        txtDate.setText(dataList.get(i).getApplicable_date());
        txtState.setText(dataList.get(i).getWeather_state_name());
        txtMinTemp.setText(String.format(DEFAULT_TEMP_STR_FORMAT, dataList.get(i).getMin_temp(), dataList.get(i).getMin_temp_f()));
        txtMaxTemp.setText(String.format(DEFAULT_TEMP_STR_FORMAT, dataList.get(i).getMax_temp(), dataList.get(i).getMax_temp_f()));

        return lvView;
    }
}
