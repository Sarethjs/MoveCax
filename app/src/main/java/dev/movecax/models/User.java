package dev.movecax.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Date;

import dev.movecax.Presenters.UserModelListener;
import dev.movecax.models.helpers.ErrorFormatter;
import dev.movecax.singleton.UserSingleton;
import okhttp3.ResponseBody;
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

    public void createUser(UserModelListener.RegisterListener listener) {
        Log.d("uses", "Creating user");

        UserService userService = UserService.retrofit.create(UserService.class);
        Call<User> call = userService.createUser(this);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call,
                                   @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    listener.userCreated("Usuario registrado");

                    // Create user Singleton
                    UserSingleton.setCurrentUser((User) response.body());
                } else {
                    String errMsg = ErrorFormatter.parseError(response.errorBody());
                    listener.userNotCreated("Error: "+ errMsg);
                    Log.d("uses", "onResponse: " + response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call,
                                  @NonNull Throwable t) {
                listener.userNotCreated("Error al conectarse con el servidor");
            }
        });
    }


    public void login(UserModelListener.LoginListener listener) {

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
                    String error = ErrorFormatter.parseError(response.errorBody());
                    listener.userNotLogged(error);
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
                    Log.d("uses", "onResponse: User by token: " + UserSingleton.getCurrentUser());
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

    public void logout(UserModelListener.LogoutListener listener) {
        UserService service = UserService.retrofit.create(UserService.class);
        Call<Void> call = service.logout(this);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    // Remove Singleton
                    UserSingleton.destroySession(); // Fix this
                    listener.userLogout("Good bye, I'll waiting for you");
                }
                else {
                    listener.onFailure("Vuelve a intentarlo");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call,
                                  @NonNull Throwable t) {
                listener.onFailure("No se pudo conectar al servidor");
            }
        });
    }

    public void changePassword(UserModelListener.ChangePassListener listener,
                               PasswordChangeRequest changeRequest) {

        UserService service = UserService.retrofit.create(UserService.class);
        Call<User> call = service.changePassword(changeRequest);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call,
                                   @NonNull Response<User> response) {
                Log.d("sar_pass", "onResponse: " + response);
                if (response.isSuccessful()) {
                    UserSingleton.setCurrentUser(response.body());
                    listener.passwordChanged("Contraseña actualizada");
                }
                else {
                    String error = ErrorFormatter.parseError(response.errorBody());
                    listener.passwordNotChanged(error);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call,
                                  @NonNull Throwable t) {
                listener.passwordNotChanged("Pass not: " + t);
            }
        });

    }

    public static void restorePassword(ResetRequest request, UserModelListener.RestorePassword listener) {

        UserService service = UserService.retrofit.create(UserService.class);
        Call<ResponseBody> call = service.restorePassword(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("restore_pas", "onResponse: " + response.body());
                        JsonParser parser = new JsonParser();
                        String jsonResponse = response.body().string();
                        JsonObject jsonObject = parser.parse(jsonResponse).getAsJsonObject();
                        listener.success(jsonObject.get("msg").getAsString());
                    } catch (IOException | NullPointerException e) {
                        listener.failure("Error leyendo respuesta del servidor");
                    }

                } else {
                    Log.d("restore_pas", "onResponse: " + response.errorBody());
                    String error = ErrorFormatter.parseError(response.errorBody());
                    listener.failure(error);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,
                                  @NonNull Throwable t) {
                listener.failure("Server not found");
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


