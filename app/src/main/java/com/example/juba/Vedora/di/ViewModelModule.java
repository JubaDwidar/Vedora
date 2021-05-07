package com.example.juba.chatmessenger.di;

import com.example.juba.chatmessenger.datasource.repo.FireBaseDataRepo;
import com.example.juba.chatmessenger.repository.AuthRepo;
import com.example.juba.chatmessenger.repository.DataRepo;
import com.example.juba.chatmessenger.ui.login.LoginViewModel;
import com.example.juba.chatmessenger.ui.message.MessageViewModel;
import com.example.juba.chatmessenger.ui.register.RegisterViewModel;
import com.example.juba.chatmessenger.ui.settings.SettingsViewModel;
import androidx.lifecycle.ViewModel;
import java.util.Map;
import javax.inject.Provider;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class ViewModelModule
{

    @Provides
    ViewModelProviderFactory ViewModelProviderFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators)
    {
        return new ViewModelProviderFactory(creators);
    }




    @Provides
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    ViewModel bindLoginViewModel(AuthRepo authRepo)
    {
        return new LoginViewModel(authRepo);

    }

    @Provides
    @IntoMap
    @ViewModelKey(MessageViewModel.class)
    ViewModel provideMessageModel(FireBaseDataRepo fireBaseDataRepo)
    {
        return new MessageViewModel(fireBaseDataRepo);

    }


    @Provides
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    ViewModel bindRegisterViewModel(AuthRepo authRepo)
    {
        return new RegisterViewModel(authRepo);

    }

    @Provides
    @IntoMap
    @ViewModelKey(SettingsViewModel.class)
    ViewModel bindSettingsViewModel(AuthRepo authRepo, DataRepo dataRepo)
    {
        return new SettingsViewModel(authRepo,dataRepo);

    }



}
