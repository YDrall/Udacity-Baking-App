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

package me.drall.bakingapp.ui.adapters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.amulyakhare.textdrawable.TextDrawable;
import java.util.List;
import java.util.Locale;
import me.drall.bakingapp.R;
import me.drall.bakingapp.data.pojo.Recipe;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private List<Recipe> recipes;

    public RecipeListAdapter(@Nullable List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_list,parent,false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {

        holder.recipeName.setText(recipes.get(position).name());

        holder.recipeIngredients.setText(String.format(Locale.getDefault(), "Ingredients: %d",
            recipes.get(position).ingredients().size()));

        TextDrawable drawable = TextDrawable.builder()
            .buildRect(recipes.get(position).name().substring(0,1), Color.parseColor("#616161"));
        holder.recipeImage.setImageDrawable(drawable);

        holder.recipeSteps.setText(String.format(
            Locale.getDefault(),"Steps: %d",
            recipes.get(position).steps().size()
        ));
    }

    @Override public int getItemCount() {
        return recipes==null? 0:recipes.size();
    }

    public void swapRecipeList(List<Recipe> recipeList) {
        this.recipes = recipeList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_item_name) TextView recipeName;
        @BindView(R.id.recipe_item_text_image) ImageView recipeImage;
        @BindView(R.id.recipe_item_text_ingredients) TextView recipeIngredients;
        @BindView(R.id.recipe_item_text_steps) TextView recipeSteps;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
