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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import java.util.Locale;
import me.drall.bakingapp.R;
import me.drall.bakingapp.data.pojo.RecipeIngredients;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    List<RecipeIngredients> ingredientsList;

    public IngredientsAdapter(List<RecipeIngredients> recipeIngredientsList) {
        this.ingredientsList = recipeIngredientsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredients_list,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.descriptionText.setText(
            String.format(Locale.getDefault(),
                "%d) %s%s %s",
                position+1,
                ingredientsList.get(position).quantity(),
                ingredientsList.get(position).measure().toLowerCase(),
                ingredientsList.get(position).ingredient())
        );
    }

    @Override public int getItemCount() {
        return ingredientsList==null? 0:ingredientsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_description)
        TextView descriptionText;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
