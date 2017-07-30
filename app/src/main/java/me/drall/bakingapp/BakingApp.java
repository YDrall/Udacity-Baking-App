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

import android.app.Application;

import com.facebook.stetho.Stetho;

import me.drall.bakingapp.data.ApiModule;
import me.drall.bakingapp.data.storage.StorageModule;
import timber.log.Timber;

public class BakingApp extends Application {

    private static BakingAppComponent appComponent;
    private static Application application;

    @Override public void onCreate() {
        super.onCreate();
        application = this;
        initializeAppComponent();
        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Stetho.initializeWithDefaults(this);
    }

    private static void initializeAppComponent() {
        appComponent = DaggerBakingAppComponent.builder()
                .bakingAppModule(new BakingAppModule(application))
                .apiModule(new ApiModule("http://go.udacity.com/"))
                .storageModule(new StorageModule())
                .build();
    }

    public static BakingAppComponent getComponent() {
        if(appComponent== null)
            initializeAppComponent();
        return appComponent;
    }
}
