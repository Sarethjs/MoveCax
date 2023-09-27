package dev.movecax.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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


        // Set presenter for this view
        this.presenter = new LoginUserPresenter(this);
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
}