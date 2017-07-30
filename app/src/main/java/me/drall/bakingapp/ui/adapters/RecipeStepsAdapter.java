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

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import java.util.List;
import me.drall.bakingapp.GlideApp;
import me.drall.bakingapp.GlideRequests;
import me.drall.bakingapp.R;
import me.drall.bakingapp.data.pojo.RecipeSteps;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    private List<RecipeSteps> recipeStepsList;
    private Fragment parentFragment;
    private Activity parentActivity;

    public RecipeStepsAdapter(List<RecipeSteps> recipeStepsList, Fragment fragment) {
        this.recipeStepsList =recipeStepsList;
        this.parentFragment = fragment;
    }

    public RecipeStepsAdapter(List<RecipeSteps> recipeStepsList, Activity activity) {
        this.recipeStepsList =recipeStepsList;
        this.parentActivity = activity;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recepie_step,parent,false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {

        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        TextDrawable drawable = TextDrawable.builder()
            .buildRound(Integer.toString(position+1), Color.parseColor("#616161"));
        holder.stepCountImage.setImageDrawable(drawable);
        // remove step counting from description.
        String des = recipeStepsList.get(position).description().replaceFirst("^\\d+(\\.\\d)?\\.","");
        holder.recipeDescriptionText.setText(des);
        if(recipeStepsList.get(position).thumbnailURL()==null ||
            "".equals(recipeStepsList.get(position).thumbnailURL())) {
            holder.recipeImage.setVisibility(View.GONE);
        }
        else {
            GlideRequests request;
            if(parentActivity!=null)
                request = GlideApp.with(parentActivity);
            else
                request = GlideApp.with(parentFragment);
            request.load(recipeStepsList.get(position).thumbnailURL())
            .centerCrop()
            .into(holder.recipeImage);
        }

        if(recipeStepsList.get(position).videoURL()==null ||
            "".equals(recipeStepsList.get(position).videoURL())) {
            holder.playerView.setVisibility(View.GONE);
        }

    }

    @Override public int getItemCount() {
        return recipeStepsList ==null? 0: recipeStepsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.step_count_image) ImageView stepCountImage;
        @BindView(R.id.recipe_description_text) TextView recipeDescriptionText;
        @BindView(R.id.recipe_step_image) ImageView recipeImage;
        @BindView(R.id.recipe_step_video) CardView playerView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
