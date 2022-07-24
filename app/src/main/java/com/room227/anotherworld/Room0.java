package com.room227.anotherworld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

public class Room0 extends AppCompatActivity {

    private ImageView mIvHead;
    private SlideMenu slideMenu;
    private List<Weapon> weaponList = new ArrayList<>();

    Weapon skull = new Weapon("堕落者之颅", R.drawable.skull,
                "萨奇尔曾是艾瑞达的一位伟大领袖，而如今这颗颅骨是他唯一的遗物",
                "每当你掷点数时可以掷两枚d20，取二者最大值作为结果", 4000);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 隐藏顶部状态栏

        mIvHead =  findViewById(R.id.iv_head);
        slideMenu = findViewById(R.id.slideMenu);
        mIvHead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                slideMenu.switchMenu();
            }
        });

        //侧边栏背包内武器显示
        initWeapons();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        WeaponAdapter adapter = new WeaponAdapter(weaponList);
        recyclerView.setAdapter(adapter);


        recyclerView.setVisibility(View.INVISIBLE);

        Dice d20_battle = new Dice();

        //从SharedPreferences中获取先前的信息
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        Character hero = new Character();
        Character monster = new Character();
        hero.name = pref.getString("hero_name", "");

        hero.hp = pref.getInt("hero_hp", 0);
        hero.attack_power = pref.getInt("hero_attackpower", 0);
        hero.defense_power = pref.getInt("hero_defensepower", 0);
        hero.speed = pref.getInt("hero_speed", 0);
        hero.alive = true;
        hero.exp = pref.getInt("hero_exp", 0);
        hero.level = pref.getInt("hero_level", 1);
        hero.money = 0;

        monster.name = "骷髅";
        monster.hp = 10;
        monster.attack_power = 6;
        monster.defense_power = 2;
        monster.speed = 20;
        monster.alive = true;



        TextView textView_show, textView_show_hp, textView_show_attackpower,
                textView_show_defensepower,textView_show_speed,
                textView_show_level, textView_show_money;
        textView_show = findViewById(R.id.textView_show);
        textView_show.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView_show.setText("掷d20，开始战斗！\n");
        textView_show_hp = findViewById(R.id.textView_show_hp);
        textView_show_attackpower = findViewById(R.id.textView_show_attackpower);
        textView_show_defensepower = findViewById(R.id.textView_show_defensepower);
        textView_show_speed = findViewById(R.id.textView_show_speed);
        textView_show_level = findViewById(R.id.textView_show_level);
        textView_show_money = findViewById(R.id.textView_show_money);

        textView_show_hp.setText("生命值 " + hero.hp);
        textView_show_attackpower.setText("攻击力 " + hero.attack_power);
        textView_show_defensepower.setText("防御力 " + hero.defense_power);
        textView_show_speed.setText("速度值 " + hero.speed);
        textView_show_level.setText("level " + hero.level + " exp " + hero.exp);
        textView_show_money.setText("$ 0");

        //骷髅入场，需要定义imageview
        ImageView image_skull;
        image_skull = findViewById(R.id.imageView_skull);

        YoYo.with(Techniques.BounceInRight)
                .duration(4000)
                .repeat(0)
                .playOn(image_skull);

        ImageButton imageButton_next;
        imageButton_next = findViewById(R.id.imageButton_next);
        imageButton_next.setVisibility(View.INVISIBLE);

        ImageButton imageButton_close;
        imageButton_close = findViewById(R.id.imageButton_closelog);
        imageButton_close.setTag(0); //实现按多次不同效果
        imageButton_close.setVisibility(View.INVISIBLE);

        ImageButton imageButton_Dice;
        imageButton_Dice = findViewById(R.id.imageButton_Dice_for_Battle);
        imageButton_Dice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d20_battle.dice_result = d20_battle.D20_Num(1);
                int attack_bonus = d20_battle.dice_before_battle(d20_battle.dice_result);
                Toast.makeText(Room0.this, "1d20的Roll点结果为" +
                        d20_battle.dice_result + "    攻击力增益" + attack_bonus,
                        Toast.LENGTH_LONG).show();

                hero.attack_power = hero.attack_power + attack_bonus;
                if(hero.attack_power <= 0)
                    textView_show_attackpower.setText("攻击力 1");
                else
                    textView_show_attackpower.setText("攻击力 " + hero.attack_power);
                //d20的roll点结果将反映到攻击力上，+-5，若小于等于0则为1（保底值）


                //战斗前roll完d20后，骰子按钮消失，不允许玩家重复roll
                imageButton_Dice.setVisibility(View.INVISIBLE);
                imageButton_close.setVisibility(View.VISIBLE);

                StringBuilder battle_log = new StringBuilder();
                if (hero.speed >= monster.speed)
                {
                    battle_log.append(hero.name + "先发制人！\n");
                    while(hero.hp > 0 && monster.hp > 0) {
                        //己方先攻击
                        monster.hp = monster.hp - monster.attacked_by(hero);

                        battle_log.append(hero.name + "对敌人造成了" +
                                monster.attacked_by(hero) + "点伤害\n");
                        if (monster.hp <= 0) {
                            monster.alive = false;
                            battle_log.append(monster.name + "被打倒了！\n");
                            break;
                        }
                        //敌方后攻击
                        hero.hp = hero.hp - hero.attacked_by(monster);
                        battle_log.append("敌人对" + hero.name + "造成了" +
                                hero.attacked_by(monster) + "点伤害\n");
                        if (hero.hp <= 0) {
                            hero.hp = 0;
                            textView_show_hp.setText("生命值 " + hero.hp);
                            hero.alive = false;
                            battle_log.append(hero.name + "被打倒了！\n");
                            break;
                        }
                        textView_show_hp.setText("生命值 " + hero.hp);
                    }
                }
                else
                {
                    //敌方先攻击
                    battle_log.append(hero.name + "被敌人抢占先机！\n");
                    while(hero.hp > 0 && monster.hp > 0) {
                        hero.hp = hero.hp - hero.attacked_by(monster);
                        battle_log.append("敌人对" + hero.name + "造成了" +
                                hero.attacked_by(monster) + "点伤害\n");
                        if (hero.hp <= 0) {
                            hero.hp = 0;
                            textView_show_hp.setText("生命值 " + hero.hp);
                            hero.alive = false;
                            battle_log.append(hero.name + "被打倒了！\n");
                            break;
                        }
                        textView_show_hp.setText("生命值 " + hero.hp);
                        //己方后攻击
                        monster.hp = monster.hp - monster.attacked_by(hero);
                        battle_log.append(hero.name + "对敌人造成了" +
                                monster.attacked_by(hero) + "点伤害\n");
                        if (monster.hp <= 0) {
                            monster.alive = false;
                            battle_log.append(monster.name + "被打倒了！\n");
                            break;
                        }
                    }
                }
                textView_show.setText(battle_log.toString());
            }
        });

        imageButton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (Integer)view.getTag();
                if (!hero.alive)
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder (Room0.this);
                    dialog.setTitle("生命值归零");
                    dialog.setMessage("您已被击败");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.
                            OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Room0.this,
                                    End_Fail.class);
                            startActivity(intent);
                        }
                    });
//                    dialog.setNegativeButton("Cancel", new DialogInterface.
//                            OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    });
                    dialog.show();
                }
                else {
                    StringBuilder battle_end_log = new StringBuilder();
                    switch (tag) {
                        case 0:
                            YoYo.with(Techniques.FadeOut)
                                    .duration(1000)
                                    .repeat(0)
                                    .playOn(image_skull);

                            battle_end_log.append("获得道具：长剑\n");
                            hero.attack_power = hero.attack_power + 2;
                            textView_show_attackpower.setText("攻击力 " + hero.attack_power);
                            textView_show.setText(battle_end_log.toString());
                            //侧边栏背包内武器显示
                            recyclerView.setVisibility(View.VISIBLE);
                            view.setTag(1);
                            break;
                        case 1:
                            battle_end_log.append("获得道具：长剑\n");
                            battle_end_log.append("获得金币：8000\n");

                            textView_show.setText(battle_end_log.toString());
                            hero.money = hero.money + 8000;
                            textView_show_money.setText("$ " + hero.money);
                            view.setTag(2);
                            break;
                        case 2:
                            battle_end_log.append("获得道具：长剑\n");
                            battle_end_log.append("获得金币：8000\n");
                            battle_end_log.append("打倒了" + monster.name + "，获得经验值100\n");
                            hero.exp = hero.exp + 100;
                            if (hero.isUpgrade()) {
                                battle_end_log.append("升级了！" + hero.name +
                                        "升至" + hero.level + "级！");
                            }
                            textView_show_level.setText("level " + hero.level + " exp " + hero.exp);
                            textView_show.setText(battle_end_log.toString());
                            imageButton_close.setVisibility(View.INVISIBLE);
                            imageButton_next.setVisibility(View.VISIBLE);
//                            imageButton_test.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        });
        imageButton_next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View v){
                SharedPreferences.Editor editor = getSharedPreferences("data",
                        MODE_PRIVATE).edit();
                editor.putString("hero_name", hero.name);
                editor.putInt("hero_hp", hero.hp);
                editor.putInt("hero_level", hero.level);
                editor.putInt("hero_exp", hero.exp);
                editor.putInt("hero_money", hero.money);
                editor.apply();

                Intent intent0 = new Intent(Room0.this, Shop.class);
                startActivity(intent0);
            }
        });
    }
    //武器库
    private void initWeapons() {


        Weapon sword = new Weapon("长剑", R.drawable.sword,
                "一把普通的长剑，剑柄处已经发黑", "攻击力+2", 1000);
        weaponList.add(sword);
//
//        Weapon skull = new Weapon("堕落者之颅", R.drawable.skull,
//                "萨奇尔曾是艾瑞达的一位伟大领袖，而如今这颗颅骨是他唯一的遗物",
//                "每当你掷点数时可以掷两枚d20，取二者最大值作为结果", 4000);
//        weaponList.add(skull);
//
//        Weapon kingslayers = new Weapon("弑君", R.drawable.kingslayers,
//                "手握痛楚和哀伤——只要能生存下去，迦罗娜别无他求",
//                "每次攻击将造成两次攻击伤害", 4000);
//        weaponList.add(kingslayers);

//                            weaponList.add(skull);
//                            adapter.notifyDataSetChanged();

    }
}