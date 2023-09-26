package dev.movecax.models;

import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dev.movecax.Presenters.RegistroUserPresenter;
import dev.movecax.views.RegistroUserView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Users {
    private int id;
    private String names;
    private String lastnames;
    private String email;
    private String password;
    private Date dateBorn;
    private char sex;
    private History History;

    public Users () {}
    public Users(String Names, String lastnames, String Email, String Password, Date DateBorn,  char Sex) {
        this.names = Names;
        this.email = Email;
        this.lastnames = lastnames;
        this.password = Password;
        this.sex = Sex;
        this.dateBorn = DateBorn;
    }

    public void createUser(RegistroUserPresenter presenter) {
        Log.d("uses", "Creating user");

        UserService userService = UserService.retrofit.create(UserService.class);
        Call<Users> call = userService.createUser(this);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    presenter.showMessage("Usuario creado");
                } else {
                    presenter.showMessage("Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                presenter.showMessage("Error fatal al intentar conectar con el servidor");
            }
        });
    }


    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int Id) {
        this.id = Id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String Names) {
        this.names = Names;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String Email) {
        this.email = Email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String Password) {
        this.password = Password;
    }

    public Date getDateBorn() {
        return dateBorn;
    }

    public void setDateBorn(Date DateBorn) {
        this.dateBorn = DateBorn;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char Sex) {
        this.sex = Sex;
    }

    public History getHistory() {
        return History;
    }

    public String getLastnames() {
        return lastnames;
    }

    public void setLastnames(String lastnames) {
        this.lastnames = lastnames;
    }

    public void setHistory(History History) {
        this.History = History;
    }
}


