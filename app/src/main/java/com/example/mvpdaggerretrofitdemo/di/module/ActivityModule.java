package com.example.mvpdaggerretrofitdemo.di.module;

import android.app.Activity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mapsay on 17/3/16.
 */

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @Singleton
    Activity provideActivity() {
        return mActivity;
    }
}
