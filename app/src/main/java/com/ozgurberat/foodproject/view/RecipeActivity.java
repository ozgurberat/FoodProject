package com.ozgurberat.foodproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.ozgurberat.foodproject.R;
import com.ozgurberat.foodproject.model.Recipe;
import com.ozgurberat.foodproject.requests.responses.RecipeResponse;
import com.ozgurberat.foodproject.util.GlideHelper;
import com.ozgurberat.foodproject.viewmodel.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    private static final String TAG = "RecipeActivity";

    private ImageView recipeImage;
    private ProgressBar progressBar;
    private CollapsingToolbarLayout recipeCollapsingToolbar;
    private LinearLayout recipeLinearLayout;
    private TextView timeout;

    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        initViews();

        String id = getIntent().getStringExtra("id");
        String path = getIntent().getStringExtra("intentPath");
        switch (path) {
            case "intentFromMainActivity" : {
                recipeViewModel.fetchRecipe(id);
                System.out.println("main");
                break;
            }
            case "intentFromSavedRecipeActivity" : {
                System.out.println("savedrecipes");
                recipeViewModel.fetchRecipeFromSQLite(id);
                break;
            }
        }
        subscribeObservers();
    }

    private void initViews() {
        recipeImage = findViewById(R.id.recipe_image);
        progressBar = findViewById(R.id.recipe_progress_bar);
        recipeCollapsingToolbar = findViewById(R.id.recipe_collapsing_toolbar);
        recipeLinearLayout = findViewById(R.id.recipe_layout);
        timeout = findViewById(R.id.recipe_timeout);
    }


    private void showProgressBar(Boolean visibility) {
        if (visibility) {
            progressBar.setVisibility(View.VISIBLE);
        }
        else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setCollapsingToolbar(String title) {
        recipeCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        recipeCollapsingToolbar.setCollapsedTitleTypeface(Typeface.DEFAULT_BOLD);
        recipeCollapsingToolbar.setExpandedTitleColor(Color.WHITE);
        recipeCollapsingToolbar.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
        recipeCollapsingToolbar.setExpandedTitleTextAppearance(R.style.TextAppearance);
        recipeCollapsingToolbar.setTitle(title);
    }

    private void showTimedOutScreen(Boolean aBoolean) {
        timeout.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
    }

    private void subscribeObservers() {
        recipeViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                showProgressBar(aBoolean);
            }
        });

        recipeViewModel.getIsTimedOut().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                showTimedOutScreen(aBoolean);
            }
        });

        recipeViewModel.getRecipeFromSQLite().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                System.out.println(recipe);
                List<Recipe> recipeList = new ArrayList<>();
                recipeList.add(recipe);
                RecipeResponse recipeResponse = new RecipeResponse(recipeList);
                recipeViewModel.triggerGetRecipe(recipeResponse);
            }
        });
        recipeViewModel.getRecipe().observe(this, new Observer<RecipeResponse>() {
            @Override
            public void onChanged(RecipeResponse recipeResponse) {
                if (recipeResponse == null || recipeResponse.getRecipe() == null || recipeResponse.getRecipe().size() == 0)
                    return;

                Glide.with(getApplicationContext())
                        .setDefaultRequestOptions(GlideHelper.setRequestOptions(RecipeActivity.this, false))
                        .load(recipeResponse.getRecipe().get(0).getMealImage())
                        .into(recipeImage);

                setCollapsingToolbar(recipeResponse.getRecipe().get(0).getMealName());

                List<String> ingredientList = getIngredientList(recipeResponse);
                List<String> measureList = getMeasureList(recipeResponse);


                //How many ingredient or measure do i need to display?
                int ingredientCounter = 0;
                for (String ingredient: ingredientList) {
                    if (ingredient != null && !ingredient.equals("")) {
                        ingredientCounter++;
                    }
                    else {
                        break;
                    }
                }

                recipeLinearLayout.removeAllViewsInLayout();

                addTextViewToLayout("Ingredients",
                        Color.BLACK,
                        22f,
                        Typeface.DEFAULT_BOLD,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        20,
                        20,
                        20,
                        20,
                        recipeLinearLayout);


                for (int i=0; i<ingredientCounter; i++) {
                    addTextViewToLayout("-"+ "  "+ingredientList.get(i) + ", " + measureList.get(i),
                            Color.BLACK,
                            20f,
                            Typeface.DEFAULT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            20,
                            20,
                            20,
                            20,
                            recipeLinearLayout);
                }

                addTextViewToLayout("Instructions",
                        Color.BLACK,
                        22f,
                        Typeface.DEFAULT_BOLD,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        20,
                        20,
                        20,
                        20,
                        recipeLinearLayout);


                String instructions = recipeResponse.getRecipe().get(0).getMealInstructions();
                String[] instructionsArray = instructions.split("\r\n");
                int step=0;
                for (int i=0; i<instructionsArray.length; i++) {
                    if (instructionsArray[i] != null && !instructionsArray[i].equals("")) {
                        step++;
                        addTextViewToLayout(
                                "Step "+ (step) + ": ",
                                Color.GRAY,
                                20f,
                                Typeface.DEFAULT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                20,
                                20,
                                20,
                                20,
                                recipeLinearLayout
                        );

                        addTextViewToLayout(
                                instructionsArray[i],
                                Color.BLACK,
                                20f,
                                Typeface.DEFAULT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                20,
                                20,
                                20,
                                20,
                                recipeLinearLayout
                        );
                    }

                }



                if (recipeResponse.getRecipe().get(0).getMealYoutubeLink() != null &&
                        !recipeResponse.getRecipe().get(0).getMealYoutubeLink().equals("")) {

                    addTextViewToLayout(
                            "If you watch a youtube video about this recipe, click the link below!",
                            Color.RED,
                            22f,
                            Typeface.SERIF,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            20,
                            20,
                            20,
                            20,
                            recipeLinearLayout
                    );

                    TextView youtubeLink = addTextViewToLayout(
                            recipeResponse.getRecipe().get(0).getMealYoutubeLink(),
                            Color.BLUE,
                            16f,
                            Typeface.SERIF,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            20,
                            20,
                            20,
                            20,
                            recipeLinearLayout
                    );

                    youtubeLink.setLinksClickable(true);
                    Linkify.addLinks(youtubeLink, Linkify.WEB_URLS);
                }


                if (recipeResponse.getRecipe().get(0).getMealSource() != null &&
                        !recipeResponse.getRecipe().get(0).getMealSource().equals("")) {

                    TextView source = addTextViewToLayout(
                            "Source : "+recipeResponse.getRecipe().get(0).getMealSource(),
                            Color.BLACK,
                            16f,
                            Typeface.SERIF,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            20,
                            20,
                            20,
                            20,
                            recipeLinearLayout
                    );

                    source.setLinksClickable(true);
                    Linkify.addLinks(source, Linkify.WEB_URLS);

                }
            }


        });

    }

    private TextView addTextViewToLayout(
            String text,
            int color,
            float size,
            Typeface typeface,
            int width,
            int height,
            int topMargin,
            int botMargin,
            int leftMargin,
            int rightMargin,
            LinearLayout linearLayout
    ) {
        TextView textView = new TextView(RecipeActivity.this);
        textView.setText(text);
        textView.setTextColor(color);
        textView.setTextSize(size);
        textView.setTypeface(typeface);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.topMargin = topMargin;
        layoutParams.bottomMargin = botMargin;
        layoutParams.leftMargin = leftMargin;
        layoutParams.rightMargin = rightMargin;
        textView.setLayoutParams(layoutParams);
        linearLayout.addView(textView);

        return textView;
    }

    private ArrayList<String> getIngredientList(RecipeResponse recipeResponse) {
        Recipe recipe = recipeResponse.getRecipe().get(0);
        ArrayList<String> ingredientList = new ArrayList<>();
        ingredientList.add(recipe.getMealIngredient1());
        ingredientList.add(recipe.getMealIngredient2());
        ingredientList.add(recipe.getMealIngredient3());
        ingredientList.add(recipe.getMealIngredient4());
        ingredientList.add(recipe.getMealIngredient5());
        ingredientList.add(recipe.getMealIngredient6());
        ingredientList.add(recipe.getMealIngredient7());
        ingredientList.add(recipe.getMealIngredient8());
        ingredientList.add(recipe.getMealIngredient9());
        ingredientList.add(recipe.getMealIngredient10());
        ingredientList.add(recipe.getMealIngredient11());
        ingredientList.add(recipe.getMealIngredient12());
        ingredientList.add(recipe.getMealIngredient13());
        ingredientList.add(recipe.getMealIngredient14());
        ingredientList.add(recipe.getMealIngredient15());
        ingredientList.add(recipe.getMealIngredient16());
        ingredientList.add(recipe.getMealIngredient17());
        ingredientList.add(recipe.getMealIngredient18());
        ingredientList.add(recipe.getMealIngredient19());
        ingredientList.add(recipe.getMealIngredient20());

        return ingredientList;
    }

    private List<String> getMeasureList(RecipeResponse recipeResponse) {
        Recipe recipe = recipeResponse.getRecipe().get(0);
        ArrayList<String> measureList = new ArrayList<>();
        measureList.add(recipe.getMealMeasure1());
        measureList.add(recipe.getMealMeasure2());
        measureList.add(recipe.getMealMeasure3());
        measureList.add(recipe.getMealMeasure4());
        measureList.add(recipe.getMealMeasure5());
        measureList.add(recipe.getMealMeasure6());
        measureList.add(recipe.getMealMeasure7());
        measureList.add(recipe.getMealMeasure8());
        measureList.add(recipe.getMealMeasure9());
        measureList.add(recipe.getMealMeasure10());
        measureList.add(recipe.getMealMeasure11());
        measureList.add(recipe.getMealMeasure12());
        measureList.add(recipe.getMealMeasure13());
        measureList.add(recipe.getMealMeasure14());
        measureList.add(recipe.getMealMeasure15());
        measureList.add(recipe.getMealMeasure16());
        measureList.add(recipe.getMealMeasure17());
        measureList.add(recipe.getMealMeasure18());
        measureList.add(recipe.getMealMeasure19());
        measureList.add(recipe.getMealMeasure20());

        return measureList;
    }

}












