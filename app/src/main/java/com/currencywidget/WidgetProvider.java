package com.currencywidget;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class WidgetProvider extends AppWidgetProvider {
    private static final String plus100 = "plus100";
    private static final String minus100 = "minus100";

    private static final String plus50 = "plus50";
    private static final String minus50 = "minus50";

    private static final String plus10 = "plus10";
    private static final String minus10 = "minus10";

    private static final String plus1 = "plus1";
    private static final String minus1 = "minus1";


    private static final String convert = "convert";


    private static final String TAG = "WidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        remoteViews.setOnClickPendingIntent(R.id.plus100, getPendingSelfIntent(context, plus100));
        remoteViews.setOnClickPendingIntent(R.id.minus100, getPendingSelfIntent(context, minus100));

        remoteViews.setOnClickPendingIntent(R.id.plus50, getPendingSelfIntent(context, plus50));
        remoteViews.setOnClickPendingIntent(R.id.minus50, getPendingSelfIntent(context, minus50));

        remoteViews.setOnClickPendingIntent(R.id.plus10, getPendingSelfIntent(context, plus10));
        remoteViews.setOnClickPendingIntent(R.id.minus10, getPendingSelfIntent(context, minus10));

        remoteViews.setOnClickPendingIntent(R.id.plus1, getPendingSelfIntent(context, plus1));
        remoteViews.setOnClickPendingIntent(R.id.minus1, getPendingSelfIntent(context, minus1));


        remoteViews.setOnClickPendingIntent(R.id.convert, getPendingSelfIntent(context, convert));

        appWidgetManager.updateAppWidget(new ComponentName(context, WidgetProvider.class), remoteViews);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, FLAG_IMMUTABLE);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        switch (intent.getAction()) {
            case plus100:
                remoteViews.setTextViewText(R.id.amount, String.valueOf(MainActivity.plus100()));
                break;

            case minus100:
                if (MainActivity.getAmount() > 0) {
                    remoteViews.setTextViewText(R.id.amount, String.valueOf(MainActivity.minus100()));
                } else {
                    Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                }
                break;

            case plus50:
                remoteViews.setTextViewText(R.id.amount, String.valueOf(MainActivity.plus50()));
                break;

            case minus50:
                if (MainActivity.getAmount() > 0) {
                    remoteViews.setTextViewText(R.id.amount, String.valueOf(MainActivity.minus50()));
                } else {
                    Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                }
                break;

            case plus10:
                remoteViews.setTextViewText(R.id.amount, String.valueOf(MainActivity.plus10()));
                break;


            case minus10:
                if (MainActivity.getAmount() > 0) {
                    remoteViews.setTextViewText(R.id.amount, String.valueOf(MainActivity.minus10()));
                } else {
                    Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                }
                break;

            case plus1:
                remoteViews.setTextViewText(R.id.amount, String.valueOf(MainActivity.plus1()));
                break;

            case minus1:
                if (MainActivity.getAmount() > 0) {
                    remoteViews.setTextViewText(R.id.amount, String.valueOf(MainActivity.minus1()));
                } else {
                    Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                }
                break;

            case convert:
                Toast.makeText(context, "Reading Data, please wait !", Toast.LENGTH_SHORT).show();
                Call<JsonObject> call = new Retro().getRetrofitInstance().create(Inter.class).getExchangeCurrency();
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject res = response.body();
                        String exRate = String.valueOf(res.get("AED_INR"));
                        double data = MainActivity.getAmount() * Double.parseDouble(exRate);
                        int result = (int) data;
                        String output = String.valueOf(result);
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                        remoteViews.setTextViewText(R.id.result, String.valueOf(Integer.valueOf(output)));
                        remoteViews.setTextViewText(R.id.valueOf, String.valueOf(MainActivity.amount));
                        appWidgetManager.updateAppWidget(new ComponentName(context, WidgetProvider.class), remoteViews);
                        Log.e(TAG, "onResponse: " + output);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                break;
        }
        appWidgetManager.updateAppWidget(new ComponentName(context, WidgetProvider.class), remoteViews);
    }


    class Retro {
        Retrofit retrofit;

        public Retrofit getRetrofitInstance() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl("https://free.currconv.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }

    interface Inter {
        @GET("api/v7/convert?q=AED_INR,INR_AED&compact=ultra&apiKey=33936357e65436c241f7")
        Call<JsonObject> getExchangeCurrency();
    }

}
