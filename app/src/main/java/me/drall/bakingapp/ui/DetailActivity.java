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
import me.drall.bakingapp.R;
import me.drall.bakingapp.data.pojo.Recipe;
import me.drall.bakingapp.ui.exoplayer.PlayerActivity;

import static me.drall.bakingapp.ui.HomeActivity.RECIPE_DETAIL_BUNDLE_ARGS_KEY;

public class DetailActivity extends AppCompatActivity implements RecipeDetailFragment.Callback {

    private String RECIPE_DETAIL_FRAGMENT_TAG  = "RECIPE_DETAIL_FRAGMENT_TAG";
    @BindView(R.id.recipe_detail_frame) FrameLayout frameLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private Recipe recipe;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        recipe = getIntent().getParcelableExtra(RECIPE_DETAIL_BUNDLE_ARGS_KEY);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(recipe.name());
        }

        if(savedInstanceState==null) {
            RecipeDetailFragment detailFragment = new RecipeDetailFragment();
            Bundle args = new Bundle();
            args.putParcelable(RECIPE_DETAIL_BUNDLE_ARGS_KEY, recipe);
            detailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_detail_frame, detailFragment, RECIPE_DETAIL_FRAGMENT_TAG)
                .commit();
        }
    }

    @Override public void onVideoPlayClicked(String url) {
        Intent intent = new Intent(this,PlayerActivity.class);
        intent.putExtra(PlayerActivity.RECIPE_URL_KEY,url);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
