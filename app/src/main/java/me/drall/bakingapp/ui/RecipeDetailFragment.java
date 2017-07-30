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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.drall.bakingapp.R;
import me.drall.bakingapp.data.pojo.Recipe;
import me.drall.bakingapp.ui.adapters.IngredientsAdapter;
import me.drall.bakingapp.ui.adapters.RecipeStepsAdapter;
import me.drall.bakingapp.ui.listener.RecyclerItemClickListener;

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.recipe_detail_ingredients_list)
    RecyclerView ingredientsRecyclerView;

    @BindView(R.id.recipe_detail_steps_list)
    RecyclerView stepsRecyclerView;

    private Recipe recipe;
    private Callback callback;

    public RecipeDetailFragment() {
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Callback) {
            this.callback = (Callback) context;
        }
        else {
            throw new IllegalStateException("Activity should implements callbacks");
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recepie_detail,container,false);
        ButterKnife.bind(this,view);
        recipe = getArguments().getParcelable(HomeActivity.RECIPE_DETAIL_BUNDLE_ARGS_KEY);

        showIngredients();
        showSteps();
        setStepRecyclerOnItemClickListener();

        return view;
    }

    private void setStepRecyclerOnItemClickListener() {
        stepsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.recipe_step_video:
                    case R.id.recipe_step_video_play_icon:
                    case R.id.recipe_step_video_overlay:
                    case R.id.recipe_step_image:
                        callback.onVideoPlayClicked(recipe.steps().get(position).videoURL());
                        break;
                    default:
                        break;
                }
            }
        }));
    }


    private void showSteps() {
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stepsRecyclerView.setAdapter(new RecipeStepsAdapter(recipe.steps(),this));
    }

    private void showIngredients() {
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientsRecyclerView.setAdapter(new IngredientsAdapter(recipe.ingredients()));
    }

    public interface Callback {
        void onVideoPlayClicked(String url);
    }
}
