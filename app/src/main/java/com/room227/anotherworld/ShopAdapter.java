package com.room227.anotherworld;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    private List<Weapon> mShopList;
    private List<Weapon> weaponList = new ArrayList<>();
    WeaponAdapter adapter = new WeaponAdapter(weaponList);

    private OnMyItemClickListener onMyItemClickListener;//定义的接口

    //接口的setget方法 对外提供
    public void setOnMyItemClickListener(OnMyItemClickListener onMyItemClickListener) {
        this.onMyItemClickListener = onMyItemClickListener;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        View shopView;
        ImageView shopImage;
        TextView shopName;
        TextView shopCost;

        public ViewHolder(View view) {
            super(view);
            shopView = view;
            shopImage = (ImageView) view.findViewById(R.id.shop_image);
            shopName = (TextView) view.findViewById(R.id.shop_name);
            shopCost = (TextView) view.findViewById(R.id.shop_cost);
        }
    }
    public ShopAdapter(List<Weapon> shopList) {
        mShopList = shopList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_item, parent, false);
//        ViewHolder holder = new ViewHolder(view);
        final ViewHolder holder = new ViewHolder(view);

        holder.shopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Weapon shopitem = mShopList.get(position);
                Toast.makeText(v.getContext(), "" + shopitem.getDescription(),
                        Toast.LENGTH_LONG).show();
            }
        });

        holder.shopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Weapon shopitem = (Weapon) mShopList.get(position);
                Toast.makeText(v.getContext(), "" + shopitem.getAbility(),
                        Toast.LENGTH_LONG).show();
            }
        });


        holder.shopCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Weapon shopitem = mShopList.get(position);
                onMyItemClickListener.onMyItemClick(shopitem, position,
                        shopitem.name, shopitem.cost);
                removeItem(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Weapon shopitem = mShopList.get(position);
        holder.shopImage.setImageResource(shopitem.getImageId());
        holder.shopName.setText(shopitem.getName());
        holder.shopCost.setText("$ " + shopitem.getCost());
    }

    @Override
    public int getItemCount() {
        return mShopList.size();
    }


//    public void addWeapon(Weapon weapon) {
//        mShopList.add(ShopItem);
//        //notifyDataSetChanged();
//        notifyItemInserted(getItemCount());
//    }

    public void removeItem(int position) {
        mShopList.remove(position);
        notifyItemRemoved(position); // 提醒item删除指定数据
        notifyDataSetChanged();// 提醒list刷新，没有动画效果
    }

    // 接口回调
    public interface OnMyItemClickListener{
        void onMyItemClick(Weapon item, int position, String name, int cost);
    }

}
