package dev.movecax.views;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dev.movecax.Presenters.RegistroUserPresenter;
import dev.movecax.R;

public class RegistroUserActivity extends AppCompatActivity implements RegistroUserView{

    private RegistroUserPresenter presenter;

    private EditText etNames;
    private EditText etLastnames;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etDateBorn;
    private EditText etSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_user);

        etNames = findViewById(R.id.etNombre);
        etLastnames = findViewById(R.id.etApellidos);
        etEmail = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etContraseña);
        etDateBorn = findViewById(R.id.etFechaNacimiento);
        etSex = findViewById(R.id.etSexo);
        Button btnRegister = findViewById(R.id.btnRegistrar);

        presenter = new RegistroUserPresenter(this);

        // Functionality for buttons
        btnRegister.setOnClickListener(view-> this.createUser());
    }

    @Override
    public void showError() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void createUser(){

        String names = etNames.getText().toString();
        String lastnames = etLastnames.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String dateBorn = etDateBorn.getText().toString();
        char sex = etSex.getText().charAt(0);

        presenter.createUser(names, lastnames, email, password, dateBorn, sex);
    }

}
