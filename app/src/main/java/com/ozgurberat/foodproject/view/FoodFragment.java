package com.ozgurberat.foodproject.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.ozgurberat.foodproject.R;
import com.ozgurberat.foodproject.adapter.FoodRecyclerAdapter;
import com.ozgurberat.foodproject.model.Food;
import com.ozgurberat.foodproject.model.Recipe;
import com.ozgurberat.foodproject.requests.responses.FoodResponse;
import com.ozgurberat.foodproject.viewmodel.FoodViewModel;
import com.ozgurberat.foodproject.viewmodel.SavedRecipeViewModel;

import java.util.ArrayList;
import java.util.List;

public class FoodFragment extends Fragment implements FoodRecyclerAdapter.FoodViewListener {
    private static final String TAG = "FoodFragment";

    private FoodViewModel foodViewModel;

    private RecyclerView recyclerView;
    private FoodRecyclerAdapter adapter;
    private TextView nullResponseText;
    private ImageView nullResponseImage;

    private String category;
    private String query;

    private String foodIdForRetry;
    private boolean forAlertDialogAndSnackbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        forAlertDialogAndSnackbar = false;

        initRecycler(view);
        inflateToolbarMenu(view);
        initViews(view);

        if (getArguments() != null) {
            //  Deciding which request we need to send in the background. If the bundle is sent for 'category',
            //only the category request will happen. Same thing happens with 'query'.
            category = getArguments().getString("category");
            if (category != null && !category.equals("")) {
                foodViewModel.fetchFoods(category);
//                Toast.makeText(getContext(), "Fetched From API in FoodFragment", Toast.LENGTH_SHORT).show();
            }

            query = getArguments().getString("query"); //
            if (query != null && !query.equals("")) {
                foodViewModel.fetchQueriedFoods(query);
//                Toast.makeText(getContext(), "Fetched From API in FoodFragment", Toast.LENGTH_SHORT).show();
            }

        }

        subscribeObservers(); //observe live data

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.food_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getArguments() != null) {
                    category = getArguments().getString("category");
                    if (query != null && !query.equals("")) {
                        foodViewModel.fetchQueriedFoods(query);
                    }
                    else
                        foodViewModel.fetchFoods(category);
                }
//                Toast.makeText(getContext(), "Fetched From API in FoodFragment", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void initViews(View view) {
        nullResponseText = view.findViewById(R.id.error_message_text);
        nullResponseImage = view.findViewById(R.id.error_image);
    }

    private void subscribeObservers() {
        foodViewModel.getFoodList().observe(getViewLifecycleOwner(), new Observer<FoodResponse>() {
            @Override
            public void onChanged(FoodResponse foodResponse) {
                if (foodResponse != null && foodResponse.getFoods() != null) {
                    if (foodResponse.getFoods().size() > 0) {
                        adapter.updateData(foodResponse.getFoods());
                        showRecyclerView();
                    }
                    if (foodResponse.getFoods().isEmpty()) { // That means response from api is an error or the request is timed out.
                        hideRecycler(); // Meanwhile, main activity shows generic timed out message.
                    }
                }
                else {
                    showNullOrEmptyMessage(); // If the response is null or empty, live data sets its value to null.
                }
            }
        });

        foodViewModel.getQueriedFoods().observe(getViewLifecycleOwner(), new Observer<FoodResponse>() {
            @Override
            public void onChanged(FoodResponse foodResponse) {
                if (foodResponse != null && foodResponse.getFoods() != null) {
                    if (foodResponse.getFoods().size() > 0) {
                        adapter.updateData(foodResponse.getFoods());
                        showRecyclerView();
                    }
                    if (foodResponse.getFoods().isEmpty()) {
                        hideRecycler();
                    }
                }
                else {
                    showNullOrEmptyMessage();
                }
            }
        });

        foodViewModel.getRecipeFromSQLite().observe(getViewLifecycleOwner(), new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if (recipe != null) {
                    if (forAlertDialogAndSnackbar) {
                        Snackbar snackbar = Snackbar.make(getView(),"SUCCESS!\nSaved: "+recipe.getMealName(), BaseTransientBottomBar.LENGTH_LONG);
                        snackbar.setTextColor(Color.CYAN);
                        snackbar.show();

                    }

                }
                else {
                    if (forAlertDialogAndSnackbar) {
                        showAlertDialog();
                    }
                }
            }
        });

    }

    private AlertDialog createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Slow Connection");
        builder.setMessage("Recipe could not save.Do you wanna try again?");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                foodViewModel.fetchRecipeForResult(foodIdForRetry);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    private void showAlertDialog() {
        AlertDialog alertDialog = createAlertDialog();
        alertDialog.show();
        Button buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonPositive.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.RED));
        Button buttonNegative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonNegative.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.RED));
    }

    private void hideRecycler() {
        recyclerView.setVisibility(View.GONE);
        nullResponseText.setVisibility(View.GONE);
        nullResponseImage.setVisibility(View.GONE);
    }

    private void showRecyclerView() {
        nullResponseText.setVisibility(View.GONE);
        nullResponseImage.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showNullOrEmptyMessage() {
        nullResponseText.setVisibility(View.VISIBLE);
        nullResponseImage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }


    /**
     * Inflate toolbar and set SearchView functionality
     */
    private void inflateToolbarMenu(View view) {
        Toolbar toolbar = view.findViewById(R.id.food_toolbar);
        toolbar.inflateMenu(R.menu.search_menu);

        Menu menu = toolbar.getMenu();
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Filter by ingredient. (e.g. egg)");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {
                foodViewModel.fetchQueriedFoods(queryText);
                query = queryText;
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem saveItem = menu.findItem(R.id.action_save);
        saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getActivity(), SavedRecipeActivity.class);
                startActivity(intent);
                return true;
            }
        });

    }

    /**
     * Same functionality with CategoryFragment
     * @param view
     */
    private void initRecycler(View view) {
        recyclerView = view.findViewById(R.id.food_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_attribute));
        recyclerView.addItemDecoration(itemDecorator);
        adapter = new FoodRecyclerAdapter(view.getContext(), new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFoodClicked(Food food) {
        Intent intent = new Intent(getContext(), RecipeActivity.class);
        intent.putExtra("intentPath", "intentFromMainActivity");
        intent.putExtra("id", food.getMealId());
        startActivity(intent);
    }

    @Override
    public void onSaveClicked(Food food) {
        foodViewModel.fetchRecipeForResult(food.getMealId());
        foodIdForRetry = food.getMealId();
        forAlertDialogAndSnackbar = true;

    }

}









