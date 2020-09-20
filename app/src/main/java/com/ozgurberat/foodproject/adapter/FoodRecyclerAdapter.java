package com.ozgurberat.foodproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ozgurberat.foodproject.R;
import com.ozgurberat.foodproject.model.Food;
import com.ozgurberat.foodproject.util.GlideHelper;

import java.util.ArrayList;
import java.util.List;

public class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.FoodCardViewHolder> {

    public interface FoodViewListener {
        void onFoodClicked(Food food);
        void onSaveClicked(Food food);
    }

    private Context context;
    private List<Food> foods;
    private FoodViewListener foodViewListener;

    public FoodRecyclerAdapter(Context context, List<Food> foods, FoodViewListener foodViewListener) {
        this.context = context;
        this.foods = foods;
        this.foodViewListener = foodViewListener;
    }

    public void updateData(List<Food> foodsList) {
        foods.clear();
        foods.addAll(foodsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_cardview, parent, false);
        return new FoodCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCardViewHolder holder, int position) {
        holder.foodName.setText(foods.get(position).getMealName());

        Glide.with(context)
                .setDefaultRequestOptions(GlideHelper.setRequestOptions(context, false))
                .load(foods.get(position).getMealImage())
                .into(holder.foodImage);

        holder.foodCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodViewListener.onFoodClicked(foods.get(position));
            }
        });

        holder.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodViewListener.onSaveClicked(foods.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class FoodCardViewHolder extends RecyclerView.ViewHolder {

        CardView foodCard;
        ImageView foodImage;
        TextView foodName;
        AppCompatImageButton saveButton;

        public FoodCardViewHolder(@NonNull View itemView) {
            super(itemView);

            foodCard = itemView.findViewById(R.id.food_card);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            saveButton = itemView.findViewById(R.id.food_save_button);
        }
    }

}
