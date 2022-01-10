package com.currencywidget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    static int amount = 0;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    static Integer getAmount() {
        return amount;
    }

    static Integer plus100 () {
        amount = amount + 100;
        return amount;
    }

    static Integer minus100 () {
        amount = amount - 100;
        return amount;
    }


    static Integer plus50 () {
        amount = amount + 50;
        return amount;
    }

    static Integer minus50 () {
        amount = amount - 50;
        return amount;
    }

    static Integer plus10 () {
        amount = amount + 10;
        return amount;
    }

    static Integer minus10 () {
        amount = amount - 10;
        return amount;
    }

    static Integer plus1 () {
        amount = amount + 1;
        return amount;
    }

    static Integer minus1 () {
        amount = amount - 1;
        return amount;
    }

}