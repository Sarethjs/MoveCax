package dev.movecax.views;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;

import dev.movecax.Presenters.RegistroUserPresenter;
import dev.movecax.R;
import dev.movecax.models.Users;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroUserActivity extends AppCompatActivity implements RegistroUserView{

    private RegistroUserPresenter presenter;

    private EditText etNombre;
    private EditText etApellidos;
    private EditText etCorreo;
    private EditText etContraseña;
    private EditText etFechaNacimiento;
    private EditText etSexo;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_user);


        // Enlazar los elementos de la interfaz de usuario con las variables
        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        etCorreo = findViewById(R.id.etCorreo);
        etContraseña = findViewById(R.id.etContraseña);
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        etSexo = findViewById(R.id.etSexo);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        presenter = new RegistroUserPresenter(this);

        // Funcs to buttons
        this.btnRegistrar.setOnClickListener(view-> this.createUser());
    }

    @Override
    public void showError() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void createUser(){

        String names = etNombre.getText().toString();
        String lastnames = etApellidos.getText().toString();
        String email = etCorreo.getText().toString();
        String password = etContraseña.getText().toString();
        String dateBorn = etFechaNacimiento.getText().toString();
        char sex = etSexo.getText().charAt(0);

        presenter.createUser(names, lastnames, email, password, dateBorn, sex);
    }

}
