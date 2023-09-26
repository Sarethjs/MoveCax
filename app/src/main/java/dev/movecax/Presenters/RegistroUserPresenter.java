package dev.movecax.Presenters;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import dev.movecax.models.UserService;
import dev.movecax.models.Users;
import dev.movecax.views.RegistroUserActivity;
import dev.movecax.views.RegistroUserView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroUserPresenter {
    private RegistroUserActivity view;
    private Users user;

    public RegistroUserPresenter(RegistroUserActivity view) {
        this.view = view;
    }


    public void createUser(String names, String lastnames, String email, String password,
                           String dateBorn, char sex){

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(dateBorn);
            this.user = new Users(names, lastnames, email, password, date, sex);
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

    public void setUser(Users user) {
        this.user = user;
    }

}




