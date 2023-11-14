package dev.movecax.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import dev.movecax.Presenters.LoginUserPresenter;
import dev.movecax.Presenters.contracts.LoginUserContract;
import dev.movecax.R;
import dev.movecax.models.User;

public class LoginUserActivity extends AppCompatActivity implements LoginUserContract {

    private EditText etEmail, etPassword;
    private Button btnSign;
    private LoginUserPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        // Find and init widgets
        this.etEmail = this.findViewById(R.id.etEmail);
        this.etPassword = this.findViewById(R.id.etPassword);

        this.btnSign = this.findViewById(R.id.btnSign);
        this.btnSign.setOnClickListener(v->  login());

        TextView tvRegister = this.findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(v-> {
            Intent intent = new Intent(this, RegistroUserActivity.class);
            this.startActivity(intent);
            this.finish();
        });

        // Set presenter for this view
        this.presenter = new LoginUserPresenter(this);


        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetPasswordDialog();
            }
        });

    }



    private void login() {

        String email = this.etEmail.getText().toString();
        String password = this.etPassword.getText().toString();
        this.presenter.signUser(email, password);

        // Disable button
        this.btnSign.setActivated(false);
        Toast.makeText(this, "Validando, por favor espere", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void userLogged(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // Open another activity
    }

    @Override
    public void userNotLogged(String message) {
        // Enable button
        this.btnSign.setActivated(false);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showResetPasswordDialog() {
        // Infla el diseño del diálogo
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);

        // Configura el cuadro de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Recuperar Contraseña");

        // Configura el botón de restablecimiento de contraseña en el diálogo
        builder.setPositiveButton("Restablecer Contraseña ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Aquí implementa la lógica para enviar la contraseña temporal al correo proporcionado
                EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);
                String userEmail = editTextEmail.getText().toString();
                // Implementa la lógica de envío de correo aquí

                // Cierra el diálogo
                dialog.dismiss();
            }
        });

        // Configura el botón de cancelar
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Muestra el diálogo
        builder.create().show();
    }
}