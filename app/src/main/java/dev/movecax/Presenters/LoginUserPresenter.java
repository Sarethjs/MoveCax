package dev.movecax.Presenters;

import dev.movecax.models.User;
import dev.movecax.views.LoginUserActivity;

public class LoginUserPresenter implements UserModelListener.LoginListener{

    private final LoginUserActivity view;

    public LoginUserPresenter(LoginUserActivity view) {
        this.view = view;
    }

    public void signUser(String email, String password) {

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.findUser(this);

    }

    @Override
    public void userLogged(String message) {
        view.userLogged(message);
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
