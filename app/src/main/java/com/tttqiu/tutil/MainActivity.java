package com.tttqiu.tutil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tttqiu.library.Util;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Util.q();
    }
}
