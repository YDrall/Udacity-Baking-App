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

package me.drall.bakingapp.data.storage;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import me.drall.bakingapp.data.pojo.Recipe;
import me.drall.bakingapp.data.pojo.RecipeIngredients;
import me.drall.bakingapp.data.pojo.RecipeSteps;

public class RecipeDbOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RECIPE.DB";

    public RecipeDbOpenHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    // hardcoded queries to create tables
    private final String CRATE_TABLE_RECIPE_QUERY = "create table " + RecipeContract.TABLE_RECIPE_NAME + " ( "
            + "id integer primary key on conflict replace, image text, servings integer, name text );";
    private final String CREATE_TABLE_INGREDIENTS_QUERY = "create table " + RecipeContract.TABLE_RECIPE_INGREDIENTS +" ( "
            + " quantity integer, measure text, ingredient text, id integer, " +
            "foreign key (id) references "+ RecipeContract.TABLE_RECIPE_NAME + "(id));";
    private final String CREATE_TABLE_STEPS_QUERY = "create table " + RecipeContract.TABLE_RECIPE_STEPS + " ( "
            + " videoUrl text, description text, id integer, shortDes text, thumbnail text, key int, " +
            "foreign key (id) references "+ RecipeContract.TABLE_RECIPE_NAME + "(id));";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CRATE_TABLE_RECIPE_QUERY);
        db.execSQL(CREATE_TABLE_INGREDIENTS_QUERY);
        db.execSQL(CREATE_TABLE_STEPS_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("select * from "+RecipeContract.TABLE_RECIPE_NAME,null);
        if(cursor!=null) {
            cursor.moveToFirst();
            do {
                String image =cursor.getString(cursor.getColumnIndex("image"));
                int serving = cursor.getInt(cursor.getColumnIndex("servings"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));

                recipes.add(Recipe.create(image,serving,name,id));
            }while (cursor.moveToNext());
            cursor.close();
        }

        return recipes;
    }

    public void addRecipe(Recipe recipe) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("image",recipe.image());
        contentValues.put("servings",recipe.servings());
        contentValues.put("name",recipe.name());
        contentValues.put("id",recipe.id());
        getWritableDatabase()
                .insert(RecipeContract.TABLE_RECIPE_NAME,null,contentValues);
    }

    public List<RecipeIngredients> getIngredients(int recipeId) {
        String[] selectionArgs = {Integer.toString(recipeId)};
        Cursor cursor = getReadableDatabase()
                .query(RecipeContract.TABLE_RECIPE_INGREDIENTS,
                        null," id=?",selectionArgs,null,null,null);
        if(cursor!=null) {
            List<RecipeIngredients> ingredientsList = new ArrayList<>();
            cursor.moveToFirst();
            do {
                RecipeIngredients ingredients = RecipeIngredients.create(
                        cursor.getDouble(cursor.getColumnIndex("quantity")),
                        cursor.getString(cursor.getColumnIndex("measure")),
                        cursor.getString(cursor.getColumnIndex("ingredient"))
                );
                ingredientsList.add(ingredients);
            }while (cursor.moveToNext());

            cursor.close();
            return ingredientsList;
        }
        throw new IllegalArgumentException("not data for this id");
    }
    public void addRecipeIngredients(Recipe recipe) {
        List<RecipeIngredients> ingredients = recipe.ingredients();
        for(RecipeIngredients ingredient: ingredients) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("quantity", ingredient.quantity());
            contentValues.put("measure", ingredient.measure());
            contentValues.put("ingredient", ingredient.ingredient());
            contentValues.put("id",recipe.id());

            getWritableDatabase()
                    .insert(RecipeContract.TABLE_RECIPE_INGREDIENTS, null, contentValues);
        }
    }

    public void clearAllTables() {
        if(isTableExists(RecipeContract.TABLE_RECIPE_INGREDIENTS))
            getWritableDatabase().delete(RecipeContract.TABLE_RECIPE_INGREDIENTS,null,null);
        if(isTableExists(RecipeContract.TABLE_RECIPE_NAME))
            getWritableDatabase().delete(RecipeContract.TABLE_RECIPE_NAME,null,null);
        if(isTableExists(RecipeContract.TABLE_RECIPE_STEPS))
            getWritableDatabase().delete(RecipeContract.TABLE_RECIPE_STEPS,null,null);
    }

    public void addSteps(Recipe recipe) {
        List<RecipeSteps> stepsList = recipe.steps();
        for(RecipeSteps step: stepsList) {
            ContentValues  contentValues = new ContentValues();
            contentValues.put("videoUrl", step.videoURL());
            contentValues.put("description",step.description());
            contentValues.put("id", step.id());
            contentValues.put("shortDes",step.shortDescription());
            contentValues.put("thumbnail",step.thumbnailURL());
            contentValues.put("key",recipe.id());
            getWritableDatabase()
                    .insert(RecipeContract.TABLE_RECIPE_STEPS,null,contentValues);
        }
    }

    public List<RecipeSteps> getSteps(int recipeId) {
        String[] selectionArgs = {Integer.toString(recipeId)};
        Cursor cursor = getReadableDatabase()
                .query(RecipeContract.TABLE_RECIPE_STEPS,
                        null," key=?",selectionArgs,null,null,"id");
        if(cursor!=null) {
            List<RecipeSteps> stepsList = new ArrayList<>();
            cursor.moveToFirst();
            do {
                RecipeSteps step = RecipeSteps.create(
                        cursor.getString(cursor.getColumnIndex("videoUrl")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("shortDes")),
                        cursor.getString(cursor.getColumnIndex("thumbnail"))
                );
                stepsList.add(step);
            }while (cursor.moveToNext());

            cursor.close();
            return stepsList;
        }
        throw new IllegalArgumentException("not data for this id");
    }

    public boolean isTableExists(String tableName) {
        Cursor cursor = getReadableDatabase()
                .rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
}
