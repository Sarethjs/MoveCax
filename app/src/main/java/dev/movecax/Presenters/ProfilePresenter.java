package dev.movecax.Presenters;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import dev.movecax.Fragment.PerfilFragment;
import dev.movecax.models.PasswordChangeRequest;
import dev.movecax.models.User;
import dev.movecax.singleton.UserSingleton;
import dev.movecax.views.LoginUserActivity;

public class ProfilePresenter implements UserModelListener.LogoutListener,
    UserModelListener.ChangePassListener{

    private final PerfilFragment view;

    public ProfilePresenter(PerfilFragment view) {
        this.view = view;
    }

    public void logout(){
        User currentUser = UserSingleton.getCurrentUser();
        Log.d("uses", "logout: Current user: " + currentUser);
        currentUser.logout(this);
    }


    public void changePassword(String actualPassword, String newPassword, String confirmPassword){

        if (!newPassword.equals(confirmPassword)){
            onFailure("Las contraseñas no coinciden");
            return;
        }
        User currentUser = UserSingleton.getCurrentUser();
        PasswordChangeRequest request = new PasswordChangeRequest(
                currentUser.getEmail(),
                actualPassword,
                newPassword);
        currentUser.changePassword(this, request);
    }


    public void showProfileInformation(TextView[] profileInformation) {

        User currentUser = UserSingleton.getCurrentUser();

        profileInformation[0].setText(currentUser.getNames());
        profileInformation[1].setText(currentUser.getLastnames());
        profileInformation[2].setText(currentUser.getEmail());
        profileInformation[3].setText("********");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String dateFormatted = dateFormat.format(currentUser.getDateBorn());

        profileInformation[4].setText(dateFormatted);
        profileInformation[5].setText(String.valueOf(currentUser.getSex()));
    }

    @Override
    public void passwordChanged(String message) {
        view.showMessage(message);
    }

    @Override
    public void passwordNotChanged(String message) {
        onFailure(message);
    }

    @Override
    public void userLogout(String message) {
        view.userLogout(message);
        view.startActivity(new Intent(view.getContext(), LoginUserActivity.class));

        if (view.getActivity() != null) {
            view.getActivity().finish();
        } else {
            onFailure("Unknown error");
        }
    }

    @Override
    public void onFailure(String message) {
        view.error(message);
    }
}
