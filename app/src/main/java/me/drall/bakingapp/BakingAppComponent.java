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

import javax.inject.Singleton;

import dagger.Component;
import me.drall.bakingapp.data.ApiModule;
import me.drall.bakingapp.data.ApiService;
import me.drall.bakingapp.data.storage.RecipeDbOpenHelper;
import me.drall.bakingapp.data.storage.StorageModule;
import me.drall.bakingapp.ui.HomeActivity;
import me.drall.bakingapp.ui.RecipeListFragment;

@Component(modules = {BakingAppModule.class, ApiModule.class, StorageModule.class})
@Singleton
public interface BakingAppComponent {
    ApiService apiService();
    RecipeDbOpenHelper dbHelper();
    void inject(HomeActivity activity);
    void inject(RecipeListFragment fragment);
}
