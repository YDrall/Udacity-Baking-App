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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drall.bakingapp.BakingApp;
import me.drall.bakingapp.R;
import me.drall.bakingapp.data.pojo.Recipe;
import me.drall.bakingapp.ui.exoplayer.PlayerActivity;

public class HomeActivity extends AppCompatActivity implements RecipeListFragment.Callback, RecipeDetailFragment.Callback {

    public static String RECIPE_DETAIL_BUNDLE_ARGS_KEY ="RECIPE_DETAIL_BUNDLE_ARGS_KEY";
    public static String RECIPE_VIDEO_URL_KEY ="RECIPE_VIDEO_URL_KEY";
    private String RECIPE_LIST_FRAGMENT_TAG = "RECIPE_LIST_FRAGMENT_TAG";
    private String RECIPE_DETAIL_FRAGMENT_TAG  = "RECIPE_DETAIL_FRAGMENT_TAG";
    private String RECIPE_LIST_BACK_STACK_NAME = "RECIPE_LIST_BACK_STACK_NAME";
    private String RECIPE_DETAIL_BACK_STACK_NAME = "RECIPE_DETAIL_BACK_STACK_NAME";
    private String EXO_PLAYER_TAG = "EXO_PLAYER_TAG";
    private String TITLE_STACK_KEY = "TITLE_STACK_KEY";

    @BindView(R.id.home_recipe_list_frame) FrameLayout recipeListFrame;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private boolean isTowPane;

    private RecipeDetailFragment detailFragment;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        BakingApp.getComponent().inject(this);

        isTowPane = getResources().getBoolean(R.bool.isTwoPane);

        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            String currentTitle = "Recipes";
            getSupportActionBar().setTitle(currentTitle);
        }

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.home_recipe_list_frame, new RecipeListFragment(),RECIPE_LIST_FRAGMENT_TAG)
                .commit();
        }
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override protected void onResume() {
        super.onResume();

    }



    @Override public void onBackPressed() {
        super.onBackPressed();
    }

    @Override public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override public void onRecipeSelected(Recipe recipe) {
        if(isTowPane) {
            detailFragment = new RecipeDetailFragment();
            Bundle args = new Bundle();
            args.putParcelable(RECIPE_DETAIL_BUNDLE_ARGS_KEY, recipe);
            detailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_recipe_detail_frame, detailFragment, RECIPE_DETAIL_FRAGMENT_TAG)
                .commit();
        }
        else {
            Intent intent = new Intent(this,DetailActivity.class);
            intent.putExtra(RECIPE_DETAIL_BUNDLE_ARGS_KEY,recipe);
            startActivity(intent);
        }
    }

    @Override
    public void onRecipeListChanged() {
        if(isTowPane && detailFragment!=null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(detailFragment)
                    .commit();
        }
    }

    @Override public void onVideoPlayClicked(String url) {
        Intent intent = new Intent(this,PlayerActivity.class);
        intent.putExtra(PlayerActivity.RECIPE_URL_KEY,url);
        startActivity(intent);
    }
}
