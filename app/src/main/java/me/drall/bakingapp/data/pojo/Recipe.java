package me.drall.bakingapp.data.pojo;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class Recipe implements Parcelable {

	@SerializedName("image")
	public abstract String image();

	@SerializedName("servings")
	public abstract int servings();

	@SerializedName("name")
	public abstract String name();

	@SerializedName("ingredients")
	public abstract List<RecipeIngredients> ingredients();

	@SerializedName("id")
	public abstract int id();

	@SerializedName("steps")
	public abstract List<RecipeSteps> steps();


	public static TypeAdapter<Recipe> typeAdapter(Gson gson) {
		return new AutoValue_Recipe.GsonTypeAdapter(gson);
	}


	public static Recipe create(String image, int serving, String name, int key) {
		return new AutoValue_Recipe(image,serving,name,
				new ArrayList<RecipeIngredients>(),key,
				new ArrayList<RecipeSteps>());
	}

	public static Recipe create(String image, int serving, String name, int key,
								List<RecipeIngredients> ingredientsList, List<RecipeSteps> steps) {
		return new AutoValue_Recipe(image,serving,name,
				ingredientsList,key,
				steps);
	}
}