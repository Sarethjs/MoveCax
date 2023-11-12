package dev.movecax.Presenters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dev.movecax.MainActivity;
import dev.movecax.models.User;
import dev.movecax.views.RegistroUserActivity;

public class RegistroUserPresenter implements UserModelListener.LoginListener,
    UserModelListener.RegisterListener{
    private RegistroUserActivity view;
    private User user;

    public RegistroUserPresenter(RegistroUserActivity view) {
        this.view = view;
    }


    public void createUser(String names, String lastnames, String email, String password,
                           String dateBorn, char sex){

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(dateBorn);
            this.user = new User(names, lastnames, email, password, date, sex);
            user.createUser(this);
        } catch (ParseException e) {
            // We can show a custom error message
            this.view.showMessage("Ingrese una fecha en el formato correcto");
        }
    }

    @Override
    public void userLogged(String message) {
        this.view.showMessage(message);
    }

    @Override
    public void userNotLogged(String message) {
        this.view.showMessage(message);
    }

    @Override
    public void onFailure() {
        this.view.showMessage("Error al realizar la petición");
    }

    public void setView(RegistroUserActivity view) {
        this.view = view;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void userCreated(String msg) {
        Toast.makeText(this.view.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

        // Redirect to main page
        Intent intent = new Intent(this.view, MainActivity.class);
        this.view.startActivity(intent);
        this.view.finish();
    }

    @Override
    public void userNotCreated(String err) {
        Toast.makeText(this.view.getApplicationContext(), err, Toast.LENGTH_SHORT).show();
    }
}




