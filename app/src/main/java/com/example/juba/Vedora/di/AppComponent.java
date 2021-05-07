package com.example.juba.chatmessenger.di;

import android.app.Application;

import com.example.juba.chatmessenger.BaseActivity;
import com.example.juba.chatmessenger.di.di_main.users.UsersModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityModuleBuilder.class,
                AppModule.class,
                UsersModule.class,
                ViewModelModule.class,
        })
public interface AppComponent extends AndroidInjector<BaseActivity> {

    @Component.Builder
    interface Builder
    {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();

    }
}

