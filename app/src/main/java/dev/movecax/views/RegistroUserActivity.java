package dev.movecax.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

        presenter = new RegistroUserPresenter(this);

        etDateBorn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        etSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderSelectionDialog();
            }
        });

        // Functionality for buttons
        Button btnRegister = findViewById(R.id.btnRegistrar);
        btnRegister.setOnClickListener(view-> this.createUser());

        TextView sign = this.findViewById(R.id.tvSign);
        sign.setOnClickListener(v-> {
            Intent intent = new Intent(this, LoginUserActivity.class);
            this.startActivity(intent);
            this.finish();
        });
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

    private void showDatePickerDialog() {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear un DatePickerDialog y establecer un listener para la selección de fecha
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int monthOfYear, int dayOfMonth) {
                        // Formatear la fecha seleccionada
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        calendar.set(selectedYear, monthOfYear, dayOfMonth);
                        String formattedDate = dateFormat.format(calendar.getTime());

                        // Actualizar el campo de texto con la fecha seleccionada
                        etDateBorn.setText(formattedDate);
                    }
                }, year, month, day);

        datePickerDialog.getDatePicker().init(year, month, day, null);

        // Mostrar el diálogo
        datePickerDialog.show();
    }

    private void showGenderSelectionDialog() {

        final String[] genderOptions = {"M", "F"};

        // Configura el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione el género")
                .setItems(genderOptions, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the selection
                        String selectedGender = genderOptions[which];
                        etSex.setText(selectedGender);
                    }
                });

        // Muestra el AlertDialog
        builder.show();
    }
}
