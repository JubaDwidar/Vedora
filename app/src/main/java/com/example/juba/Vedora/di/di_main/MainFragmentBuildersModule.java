package com.example.juba.chatmessenger.di.di_main;

import com.example.juba.chatmessenger.di.di_main.users.UsersModule;
import com.example.juba.chatmessenger.ui.main.users.UsersFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule
{
    @ContributesAndroidInjector(modules = {
            UsersModule.class
    })
    abstract UsersFragment contributeUsersFragment();



}



