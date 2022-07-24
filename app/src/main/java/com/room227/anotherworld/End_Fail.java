package com.room227.anotherworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class End_Fail extends AppCompatActivity {

    private TiaoZiUtil tiaoziUtil1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_fail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 隐藏顶部状态栏

        TextView textView1;
        textView1 = ((TextView) findViewById(R.id.textView1));

        ImageButton imageButton_return;
        Button button;
        button = findViewById(R.id.button);
        imageButton_return = findViewById(R.id.imageButton_return);
        imageButton_return.setVisibility(View.INVISIBLE);

        //从SharedPreferences中获取先前的信息
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        Character hero = new Character();
        hero.name = pref.getString("hero_name", "");


        String s1 = "     就这样，" + hero.name + "战败了。\n\n" +
                "     在使出浑身解数脱战之后，\n\n" +
                "     " + hero.name + "躲在了一块巨石的背阴处。\n\n" +
                "     伤口从指缝处不断涌出鲜血，\n\n" +
                "     " + hero.name + "仰望天空，闭上了双眼。\n\n" +
                "     他知道，当再一次睁眼时，\n\n" +
                "     这世界会重新给自己一枚骰子，\n\n" +
                "     与一段崭新的冒险。\n\n\n\n" +
                "     END? " ;

        //调用构造方法，直接开启
        tiaoziUtil1 = new TiaoZiUtil(textView1, s1, 100);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                imageButton_return.setVisibility(View.VISIBLE);
            }
        });

        imageButton_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(End_Fail.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}