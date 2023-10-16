package dev.movecax.Presenters;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import dev.movecax.Fragment.PerfilFragment;
import dev.movecax.models.User;
import dev.movecax.singleton.UserSingleton;
import dev.movecax.views.LoginUserActivity;

public class ChangePassword implements UserModelListener.LogoutListener{

    private final PerfilFragment view;

    public ChangePassword(PerfilFragment view) {
        this.view = view;
    }

    public void logout(){
        User currentUser = UserSingleton.getCurrentUser();
        Log.d("uses", "logout: Current user: " + currentUser);
        currentUser.logout(this);
    }

    public void showProfileInformation(TextView[] profileInformation) {

        User currentUser = UserSingleton.getCurrentUser();

        profileInformation[0].setText(currentUser.getNames());
        profileInformation[1].setText(currentUser.getLastnames());
        profileInformation[2].setText(currentUser.getEmail());
        profileInformation[3].setText("********");
        profileInformation[4].setText(currentUser.getDateBorn().toString());
        profileInformation[5].setText(String.valueOf(currentUser.getSex()));
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
