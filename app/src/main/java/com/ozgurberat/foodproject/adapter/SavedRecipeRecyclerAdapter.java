package com.ozgurberat.foodproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ozgurberat.foodproject.R;
import com.ozgurberat.foodproject.model.Recipe;
import com.ozgurberat.foodproject.util.GlideHelper;

import java.util.List;

public class SavedRecipeRecyclerAdapter extends RecyclerView.Adapter<SavedRecipeRecyclerAdapter.SavedRecipeViewHolder> {

    public interface SavedRecipeViewListener {
        void onSavedRecipeClicked(Recipe recipe);
        void onDeleteClicked(Recipe recipe);
    }

    private Context context;
    private List<Recipe> savedRecipes;
    private SavedRecipeViewListener listener;

    public SavedRecipeRecyclerAdapter(Context context, List<Recipe> savedRecipes, SavedRecipeViewListener listener) {
        this.context = context;
        this.savedRecipes = savedRecipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SavedRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.saved_recipes_cardview, parent, false);
        return new SavedRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedRecipeViewHolder holder, int position) {
        holder.textView.setText(savedRecipes.get(position).getMealName());

        Glide.with(context)
                .setDefaultRequestOptions(GlideHelper.setRequestOptions(context, false))
                .load(savedRecipes.get(position).getMealImage())
                .onlyRetrieveFromCache(true)
                .into(holder.recipeImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSavedRecipeClicked(savedRecipes.get(position));
            }
        });

        holder.popupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.popupImage);
                popupMenu.getMenuInflater().inflate(R.menu.saved_recipe_card_menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_delete) {
                            listener.onDeleteClicked(savedRecipes.get(position));
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return savedRecipes.size();
    }

    public void updateData(List<Recipe> recipes) {
        savedRecipes.clear();
        savedRecipes.addAll(recipes);
        notifyDataSetChanged();
    }

    class SavedRecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView recipeImage;
        TextView textView;
        CardView cardView;
        ImageView popupImage;

        public SavedRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.saved_recipes_image);
            textView = itemView.findViewById(R.id.saved_recipes_text);
            cardView = itemView.findViewById(R.id.saved_recipes_cardview);
            popupImage = itemView.findViewById(R.id.saved_recipes_popup);
        }
    }
}
















