package dev.movecax.Presenters;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dev.movecax.models.User;
import dev.movecax.views.RegistroUserActivity;

public class RegistroUserPresenter {
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

    public void showMessage(String msg) {
        view.showMessage(msg);
    }

    public void setView(RegistroUserActivity view) {
        this.view = view;
    }

    public void setUser(User user) {
        this.user = user;
    }

}




