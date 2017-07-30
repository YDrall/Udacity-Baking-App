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

package me.drall.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.List;
import java.util.Locale;

import me.drall.bakingapp.R;
import me.drall.bakingapp.data.pojo.Recipe;
import me.drall.bakingapp.data.pojo.RecipeIngredients;
import me.drall.bakingapp.data.pojo.RecipeSteps;
import me.drall.bakingapp.data.storage.RecipeDbOpenHelper;
import me.drall.bakingapp.ui.DetailActivity;
import me.drall.bakingapp.ui.HomeActivity;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsListWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_list_widget);
        RecipeDbOpenHelper dbOpenHelper = new RecipeDbOpenHelper(context);
        List<Recipe> recipes = dbOpenHelper.getAllRecipes();
        if(recipes!=null && recipes.size()>0) {
            views.setTextViewText(R.id.appwidget_text, recipes.get(0).name());

            StringBuilder builder = new StringBuilder();
            List<RecipeIngredients> ingredientsList;

            int index=0;
            ingredientsList = dbOpenHelper.getIngredients(recipes.get(index).id());
            List<RecipeSteps> stepsList = dbOpenHelper.getSteps(recipes.get(index).id());
            Intent intent = new Intent(context, DetailActivity.class);
            Recipe recipe = Recipe.create(recipes.get(index).name(),
                    recipes.get(index).servings(),
                    recipes.get(index).name(),
                    recipes.get(index).id(),
                    ingredientsList,
                    stepsList);
            intent.putExtra(HomeActivity.RECIPE_DETAIL_BUNDLE_ARGS_KEY,recipe);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);


            for (RecipeIngredients ingredients : ingredientsList) {
                builder.append(String.format(Locale.getDefault(),
                        "-> %s%s %s",
                        ingredients.quantity(),
                        ingredients.measure().toLowerCase(),
                        ingredients.ingredient()
                        ));
                builder.append("\n");
            }

            views.setTextViewText(R.id.appwidget_text_ingredients,builder.toString());
                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        else {
            views.setTextViewText(R.id.appwidget_text_ingredients,"No records in Database.");
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

