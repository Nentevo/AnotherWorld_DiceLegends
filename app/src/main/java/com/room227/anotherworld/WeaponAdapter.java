package com.room227.anotherworld;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeaponAdapter extends RecyclerView.Adapter<WeaponAdapter.ViewHolder> {
    private List<Weapon> mWeaponList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        View weaponView;
        ImageView weaponImage;
        TextView weaponName;

        public ViewHolder(View view) {
            super(view);
            weaponView = view;
            weaponImage = (ImageView) view.findViewById(R.id.weapon_image);
            weaponName = (TextView) view.findViewById(R.id.weapon_name);
        }
    }
    public WeaponAdapter(List<Weapon> weaponList) {
        mWeaponList = weaponList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weapon_item, parent, false);
//        ViewHolder holder = new ViewHolder(view);
        final ViewHolder holder = new ViewHolder(view);

        holder.weaponView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Weapon weapon = mWeaponList.get(position);
                Toast.makeText(v.getContext(), "" + weapon.getAbility(),
                        Toast.LENGTH_LONG).show();
            }
        });

        holder.weaponImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Weapon weapon = mWeaponList.get(position);
                Toast.makeText(v.getContext(), "" + weapon.getDescription(),
                        Toast.LENGTH_LONG).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Weapon weapon = mWeaponList.get(position);
        holder.weaponImage.setImageResource(weapon.getImageId());
        holder.weaponName.setText(weapon.getName());
    }

    @Override
    public int getItemCount() {
        return mWeaponList.size();
    }

    public void addWeapon(Weapon weapon) {
        mWeaponList.add(weapon);
        notifyDataSetChanged();
        notifyItemInserted(getItemCount());
    }
}
