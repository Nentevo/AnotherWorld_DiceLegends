package com.room227.anotherworld;

import android.annotation.SuppressLint;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Shop extends AppCompatActivity {

    private TiaoZiUtil tiaoziUtil_buy;

    private ImageView mIvHead;
    private SlideMenu slideMenu;
    private List<Weapon> weaponList = new ArrayList<>();
    private List<Weapon> shopList = new ArrayList<>();

    static Weapon skull = new Weapon("堕落者之颅", R.drawable.skull,
                "萨奇尔曾是艾瑞达的一位伟大领袖，而如今这颗颅骨是他唯一的遗物",
                "每当你掷点数时可以掷两枚d20，取二者最大值作为结果", 4000);

    static Weapon kingslayers = new Weapon("弑君", R.drawable.kingslayers,
                "手握痛楚和哀伤——只要能生存下去，迦罗娜别无他求",
                "每次攻击将造成两次攻击伤害", 4000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_shop);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 隐藏顶部状态栏

        //侧边栏
        mIvHead =  findViewById(R.id.iv_head);
        slideMenu = findViewById(R.id.slideMenu);
        mIvHead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                slideMenu.switchMenu();
            }
        });

//        //侧边栏背包内武器显示
        initWeapons();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        WeaponAdapter adapter = new WeaponAdapter(weaponList);
        recyclerView.setAdapter(adapter);

//        //商店页面武器显示
        initShopItems();
        RecyclerView recyclerView_shop = (RecyclerView) findViewById(R.id.recycler_view_shop);
        LinearLayoutManager layoutManager_shop = new LinearLayoutManager(this);
        recyclerView_shop.setLayoutManager(layoutManager_shop);
        ShopAdapter adapter_shop = new ShopAdapter(shopList);
        recyclerView_shop.setAdapter(adapter_shop);

        //从SharedPreferences中获取先前的信息
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        Character hero = new Character();
        hero.name = pref.getString("hero_name", "");
        hero.exp = pref.getInt("hero_exp", 0);
        hero.level = pref.getInt("hero_level", 1);
        hero.money = pref.getInt("hero_money", 0);

        TextView textView_show, textView_show_level, textView_show_money;
        textView_show_level = findViewById(R.id.textView_show_level);
        textView_show_money = findViewById(R.id.textView_show_money);
        textView_show_level.setText("level " + hero.level + " exp " + hero.exp);
        textView_show_money.setText("$ "+ hero.money);

        textView_show = findViewById(R.id.textView_show);
        textView_show.setMovementMethod(ScrollingMovementMethod.getInstance());

        //利用回调接口获取购买的武器与对应信息
        adapter_shop.setOnMyItemClickListener(new ShopAdapter.OnMyItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            public void onMyItemClick(Weapon weapon, int position, String name, int cost) {
                Toast.makeText(Shop.this, hero.name + "购买了" + name,
                        Toast.LENGTH_SHORT).show();
                //左侧背包栏加对应物品
                weaponList.add(weapon);
                adapter.notifyDataSetChanged();
                //金币对应减少
                hero.money = hero.money - cost;
                textView_show_money.setText("$ "+ hero.money);
                String str_buy = "谢谢惠顾！";
                tiaoziUtil_buy = new TiaoZiUtil(textView_show, str_buy, 100);
            }
        });



        Dice d20_shop = new Dice();

        ImageButton imageButton_next = findViewById(R.id.imageButton_next);
        imageButton_next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View v){
                SharedPreferences.Editor editor = getSharedPreferences("data",
                        MODE_PRIVATE).edit();
                editor.putInt("hero_money", hero.money);
                editor.apply();

                Intent intent0 = new Intent(Shop.this, BossRush.class);
                startActivity(intent0);
            }
        });
    }




//    //物品列表
    private void initWeapons() {

        Weapon sword = new Weapon("长剑", R.drawable.sword,
                "一把普通的长剑，剑柄处已经发黑", "攻击力+2", 1000);
        weaponList.add(sword);

//        Weapon skull = new Weapon("堕落者之颅", R.drawable.skull,
//                "萨奇尔曾是艾瑞达的一位伟大领袖，而如今这颗颅骨是他唯一的遗物",
//                "每当你掷点数时可以掷两枚d20，取二者最大值作为结果");
//        weaponList.add(skull);
//
//        Weapon kingslayers = new Weapon("弑君", R.drawable.kingslayers,
//                "手握痛楚和哀伤——只要能生存下去，迦罗娜别无他求",
//                "每次攻击将造成两次攻击伤害");
//        weaponList.add(kingslayers);

    }
//
    private void initShopItems() {

        Weapon medicine = new Weapon("万灵药", R.drawable.medicine,
                "它的效果远超你的想象——当然它的价格也是如此",
                "生命值+20", 1000);
        shopList.add(medicine);

        Weapon skull = new Weapon("堕落者之颅", R.drawable.skull,
                "萨奇尔曾是艾瑞达的一位伟大领袖，而如今这颗颅骨是他唯一的遗物",
                "每当你掷点数时可以掷两枚d20，取二者最大值作为结果", 4000);
        shopList.add(skull);

        Weapon kingslayers = new Weapon("弑君", R.drawable.kingslayers,
                "手握痛楚和哀伤——只要能生存下去，迦罗娜别无他求",
                "每次攻击将造成两次攻击伤害", 4000);
        shopList.add(kingslayers);

        Weapon powder = new Weapon("忍者的药粉(速溶型)", R.drawable.powder,
                "东瀛忍者必会随身携带之物——无论是否会用到它",
                "速度+15", 200);
        shopList.add(powder);
    }
}