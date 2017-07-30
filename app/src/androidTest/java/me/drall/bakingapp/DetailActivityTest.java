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

package me.drall.bakingapp;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import me.drall.bakingapp.data.pojo.Recipe;
import me.drall.bakingapp.data.pojo.RecipeIngredients;
import me.drall.bakingapp.data.pojo.RecipeSteps;
import me.drall.bakingapp.ui.DetailActivity;
import me.drall.bakingapp.ui.HomeActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    Recipe recipe;

    @Before
    public void setup() {
        Intents.init();
    }

    private void prepareData() {
        List<RecipeIngredients> ingredientsList = new ArrayList<>();

        ingredientsList.add(
                RecipeIngredients.create(10,"g","ingredient 001")
        );
        ingredientsList.add(
                RecipeIngredients.create(200,"g","ingredient 002")
        );
        ingredientsList.add(
                RecipeIngredients.create(102,"g","ingredient 003")
        );
        ingredientsList.add(
                RecipeIngredients.create(101,"g","ingredient 004")
        );

        List<RecipeSteps> stepsList = new ArrayList<>();
        stepsList.add(
                RecipeSteps.create("http://techslides.com/demos/sample-videos/small.webm",
                        "description of step 1", 1, "short des 1","")
        );
        stepsList.add(
                RecipeSteps.create("http://techslides.com/demos/sample-videos/small.webm",
                        "description of step 2", 2, "short des 2","")
        );
        stepsList.add(
                RecipeSteps.create("http://techslides.com/demos/sample-videos/small.webm",
                        "description of step 3", 3, "short des 3","")
        );
        stepsList.add(
                RecipeSteps.create("http://techslides.com/demos/sample-videos/small.webm",
                        "description of step 4", 4, "short des 4","")
        );
        recipe = Recipe.create("",7,"Sample Recipe",1,ingredientsList,stepsList);
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Rule
    public ActivityTestRule<DetailActivity> mActivityRule =
            new ActivityTestRule<DetailActivity>(DetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    prepareData();
                    Intent intent = new Intent(InstrumentationRegistry.getContext(),
                            DetailActivity.class);
                    intent.putExtra(HomeActivity.RECIPE_DETAIL_BUNDLE_ARGS_KEY,recipe);
                    return intent;
                }
            };

    @Test
    public void onLoadDetailFragmentIsAddedTest() {
        onView(withId(R.id.recipe_detail_parent_scroll_view)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void IngredientsRecyclerViewShowAllIngredients() {
        onView(withId(R.id.recipe_detail_ingredients_list))
                .check(new RecyclerViewItemCountAssertion(recipe.ingredients().size()));
    }

    @Test
    public void StepsRecyclerViewShowAllSteps() {
        onView(withId(R.id.recipe_detail_steps_list))
                .check(new RecyclerViewItemCountAssertion(recipe.steps().size()));
    }


    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }
}
