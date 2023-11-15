package dev.movecax.Presenters;

import dev.movecax.MainActivity;
import dev.movecax.models.ResetRequest;
import dev.movecax.models.User;
import dev.movecax.singleton.UserSingleton;
import dev.movecax.views.LoginUserActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class LoginUserPresenter implements UserModelListener.LoginListener,
UserModelListener.RestorePassword{

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


    public void restorePassRequest(String email) {
        ResetRequest request = new ResetRequest(email);
        User.restorePassword(request, this);
    }

    @Override
    public void userNotLogged(String message) {
        view.userNotLogged(message);
    }

    @Override
    public void onFailure() {
        view.userNotLogged("Error al hacer la petición");
    }

    @Override
    public void success(String msg) {
        Toast.makeText(this.view.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void failure(String err) {
        Toast.makeText(this.view.getApplicationContext(), err, Toast.LENGTH_SHORT).show();
    }
}
