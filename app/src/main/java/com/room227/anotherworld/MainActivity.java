package com.room227.anotherworld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;


public class MainActivity extends AppCompatActivity {

    private Button mBtnEditCharacter;
    private Button mBtnExit;
    private Button mBtnSet;




    CarouselView carouselView;
    int[] images = {
            R.drawable.image_1,
            R.drawable.image_2,
            R.drawable.image_3
    };
    String[] message = {
            "zhang",
            "cao",
            "tu"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 隐藏顶部状态栏
        //创建按钮
        mBtnEditCharacter = (Button) findViewById(R.id.btn_new_game);
        mBtnExit = (Button) findViewById(R.id.btn_exit_game);
        mBtnSet = (Button) findViewById(R.id.btn_set);
        carouselView = findViewById(R.id.carouselView);

        //设置轮播图数量
        carouselView.setPageCount(images.length);
        carouselView.setImageListener(imageListener);
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText( MainActivity.this,"Current Clicked Image:"+position+",Message:"+message[position],Toast.LENGTH_SHORT).show();
            }
        });
        setListeners();

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(images[position]);
        }
    };

    private void setListeners() {
        OnClick onClick = new OnClick();

        mBtnEditCharacter.setOnClickListener(onClick);
        mBtnExit.setOnClickListener(onClick);
        mBtnSet.setOnClickListener(onClick);

    }


    //由新游戏按钮跳转至角色编辑activity
    class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.btn_set:
                    Intent intent = null;
                    intent = new Intent(MainActivity.this, musicActivity.class);
                    startActivity(intent);
                    break;

                case R.id.btn_new_game:
                    intent = new Intent(MainActivity.this, passwordActivity.class);
                    startActivity(intent);
                    break;

                case R.id.btn_exit_game:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.
                            this);
                    dialog.setTitle("温馨提示").setMessage("您将要退出游戏.")
                            .setCancelable(false).setPositiveButton("确定", new DialogInterface.
                            OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            android.os.Process.killProcess(android.os.Process.myPid());
                            finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.
                            OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(MainActivity.this, "取消成功",
//                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                    break;
                default:
                    break;
            }
        }

    }

}

