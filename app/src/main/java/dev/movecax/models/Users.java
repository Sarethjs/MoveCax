package dev.movecax.models;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Users {
    private int Id;
    private String Names;
    private String Email;
    private String Password;
    private Date DateBorn;
    private char Sex;
    private History History;

    // Constructor para crear usuario con Strings y String para el género
    public Users(String Names, String Email, String Password, String DateBorn, String Sex, String sexo) {
        this.Names = Names;
        this.Email = Email;
        this.Password = Password;
        // Convierte la cadena DateBorn en un objeto Date, asumiendo un formato específico
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Ajusta el formato según tus necesidades
        try {
            this.DateBorn = dateFormat.parse(DateBorn);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Convierte la cadena Sex en un carácter
        if (Sex != null && !Sex.isEmpty()) {
            this.Sex = Sex.charAt(0);
        } else {
            this.Sex = ' '; // Valor predeterminado si la cadena está vacía
        }
    }

    // Getters y setters
    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getNames() {
        return Names;
    }

    public void setNames(String Names) {
        this.Names = Names;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public Date getDateBorn() {
        return DateBorn;
    }

    public void setDateBorn(Date DateBorn) {
        this.DateBorn = DateBorn;
    }

    public char getSex() {
        return Sex;
    }

    public void setSex(char Sex) {
        this.Sex = Sex;
    }

    public History getHistory() {
        return History;
    }

    public void setHistory(History History) {
        this.History = History;
    }

    public void createUser() {
        // Configurar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://possible-steady-narwhal.ngrok-free.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear una instancia de tu servicio Retrofit
        UserService userService = retrofit.create(UserService.class);

        // Realizar la llamada Retrofit para crear el usuario
        Call<Users> call = userService.createUser(this); // Pasar la instancia actual de Users

        try {
            Response<Users> response = call.execute();

            if (response.isSuccessful()) {
                Users createdUser = response.body();

                // Aquí puedes realizar acciones adicionales si es necesario
            } else {
                // Manejar el caso en que la solicitud no sea exitosa
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


