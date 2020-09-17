package com.ozgurberat.foodproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ozgurberat.foodproject.R;
import com.ozgurberat.foodproject.model.Category;
import com.ozgurberat.foodproject.util.GlideHelper;

import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryCardViewHolder> {

    public interface CategoryViewListener {
        void onCategoryClicked(Category category);
    }

    private Context context;
    private List<Category> categories;
    private CategoryViewListener categoryViewListener;

    public CategoryRecyclerAdapter(Context context, List<Category> categories, CategoryViewListener categoryViewListener) {
        this.context = context;
        this.categories = categories;
        this.categoryViewListener = categoryViewListener;
    }

    @NonNull
    @Override
    public CategoryCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_cardview, parent, false);
        return new CategoryCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryCardViewHolder holder, int position) {
        holder.categoryText.setText(categories.get(position).getCategoryName());

        Glide.with(context)
                .setDefaultRequestOptions(GlideHelper.setRequestOptions(context, true))
                .load(categories.get(position).getCategoryImage())
                .into(holder.categoryImage);

        holder.categoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryViewListener.onCategoryClicked(categories.get(position));
            }
        });

    }



    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void updateData(List<Category> categoryList) {
        categories.clear();
        categories.addAll(categoryList);
        notifyDataSetChanged();
    }

    class CategoryCardViewHolder extends RecyclerView.ViewHolder {

        ImageView categoryImage;
        TextView categoryText;
        CardView categoryCard;

        public CategoryCardViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.category_image);
            categoryText = itemView.findViewById(R.id.category_text);
            categoryCard = itemView.findViewById(R.id.category_card);

        }
    }
}
