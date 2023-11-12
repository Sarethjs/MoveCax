package dev.movecax.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import dev.movecax.Presenters.ProfilePresenter;
import dev.movecax.Presenters.contracts.LogoutUserContract;
import dev.movecax.R;
import dev.movecax.singleton.UserSingleton;

import dev.movecax.Base64.ImageUtil;

public class PerfilFragment extends Fragment implements LogoutUserContract {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton btnCambiarFotoPerfil;

    private String mParam1;
    private ProfilePresenter presenter;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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

        //EditText
        TextView etNombrePerfil = view.findViewById(R.id.etNombrePerfil);
        TextView etApellidosPerfil = view.findViewById(R.id.etApellidosPerfil);
        TextView etCorreoPerfil = view.findViewById(R.id.etCorreoPerfil);
        TextView etPasswordPerfil = view.findViewById(R.id.etPasswordPerfil);
        TextView etFechaNacimientoPerfil = view.findViewById(R.id.etFechaNacimientoPerfil);
        TextView etSexoPerfil = view.findViewById(R.id.etSexoPerfil);

        Log.d("uses", "onCreateView: Fields exists");

        TextView[] profileInformation = {
                etNombrePerfil, etApellidosPerfil,
                etCorreoPerfil, etPasswordPerfil,
                etFechaNacimientoPerfil, etSexoPerfil
        };


        // Debug for current user
        Log.d("uses", "onCreateView: Current user: " + UserSingleton.getCurrentUser());

        // Obtén una referencia al EditText que contiene la contraseña
        TextView etContraseña = view.findViewById(R.id.etPasswordPerfil);
        Button changePass = view.findViewById(R.id.closeSession);
        Log.d("uses", "onCreateView: Close session: " + changePass);

        // Register presenter
        presenter = new ProfilePresenter(this);
        presenter.showProfileInformation(profileInformation);
        changePass.setOnClickListener(v -> presenter.logout());


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

        btnCambiarFotoPerfil = view.findViewById(R.id.btnCambiarFotoPerfil);

        btnCambiarFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarFotoDePerfil();
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

                presenter.changePassword(currentPassword, newPassword, confirmPassword);

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

    @Override
    public void userLogout(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void error(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showMessage(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void seleccionarFotoDePerfil() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // La imagen seleccionada está en data.getData()
            Uri selectedImageUri = data.getData();

            try {
                // Convierte la Uri a cadena Base64 utilizando ImageUtil
                InputStream inputStream = requireActivity().getContentResolver().openInputStream(selectedImageUri);
                String base64Image = ImageUtil.uriToBase64(inputStream);

                // Puedes almacenar o utilizar la cadena Base64 según tus necesidades
                Log.d("Base64Image", "onActivityResult: " + base64Image);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Muestra la imagen en el ImageButton
            btnCambiarFotoPerfil.setImageURI(selectedImageUri);
        } else {
            Toast.makeText(getContext(), "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
        }
    }
}