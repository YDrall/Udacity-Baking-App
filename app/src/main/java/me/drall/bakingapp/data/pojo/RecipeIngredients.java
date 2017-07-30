package me.drall.bakingapp.data.pojo;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class RecipeIngredients implements Parcelable {

	@SerializedName("quantity")
	public abstract double quantity();

	@SerializedName("measure")
	public abstract String measure();

	@SerializedName("ingredient")
	public abstract String ingredient();

	public static TypeAdapter<RecipeIngredients> typeAdapter(Gson gson) {
		return new AutoValue_RecipeIngredients.GsonTypeAdapter(gson);
	}

	public static RecipeIngredients create(double quantity, String measure, String ingredient) {
		return new AutoValue_RecipeIngredients(quantity,measure,ingredient);
	}
}