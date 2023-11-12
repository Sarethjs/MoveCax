package dev.movecax.Presenters;

import dev.movecax.MainActivity;
import dev.movecax.models.User;
import dev.movecax.singleton.UserSingleton;
import dev.movecax.views.LoginUserActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class LoginUserPresenter implements UserModelListener.LoginListener{

    private final LoginUserActivity view;

    public LoginUserPresenter(LoginUserActivity view) {
        this.view = view;
    }

    public void signUser(String email, String password) {

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.login(this);

    }

    @Override
    public void userLogged(String message) {

        // Save Token on local device
        SharedPreferences sharedPreferences = view.getSharedPreferences("token.cax", Context.MODE_PRIVATE);
        String token = UserSingleton.getCurrentUser().getToken();

        SharedPreferences.Editor editor  = sharedPreferences.edit();
        editor.putString("authToken", token);
        editor.apply();

        view.userLogged(message);
        view.startActivity(new Intent(view, MainActivity.class));
        view.finish();
    }

    @Override
    public void userNotLogged(String message) {
        view.userNotLogged(message);
    }

    @Override
    public void onFailure() {
        view.userNotLogged("Error al hacer la petición");
    }
}
