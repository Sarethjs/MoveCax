package dev.movecax.views;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import dev.movecax.Presenters.RegistroUserPresenter;
import dev.movecax.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroUserActivity extends AppCompatActivity {

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

        // Configurar el presentador con la actividad
        presenter = new RegistroUserPresenter(this);


        // Agregar un listener al botón "Registrar" para manejar el clic
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los datos del formulario
                String nombre = etNombre.getText().toString();
                String apellidos = etApellidos.getText().toString();
                String correo = etCorreo.getText().toString();
                String contraseña = etContraseña.getText().toString();
                String fechaNacimiento = etFechaNacimiento.getText().toString();
                String sexo = etSexo.getText().toString();

                // Llamar al método del presentador para registrar el usuario
                presenter.registrarUsuario(nombre, apellidos, correo, contraseña, fechaNacimiento, sexo);
            }
        });
    }
}
