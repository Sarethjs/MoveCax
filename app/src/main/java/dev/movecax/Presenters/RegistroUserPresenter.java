package dev.movecax.Presenters;

import dev.movecax.models.UserService;
import dev.movecax.models.Users;
import dev.movecax.views.RegistroUserView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroUserPresenter {
    private RegistroUserView view;
    private UserService userService;

    public RegistroUserPresenter(RegistroUserView view) {
        this.view = view;

        // Configurar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://possible-steady-narwhal.ngrok-free.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear una instancia de tu servicio Retrofit
        userService = retrofit.create(UserService.class);
    }

    public void registrarUsuario(String nombre, String apellidos, String correo, String contraseña, String fechaNacimiento, String sexo) {
        // Crear un objeto Users con los datos del usuario
        Users user = new Users(nombre, apellidos, correo, contraseña, fechaNacimiento, sexo);

        // Realizar la llamada a la API utilizando la instancia userService ya configurada
        Call<Users> call = userService.createUser(user);

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    // Éxito en la llamada a la API
                    view.mostrarMensajeExito();
                } else {
                    // Error en la llamada a la API
                    view.mostrarMensajeError("Error al registrar el usuario");
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                // Manejo de errores de red
                view.mostrarMensajeError("Error de red al registrar el usuario");
            }
        });
    }
}




