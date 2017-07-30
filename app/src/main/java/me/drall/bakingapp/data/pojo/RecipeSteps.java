package me.drall.bakingapp.data.pojo;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class RecipeSteps implements Parcelable {

	@SerializedName("videoURL")
	public abstract String videoURL();

	@SerializedName("description")
	public abstract String description();

	@SerializedName("id")
	public abstract int id();

	@SerializedName("shortDescription")
	public abstract String shortDescription();

	@SerializedName("thumbnailURL")
	public abstract String thumbnailURL();

	public static TypeAdapter<RecipeSteps> typeAdapter(Gson gson) {
		return new AutoValue_RecipeSteps.GsonTypeAdapter(gson);
	}

	public static RecipeSteps create(String videoUrl, String description, int id, String shortDescription, String thumbnailUrl) {
		return new AutoValue_RecipeSteps(videoUrl,description,id,shortDescription,thumbnailUrl);
	}
}