package dev.movecax.models;

import android.util.Log;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import java.util.Date;

import dev.movecax.Presenters.RegistroUserPresenter;
import dev.movecax.Presenters.UserModelListener;
import dev.movecax.singleton.UserSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User {
    private int id;
    private String names;
    private String lastnames;
    private String token;
    private String email;
    private String password;
    private Date dateBorn;
    private char sex;
    private History History;

    public User() {}
    public User(String Names, String lastnames, String Email, String Password, Date DateBorn, char Sex) {
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
        Call<User> call = userService.createUser(this);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call,
                                   @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    presenter.showMessage("Usuario creado");
                } else {
                    presenter.showMessage("Error: " + response.errorBody());
                    Log.d("uses", "onResponse: " + response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call,
                                  @NonNull Throwable t) {
                presenter.showMessage("Error fatal al intentar conectar con el servidor");
            }
        });
    }


    public void findUser(UserModelListener.LoginListener listener) {

        UserService service = UserService.retrofit.create(UserService.class);
        Call<User> call = service.getUser(this);
        Log.d("uses", "findUser: Sending data: " + this);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call,
                                   @NonNull Response<User> response) {

                if (response.isSuccessful()){
                    User user = response.body();
                    Log.i("uses", "onResponse: " + user);

                    // Create User Singleton
                    UserSingleton.setCurrentUser(user);

                    listener.userLogged("Login exitoso");
                } else{
                    listener.userNotLogged("User not logged: " + response.errorBody());
                    Log.d("uses", "Response: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call,
                                  @NonNull Throwable t) {
                listener.onFailure();
                Log.d("uses", "onFailure: Request failed");
                Log.d("uses", "onFailure: T " + t);
            }
        });
    }

    public static void findUserByToken(JsonObject json, UserModelListener.LoginListener listener){
        UserService service = UserService.retrofit.create(UserService.class);
        Call<User> call = service.getUserByToken(json);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call,
                                   @NonNull Response<User> response) {
                Log.d("uses", "onResponse: Searching user by: " + json );
                if (response.isSuccessful()){
                    User user = response.body();

                    // Create user Singleton
                    UserSingleton.setCurrentUser(user);
                    listener.userLogged("Sesión iniciada");
                    Log.d("uses", "onResponse: Getting user by token" + response);
                }

                else {
                    listener.userNotLogged("Sesión no iniciada");
                    Log.d("uses", "onResponse: Getting user by token: (err)" + response.headers());
                }

            }

            @Override
            public void onFailure(@NonNull Call<User> call,
                                  @NonNull Throwable t) {
                listener.onFailure();

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", names='" + names + '\'' +
                ", lastnames='" + lastnames + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dateBorn=" + dateBorn +
                ", sex=" + sex +
                ", History=" + History +
                '}';
    }
}


