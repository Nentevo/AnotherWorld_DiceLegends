package com.room227.anotherworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.BreakIterator;

public class EditCharacter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_character);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 隐藏顶部状态栏
        //创建二十面骰与主人公
        Dice d10_character = new Dice();
        Character hero = new Character();

        //定义需要的组件
        TextView textView_hp_num, textView_attackpower_num,
                textView_defensepower_num, textView_speed_num;
        EditText edit_name;
        ImageButton imageButton_Dice1;
        Button btn_new_game;

        //在资源中获取
        textView_hp_num = findViewById(R.id.textView_hp_num);
        textView_attackpower_num = findViewById(R.id.textView_attackpower_num);
        textView_defensepower_num = findViewById(R.id.textView_defencepower_num);
        textView_speed_num = findViewById(R.id.textView_speed_num);
        edit_name = findViewById(R.id.editName);
        imageButton_Dice1 = findViewById(R.id.imageButton_Dice1);
        btn_new_game = findViewById(R.id.btn_new_game);

        //先设置开始游戏按钮不可点击
        btn_new_game.setVisibility(View.INVISIBLE);

        //给骰子按钮添加点击事件，设置d20随机数的同时并将随机值打印至textView
        imageButton_Dice1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //在掷骰子后才能显示开始游戏按钮
                btn_new_game.setVisibility(View.VISIBLE);

                hero.hp = d10_character.D10_Num(2);
                textView_hp_num.setText(hero.hp + "");

                hero.attack_power = d10_character.D10_Num(2);
                textView_attackpower_num.setText(hero.attack_power + "");

                hero.defense_power = d10_character.D10_Num(1);
                textView_defensepower_num.setText(hero.defense_power + "");

                hero.speed = d10_character.D10_Num(2);
                textView_speed_num.setText(hero.speed + "");
            }
        });

        //给开始游戏按钮添加点击事件
        btn_new_game.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v){
                //从edittext中获取输入的名字
                hero.name = edit_name.getText().toString();

                //向SharedPreferences文件传递键值对，用以储存主人公的属性
                SharedPreferences.Editor editor = getSharedPreferences("data",
                        MODE_PRIVATE).edit();
                editor.putString("hero_name", hero.name);
                editor.putInt("hero_hp", hero.hp);
                editor.putInt("hero_attackpower", hero.attack_power);
                editor.putInt("hero_defensepower", hero.defense_power);
                editor.putInt("hero_speed", hero.speed);
                editor.putInt("hero_level", 1);
                editor.putInt("hero_exp", 0);
                editor.apply();

                Intent intent = new Intent(EditCharacter.this, Room0.class);
                startActivity(intent);
            }
        });
    }
}