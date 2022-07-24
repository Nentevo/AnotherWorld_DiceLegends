package com.room227.anotherworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

public class BossRush extends AppCompatActivity {

    private TiaoZiUtil tiaoziUtil1;
    private TiaoZiUtil tiaoziUtil2;
    private TiaoZiUtil tiaoziUtil_boss_end1;

    private ImageView mIvHead;
    private SlideMenu slideMenu;
    private List<Weapon> weaponList = new ArrayList<>();
    private int damage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_boss);
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


        Dice d20_battle = new Dice();

        //定义玩家与boss
        Character hero = new Character();
        Boss monster = new Boss();

        //从SharedPreferences中获取先前的信息
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        hero.name = pref.getString("hero_name", "");

        hero.hp = pref.getInt("hero_hp", 0);
        hero.attack_power = pref.getInt("hero_attackpower", 0);
        hero.defense_power = pref.getInt("hero_defensepower", 0);
        hero.speed = pref.getInt("hero_speed", 0);
        hero.alive = true;
        hero.exp = pref.getInt("hero_exp", 0);
        hero.level = pref.getInt("hero_level", 1);
        hero.money = pref.getInt("hero_money", 0);

        monster.name = "卡奥斯";
        monster.hp = 30;
        monster.attack_power = 5;
        monster.defense_power = 10;
        monster.speed = 20;
        monster.alive = true;


        TextView textView_show, textView_show_hp, textView_show_attackpower,
                textView_show_defensepower,textView_show_speed,
                textView_show_level, textView_show_money;
        textView_show = findViewById(R.id.textView_show);
        //加滚动条
        textView_show.setMovementMethod(ScrollingMovementMethod.getInstance());
//        textView_show.setText("掷d20，开始战斗！\n");

        //卡奥斯进场动画，需要定义imageview
        ImageView image_chaos;
        image_chaos = findViewById(R.id.imageView_chaos);

        YoYo.with(Techniques.BounceInDown)
                .duration(4000)
                .repeat(0)
                .playOn(image_chaos);

        String s1 = "我名为「卡奥斯」。         \n\n" +
                "相信你已经做好了觉悟，      \n\n" +
                "那就准备受死吧！";
        tiaoziUtil1 = new TiaoZiUtil(textView_show, s1, 100);


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
        textView_show_money.setText("$ "+ hero.money);

        //界面右下角的向右箭头
        ImageButton imageButton_next;
        imageButton_next = findViewById(R.id.imageButton_next);
        imageButton_next.setVisibility(View.INVISIBLE);

        //对话框右下角的向下箭头
        ImageButton imageButton_close;
        imageButton_close = findViewById(R.id.imageButton_closelog);
        imageButton_close.setTag(0); //实现按多次不同效果
        imageButton_close.setVisibility(View.INVISIBLE);

        //d20
        ImageButton imageButton_Dice;
        imageButton_Dice = findViewById(R.id.imageButton_Dice_for_Battle);
        imageButton_Dice.setTag(0); //实现按多次骰子不同效果
        imageButton_Dice.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int tag_dice = (Integer)v.getTag();
                //创建攻防日志
                StringBuilder battle_log_dice = new StringBuilder();
                switch (tag_dice)
                {
                    case 0:
                        //由于收藏品效果，现在一次扔两个骰子，用D20_double()
                        d20_battle.dice_result = d20_battle.D20_double();
                        int attack_bonus = d20_battle.dice_before_battle(d20_battle.dice_result);

                        textView_show.setText("掷1d20，Roll点结果为：" +
                                        d20_battle.dice_result + "\n攻击力增益：" + attack_bonus);

                        hero.attack_power = hero.attack_power + attack_bonus;
                        if(hero.attack_power <= 0)
                        {
                            textView_show_attackpower.setText("攻击力 1");
                            hero.attack_power = 2;
                        }
                        else
                        {
                            //d20的roll点结果将反映到攻击力上，+-5，若小于等于0则为1（保底值）
                            textView_show_attackpower.setText("攻击力 " + hero.attack_power);
                            //弑君的效果，每次攻击两次
                            hero.attack_power = 2 * hero.attack_power;
                        }

                        imageButton_Dice.setVisibility(View.INVISIBLE);
                        imageButton_close.setVisibility(View.VISIBLE);

                        v.setTag(1);
                        break;

                    case 1:
                        //roll诅咒法令的判定，1d20
                        d20_battle.dice_result = monster.DamningEdict();
                        textView_show.setText("1d20的Roll点结果为\n\n" +
                                d20_battle.dice_result);
                        //下一步是对话，所以骰子隐藏
                        imageButton_Dice.setVisibility(View.INVISIBLE);
                        imageButton_close.setVisibility(View.VISIBLE);
                        v.setTag(2);
                        break;

                    case 2: //战斗roll点，敌方攻击我方，我方防御判定
                        imageButton_Dice.setVisibility(View.INVISIBLE);
                        imageButton_close.setVisibility(View.VISIBLE);


                        d20_battle.dice_result = d20_battle.D20_double();
                        battle_log_dice.append("1d20结果为：" + d20_battle.dice_result + "\n\n");
                        //根据1d20结果判断
                        if(d20_battle.dice_result == 1) {
                            damage = 2 * hero.attacked_by(monster);
                            battle_log_dice.append("下一次受到两倍伤害！\n");
                        }
                        else if(d20_battle.dice_result >= 2 && d20_battle.dice_result <= 15) {
                            damage = hero.attacked_by(monster);
                            battle_log_dice.append("下一次受到正常伤害！\n");
                        }
                        else if(d20_battle.dice_result >= 16 && d20_battle.dice_result <= 19) {
                            damage = hero.attacked_by(monster) / 2;
                            if(damage <= 1)
                                damage = 1;
                            battle_log_dice.append("下一次受到的伤害减半！\n");
                        }
                        else {
                            damage = 0;
                            battle_log_dice.append("免疫下一次受到的伤害！\n");
                        }

//                        battle_log_dice.append(hero.name + "被敌人抢占先机！\n");
//                        hero.hp = hero.hp - damage;
//                        battle_log_dice.append("敌人对" + hero.name + "造成了" +
//                                damage + "点伤害\n");
                        hero.hp = hero.hp - damage;
                        //若己方生命值小于等于零
                        if (hero.hp <= 0)
                        {
                            hero.hp = 0;
                            textView_show_hp.setText("生命值 " + hero.hp);
                            hero.alive = false;
                            battle_log_dice.append("敌人对" + hero.name + "造成了" +
                                damage + "点伤害\n");
                            battle_log_dice.append(hero.name + "被卡奥斯打倒了！\n");
                            imageButton_close.setTag(-1);
                        }
                        else
                        {
                            textView_show_hp.setText("生命值 " + hero.hp);
                            battle_log_dice.append("敌人对" + hero.name + "造成了" +
                                    damage + "点伤害\n");
                        }
                        textView_show.setText(battle_log_dice.toString());
                        v.setTag(3);
                        break;

                    case 3: //战斗roll点，我方攻击敌方，我方攻击判定
                        imageButton_Dice.setVisibility(View.INVISIBLE);

                        YoYo.with(Techniques.Shake)
                                .duration(2000)
                                .repeat(0)
                                .playOn(image_chaos);

                        d20_battle.dice_result = d20_battle.D20_double();
                        battle_log_dice.append("1d20结果为：" + d20_battle.dice_result + "\n\n");

                        if(d20_battle.dice_result == 1) {
                            damage = 0;
                            battle_log_dice.append("下一次攻击判定为MISS！\n");
                        }
                        else if(d20_battle.dice_result >= 2 && d20_battle.dice_result <= 5) {
                            damage = monster.attacked_by(hero) / 2;
                            if(damage <= 1)
                                damage = 1;
                            battle_log_dice.append("下一次攻击造成的伤害变为原本的一半！\n");
                        }
                        else if(d20_battle.dice_result >= 6 && d20_battle.dice_result <= 15) {
                            damage = monster.attacked_by(hero);
                            battle_log_dice.append("下一次攻击造成正常伤害！\n");
                        }
                        else {
                            damage = monster.attacked_by(hero) * 2;
                            battle_log_dice.append("下一次攻击造成的伤害翻倍！\n");
                        }

                        monster.hp = monster.hp - damage;
                        if (monster.hp <= 0)
                        {
                            monster.hp = 0;
                            monster.alive = false;
                            battle_log_dice.append(hero.name + "对敌人造成了" +
                                monster.attacked_by(hero) + "点伤害\n");
                            battle_log_dice.append(monster.name + "被打倒了！\n");

                            //进入boss临终宣言
                            imageButton_close.setVisibility(View.VISIBLE);
//                            imageButton_next.setVisibility(View.VISIBLE);
                            imageButton_close.setTag(7);
                        }
                        else
                        {
                            battle_log_dice.append(hero.name + "对敌人造成了" +
                                    monster.attacked_by(hero) + "点伤害\n");
                            //随后正常进入循环
                            imageButton_close.setVisibility(View.VISIBLE);

                            //判定混沌弥散事件
                            int dice_CD = d20_battle.D20_Num(1);
                            if(dice_CD >= 11)
                                v.setTag(2);
                            else
                                imageButton_close.setTag(5); //进入case 5 混沌弥散
                        }
                        textView_show.setText(battle_log_dice.toString());
                        break;
                    case 4: //为混沌弥散进行1d20判定
                        d20_battle.dice_result = monster.ChaoticDispersion();
                        textView_show.setText("1d20的Roll点结果为\n\n" +
                                d20_battle.dice_result);
                        //下一步是对话，所以骰子隐藏
                        imageButton_Dice.setVisibility(View.INVISIBLE);
                        imageButton_close.setVisibility(View.VISIBLE);
                        v.setTag(3); //占用了敌方一次行动，因此下一次为我方攻击
                        break;
                    default:
                        break;
                }
            }
        });

        imageButton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (Integer)view.getTag();
                if (!hero.alive)
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder (BossRush.this);
                    dialog.setTitle("生命值归零");
                    dialog.setMessage("您已被击败");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.
                            OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(BossRush.this,
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
                    StringBuilder battle_log_close = new StringBuilder();
                    switch (tag) {
                        case -1:
                            AlertDialog.Builder dialog = new AlertDialog.Builder (BossRush.this);
                            dialog.setTitle("生命值归零");
                            dialog.setMessage("您已被击败");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("OK", new DialogInterface.
                                    OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(BossRush.this,
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
                            break;
                        case 0:
                            //boss放话，准备开场诅咒法令
                            String s2 = "「别  \n" +
                                    "    想  \n" +
                                    "    逃」";
                            tiaoziUtil2 = new TiaoZiUtil(textView_show, s2, 50);
                            view.setTag(1);
                            break;
                        case 1:
                            //宣告诅咒法令
                            YoYo.with(Techniques.Tada)
                                    .duration(2000)
                                    .repeat(0)
                                    .playOn(image_chaos);
                            textView_show.setText(
                                    Html.fromHtml(getString(R.string.DamningEdict_declaration)));
                            imageButton_close.setVisibility(View.INVISIBLE);
                            imageButton_Dice.setVisibility(View.VISIBLE);
                            view.setTag(2);
                            break;
                        case 2:
                            //诅咒法令判定结果
                            if(d20_battle.dice_result <= 15)
                            {
                                hero.speed = 0;
                                textView_show_speed.setText("速度值 " + hero.speed);
                                textView_show.setText(
                                        Html.fromHtml(getString(R.string.DamningEdict_succeed)));
                            }
                            else
                            {
                                textView_show.setText(
                                        Html.fromHtml(getString(R.string.DamningEdict_fail)));
                            }

                            //下一步是roll 1d20
                            imageButton_Dice.setVisibility(View.INVISIBLE);
                            imageButton_close.setVisibility(View.VISIBLE);
                            view.setTag(3);
                            break;
                        case 3: //敌方攻击我方的roll点说明
                            imageButton_Dice.setVisibility(View.VISIBLE);
                            imageButton_close.setVisibility(View.INVISIBLE);

                            YoYo.with(Techniques.Bounce)
                                    .duration(2000)
                                    .repeat(0)
                                    .playOn(image_chaos);

                            //说明roll点结果产生的效果
                            battle_log_close.append("卡奥斯进行攻击！\n\n");
                            battle_log_close.append("掷1d20\n" +
                                    "1：下一次受到两倍伤害\n" +
                                    "2-15：下一次受到正常伤害\n" +
                                    "16-19：下一次受到的伤害减半\n" +
                                    "20：免疫下一次伤害\n\n");
                            textView_show.setText(battle_log_close.toString());
                            view.setTag(4);
                            break;
                        case 4: //我方攻击敌方的roll点说明
                            imageButton_Dice.setVisibility(View.VISIBLE);
                            imageButton_close.setVisibility(View.INVISIBLE);
                            battle_log_close.append(hero.name + "进行攻击！\n\n");
                            battle_log_close.append("掷1d20\n" +
                                    "1：下一次攻击判定为MISS\n" +
                                    "2-5：下一次攻击造成的伤害变为原本的一半\n" +
                                    "6-15：下一次攻击造成正常伤害\n" +
                                    "16-20：下一次攻击造成的伤害翻倍\n\n");
                            textView_show.setText(battle_log_close.toString());
                            view.setTag(3);
                            break;
                        case 5: //宣告混沌弥散
                            YoYo.with(Techniques.Tada)
                                    .duration(2000)
                                    .repeat(0)
                                    .playOn(image_chaos);
                            textView_show.setText(
                                    Html.fromHtml(getString(R.string.ChaoticDispersion_declaration)));
                            imageButton_close.setVisibility(View.INVISIBLE);
                            imageButton_Dice.setVisibility(View.VISIBLE);
                            view.setTag(6);
                            imageButton_Dice.setTag(4); //1d20判定混沌弥散
                            break;
                        case 6:
                            //混沌弥散判定结果
                            if(d20_battle.dice_result <= 15)
                            {
                                if(hero.hp >= 2)
                                {
                                    hero.hp = hero.hp / 2;
                                    textView_show_hp.setText("生命值 " + hero.hp);
                                    textView_show.setText(
                                            Html.fromHtml(getString(R.string.ChaoticDispersion_succeed)));
                                    view.setTag(4); //占用敌方一次行动，随后轮到我方攻击
                                }
                                else //当己方生命值为1时，混沌弥散直接击杀
                                {
                                    hero.hp = 0;
                                    textView_show_hp.setText("生命值 " + hero.hp);
                                    hero.alive = false;
                                    textView_show.setText("「混沌弥散」\n直接击杀生命值为1的单位！");
                                    view.setTag(-1); //直接被击杀
                                }
                            }
                            else
                            {
                                textView_show.setText(
                                        Html.fromHtml(getString(R.string.ChaoticDispersion_fail)));
                                view.setTag(4); //占用敌方一次行动，随后轮到我方攻击
                            }

                            //下一步是roll 1d20
                            imageButton_Dice.setVisibility(View.INVISIBLE);
                            imageButton_close.setVisibility(View.VISIBLE);
                            break;
                        case 7: //boss临终宣言
                            String str_boss_end1 = "果然……      \n\n" +
                                    "如此吗……";
                            tiaoziUtil_boss_end1 = new TiaoZiUtil(textView_show,
                                    str_boss_end1, 100);
                            view.setTag(8);
                            break;
                        case 8: //boss临终宣言第二部分，
                            imageButton_close.setVisibility(View.INVISIBLE);
                            imageButton_next.setVisibility(View.VISIBLE);
                            str_boss_end1 = "终究……    \n\n" +
                                    "我还是敌不过宿命……";
                            tiaoziUtil_boss_end1 = new TiaoZiUtil(textView_show,
                                    str_boss_end1, 200);
                            YoYo.with(Techniques.FadeOut)
                                    .duration(4000)
                                    .repeat(0)
                                    .playOn(image_chaos);
                            break;
                        default:
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
                editor.putInt("hero_hp", hero.hp);
                editor.putInt("hero_level", hero.level);
                editor.putInt("hero_exp", hero.exp);
                editor.apply();

                Intent intent1 = new Intent(BossRush.this, End.class);
                startActivity(intent1);
            }
        });
    }

    private void initWeapons() {

        Weapon sword = new Weapon("长剑", R.drawable.sword,
                "一把普通的长剑，剑柄处已经发黑", "攻击力+2", 1000);
        weaponList.add(sword);

        Weapon skull = new Weapon("堕落者之颅", R.drawable.skull,
                "萨奇尔曾是艾瑞达的一位伟大领袖，而如今这颗颅骨是他唯一的遗物",
                "每当你掷点数时可以掷两枚d20，取二者最大值作为结果", 4000);
        weaponList.add(skull);

        Weapon kingslayers = new Weapon("弑君", R.drawable.kingslayers,
                "手握痛楚和哀伤——只要能生存下去，迦罗娜别无他求",
                "每次攻击将造成两次攻击伤害", 4000);
        weaponList.add(kingslayers);

    }
}