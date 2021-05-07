package com.example.juba.chatmessenger.di.di_main;


import com.example.juba.chatmessenger.di.ViewModelKey;
import com.example.juba.chatmessenger.ui.main.users.UsersFragmentViewModel;
import androidx.lifecycle.ViewModel;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class MainViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(UsersFragmentViewModel.class)
    ViewModel bindUserViewModel() {
        return new UsersFragmentViewModel();

    }



}

