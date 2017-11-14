package com.meiyou.androidskin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.meiyou.skinlib.AndroidSkin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //AndroidSkin.getInstance().registerIgnoreSkinActivity(this);
        setContentView(R.layout.activity_main);
        initToolbar();
    }

    public void onSkinChange(View view){
        Intent intent = new Intent();
        intent.setClass(this,SkinSelectActivity.class);
        startActivity(intent);
    }



    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("AndroidSkin");
        toolbar.setSubtitle("Welcome to AndroidSkin");
        toolbar.setNavigationIcon(R.drawable.apk_all_bottomfind);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SkinSelectActivity.class));
            }
        });
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.apk_all_bottomfind));
    }
}
