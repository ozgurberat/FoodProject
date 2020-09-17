package com.ozgurberat.foodproject.view;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ozgurberat.foodproject.R;
import com.ozgurberat.foodproject.adapter.CategoryRecyclerAdapter;
import com.ozgurberat.foodproject.model.Category;
import com.ozgurberat.foodproject.requests.responses.CategoryResponse;
import com.ozgurberat.foodproject.viewmodel.CategoryViewModel;

import java.util.ArrayList;

public class CategoryFragment extends Fragment implements CategoryRecyclerAdapter.CategoryViewListener {
    private static final String TAG = "CategoryFragment";

    private CategoryViewModel viewModel;
    private CategoryRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecycler(view);
        inflateToolbarMenu(view);


        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        viewModel.fetchCategories(); // fetch category list when the view is created
        subscribeObservers(); // observe live data


        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.category_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.fetchCategories();
                Toast.makeText(getContext(), "Fetched From API in CategoryFragment", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    private void subscribeObservers() {
        viewModel.getCategoryList().observe(getViewLifecycleOwner(), new Observer<CategoryResponse>() {
            @Override
            public void onChanged(CategoryResponse categoryResponse) {
                if (categoryResponse != null && categoryResponse.getCategories() != null && categoryResponse.getCategories().size() > 0) {
                    adapter.updateData(categoryResponse.getCategories());
                    showRecycler(true);
                }
                else {
                    Log.d(TAG, "onChanged: null value in CategoryFragment.");
                    showRecycler(false);
                }
            }
        });

    }

    private void showRecycler(boolean visibility) {
        if (visibility)
            recyclerView.setVisibility(View.VISIBLE);
        else
            recyclerView.setVisibility(View.GONE);
    }

    /**
     * Inflate toolbar and set SearchView functionality
     */
    private void inflateToolbarMenu(View view) {
        Toolbar toolbar = view.findViewById(R.id.category_toolbar);
        toolbar.inflateMenu(R.menu.search_menu);

        Menu menu = toolbar.getMenu();
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Filter by ingredient. (e.g. egg)");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("query", query);
                System.out.println(query);
                searchView.clearFocus();
                Navigation.findNavController(view).navigate(R.id.action_categoryFragment_to_foodFragment, bundle);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    /**
     * Initialize recycler view for the first time and set its adapter to a empty List
     * @param view View object for accessing the ID
     */
    private void initRecycler(View view) {
        recyclerView = view.findViewById(R.id.category_recycler);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_attribute));
        recyclerView.addItemDecoration(itemDecorator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CategoryRecyclerAdapter(view.getContext(), new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCategoryClicked(Category category) {
        Bundle bundle = new Bundle();
        bundle.putString("category", category.getCategoryName());
        Navigation.findNavController(getView()).navigate(R.id.action_categoryFragment_to_foodFragment, bundle);
        System.out.println("clicked : "+category.getCategoryName());
    }

}











