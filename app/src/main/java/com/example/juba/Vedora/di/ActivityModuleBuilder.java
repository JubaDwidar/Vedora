package com.example.juba.chatmessenger.di;

import com.example.juba.chatmessenger.di.di_main.MainFragmentBuildersModule;
import com.example.juba.chatmessenger.di.di_main.MainViewModelModule;
import com.example.juba.chatmessenger.di.di_main.users.MessageModule;
import com.example.juba.chatmessenger.notification.MyFireBaseService;
import com.example.juba.chatmessenger.ui.login.LoginActivity;
import com.example.juba.chatmessenger.ui.main.MainActivity;
import com.example.juba.chatmessenger.ui.message.MessageActivity;
import com.example.juba.chatmessenger.ui.register.RegisterActivity;
import com.example.juba.chatmessenger.ui.settings.SettingsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityModuleBuilder {

    @ContributesAndroidInjector()
    abstract RegisterActivity contuributeRegActivity();

    @ContributesAndroidInjector()
    abstract LoginActivity contLoginActivity();


    @ContributesAndroidInjector()
    abstract SettingsActivity conSettActivity();

    @ContributesAndroidInjector()
    abstract MyFireBaseService conMyServiceActivity();


    @ContributesAndroidInjector(modules = {
            MainFragmentBuildersModule.class,
            MainViewModelModule.class,
    })
    abstract MainActivity contributeMainActivity();


    @ContributesAndroidInjector(modules = {
            MessageModule.class,

    })
    abstract MessageActivity contributeMessageActivity();

}

