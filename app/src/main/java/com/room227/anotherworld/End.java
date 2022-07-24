package com.room227.anotherworld;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class End extends Activity {



    private TiaoZiUtil tiaoziUtil1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
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


        String s1 = "     " + hero.name + "又完成了一次不凡的冒险。\n\n" +
                "     一路取得长剑、堕落者之颅、弑君……\n\n" +
                "     击败骷髅……\n\n" +
                "     战胜卡奥斯……\n\n" +
                "     得胜之后，" + hero.name + "坐上了卡奥斯的王座。\n\n" +
                "     由于战斗的劳累，他不禁合上了双眼。\n\n" +
                "     他也知道，当再一次睁眼时，\n\n" +
                "     这世界只会留给自己一枚骰子，\n\n" +
                "     与一段崭新的冒险。\n\n\n" +
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
                Intent intent = new Intent(End.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}