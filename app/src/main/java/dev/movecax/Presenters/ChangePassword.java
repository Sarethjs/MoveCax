package dev.movecax.Presenters;

import android.content.Intent;
import android.util.Log;

import dev.movecax.Fragment.FourthFragment;
import dev.movecax.models.User;
import dev.movecax.singleton.UserSingleton;
import dev.movecax.views.LoginUserActivity;

public class ChangePassword implements UserModelListener.LogoutListener{

    private final FourthFragment view;

    public ChangePassword(FourthFragment view) {
        this.view = view;
    }

    public void logout(){
        User currentUser = UserSingleton.getCurrentUser();
        Log.d("uses", "logout: Current user: " + currentUser);
        currentUser.logout(this);
    }

    @Override
    public void userLogout(String message) {
        view.userLogout(message);
        view.startActivity(new Intent(view.getContext(), LoginUserActivity.class));
        view.getActivity().finish();
    }

    @Override
    public void onFailure(String message) {
        view.error(message);
    }
}
