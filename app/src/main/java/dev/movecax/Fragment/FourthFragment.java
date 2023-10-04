package dev.movecax.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputLayout;
import dev.movecax.R;

public class FourthFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FourthFragment() {
        // Required empty public constructor
    }

    public static FourthFragment newInstance(String param1, String param2) {
        FourthFragment fragment = new FourthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fourth, container, false);

        // Obtén una referencia al EditText que contiene la contraseña
        EditText etContraseña = view.findViewById(R.id.etContraseña);

        // Configura un OnClickListener para el ícono (drawableEnd)
        etContraseña.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_action_editar, 0);
        etContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre el popup para editar la contraseña o realiza la acción deseada aquí
                mostrarPopupCambiarContrasena();
            }
        });


        // También puedes configurar un OnFocusChangeListener para mostrar el ícono solo cuando el EditText tiene el foco
        etContraseña.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etContraseña.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_action_editar, 0);
                } else {
                    etContraseña.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0); // Elimina el ícono cuando no tiene foco
                }
            }
        });


        return view;
    }

    private void mostrarPopupCambiarContrasena() {
        // Crea un AlertDialog personalizado con EditText
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cambiar Contraseña");

        // Infla el layout personalizado del diálogo
        View viewInflated = LayoutInflater.from(getActivity()).inflate(R.layout.popup_cambiar_password, null);
        builder.setView(viewInflated);

        final EditText etPassword = viewInflated.findViewById(R.id.etPassword);
        final EditText etNewPassword = viewInflated.findViewById(R.id.etNewPassword);
        final EditText etConfirmPassword = viewInflated.findViewById(R.id.etConfirmPassword);

        builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String currentPassword = etPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                // Realiza la lógica para cambiar la contraseña aquí
                // Puedes validar, actualizar la contraseña en la base de datos, etc.
                
                // Cierra el diálogo
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
