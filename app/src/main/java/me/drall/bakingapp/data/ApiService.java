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

package me.drall.bakingapp.data;

import io.reactivex.Observable;
import java.util.List;
import me.drall.bakingapp.data.pojo.Recipe;
import retrofit2.http.GET;

public interface ApiService {
    @GET("android-baking-app-json") Observable<List<Recipe>> getRecipes();
}
