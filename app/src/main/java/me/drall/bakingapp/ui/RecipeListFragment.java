/*
 *  Copyright (C) 2017 Yogesh Drall
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package me.drall.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.drall.bakingapp.BakingApp;
import me.drall.bakingapp.R;
import me.drall.bakingapp.data.ApiService;
import me.drall.bakingapp.data.pojo.Recipe;
import me.drall.bakingapp.data.storage.RecipeDbOpenHelper;
import me.drall.bakingapp.ui.adapters.RecipeListAdapter;
import me.drall.bakingapp.ui.listener.RecyclerItemClickListener;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment {

    private final String RECIPE_LIST_DATA_STATE_KEY = "RECIPE_LIST_KEY";
    private final String RECIPE_LIST_LAYOUT_STATE_KEY = "RECIPE_LIST_LAYOUT_STATE_KEY";
    private final String RECIPE_LIST_SELECTED_ITEM_INDEX_KEY ="RECIPE_LIST_SELECTED_ITEM_INDEX_KEY";

    @Inject ApiService apiService;
    @Inject
    RecipeDbOpenHelper dbOpenHelper;

    @BindView(R.id.recipe_list_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recipe_list_recycler_view) RecyclerView recipeRecyclerView;

    private List<Recipe> recipeList;
    private Callback callback;
    private RecipeListAdapter recipeListAdapter;
    private CompositeDisposable subscription= new CompositeDisposable();
    private int currentlySelectedPosition = -1;


    public RecipeListFragment() {
        // Required empty public constructor
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof RecipeListFragment.Callback) {
            callback = (Callback) getActivity();
        }
        else {
            throw new IllegalStateException("Activity must implement callback function");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recipes_list,container,false);

        ButterKnife.bind(this,view);
        BakingApp.getComponent().inject(this);

        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeListAdapter = new RecipeListAdapter(null);
        recipeRecyclerView.setAdapter(recipeListAdapter);

        if(savedInstanceState==null) {
            fetchRecipes(true);
        }
        else {
            recipeList = savedInstanceState.getParcelableArrayList(RECIPE_LIST_DATA_STATE_KEY);
            currentlySelectedPosition = savedInstanceState.getInt(RECIPE_LIST_SELECTED_ITEM_INDEX_KEY);
            if(recipeList!=null) {
                onFetchRecipe(recipeList);
                recipeRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(RECIPE_LIST_LAYOUT_STATE_KEY)
                );

            }
            else {
                fetchRecipes(true);
            }
        }


        setRecipeRecyclerOnItemClickListener();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchRecipes(true);
            }
        });
        return view;
    }

    @Override public void onPause() {
        if(!subscription.isDisposed())
            subscription.dispose();
        super.onPause();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    private void setRecipeRecyclerOnItemClickListener() {
        recipeRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                setRecipeItemHighlighted(position);
                callback.onRecipeSelected(recipeList.get(position));
                currentlySelectedPosition=position;
            }
        }));
    }

    private void setRecipeItemHighlighted(int position) {
        if(getResources().getBoolean(R.bool.isTwoPane)) {
            if (currentlySelectedPosition >= 0 && currentlySelectedPosition != position) {
                recipeRecyclerView.getLayoutManager()
                        .getChildAt(currentlySelectedPosition)
                        .findViewById(R.id.recipe_item_background)
                        .setBackgroundResource(R.drawable.card_bg);
            }
            recipeRecyclerView.getLayoutManager()
                    .getChildAt(position)
                    .findViewById(R.id.recipe_item_background)
                    .setBackgroundResource(R.drawable.card_bg_selected);
        }

    }

    private void fetchRecipes(boolean showLoading) {
        if(showLoading)
            swipeRefreshLayout.setRefreshing(true);
        subscription.add(apiService.getRecipes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<Recipe>>() {
                @Override public void accept(@NonNull List<Recipe> recipes) throws Exception {
                    Timber.d(""+recipes.size());
                    onFetchRecipe(recipes);
                }
            }, new Consumer<Throwable>() {
                @Override public void accept(@NonNull Throwable throwable) throws Exception {
                    onFetchRecipeError(throwable);
                }
            }));
    }

    private void onFetchRecipeError(Throwable throwable) {
        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        Timber.d(throwable.getMessage());
    }

    private void onFetchRecipe(List<Recipe> recipes) {
        Timber.d(recipes.toString());

        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

        recipeList = recipes;

        updateRecipeList(recipes);
        callback.onRecipeListChanged();

        clearAndUpdateDatabase(recipes);
    }

    private void clearAndUpdateDatabase(final List<Recipe> recipe) {
        Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> e) throws Exception {
                dbOpenHelper.clearAllTables();
                e.onComplete();
            }
        })
            .subscribeOn(Schedulers.io())
            .subscribe(new Observer<Void>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull Void aVoid) {

                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Timber.d(e);
                }

                @Override
                public void onComplete() {
                    Timber.d("deletion of table completed..");
                    updateDatabase(recipe);
                }
            });

    }

    private void updateDatabase(List<Recipe> recipes) {
        Observable.fromIterable(recipes)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Recipe>() {
                    @Override
                    public void accept(@NonNull Recipe recipe) throws Exception {
                        dbOpenHelper.addRecipe(recipe);
                        dbOpenHelper.addRecipeIngredients(recipe);
                        dbOpenHelper.addSteps(recipe);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Timber.d(throwable);
                    }
                });
    }

    private void updateRecipeList(List<Recipe> recipes) {
        recipeListAdapter.swapRecipeList(recipes);
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST_DATA_STATE_KEY,
            (ArrayList<? extends Parcelable>) recipeList);
        outState.putParcelable(RECIPE_LIST_LAYOUT_STATE_KEY,
            recipeRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putInt(RECIPE_LIST_SELECTED_ITEM_INDEX_KEY,currentlySelectedPosition);
    }

    public interface Callback {
        void onRecipeSelected(Recipe recipe);
        void onRecipeListChanged();
    }

}
