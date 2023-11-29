package dev.movecax.Info;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import dev.movecax.Presenters.TakeRoute;
import dev.movecax.R;
import dev.movecax.models.Route;
import dev.movecax.singleton.UserSingleton;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private TextView tvRoutePrice, tvRouteName, tvDest, tvOrigin;
    private Button btnTakeRoute, btnCancel;
    public Route route;
    public String destStreet, originStreet;
    private TakeRoute presenter;
    private View infoLayout;
    private float dX, dY;

    public BottomSheetFragment() {
    }

    public BottomSheetFragment(Route route, String destStreet, String originStreet) {
        this.route = route;
        this.destStreet = destStreet;
        this.originStreet = originStreet;
        presenter = new TakeRoute(this);
    }

    public void setData(Route route) {
        // Setting price
        float price = route.getPrice();
        String strPrice = String.format(Locale.getDefault(), "S./ %.2f", price);
        this.tvRoutePrice.setText(strPrice);

        // Setting name
        this.tvRouteName.setText(route.getRouteName());

        // Setting destination
        this.tvDest.setText(this.destStreet == null ? "Not resolved" : this.destStreet);

        // Setting origin
        if (this.originStreet == null) {
            this.originStreet = "Your location";
        }
        this.tvOrigin.setText(this.originStreet);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        // Init components
        this.tvRoutePrice = view.findViewById(R.id.textViewTarifa);
        this.btnCancel = view.findViewById(R.id.btnCancel);
        this.btnTakeRoute = view.findViewById(R.id.btnTakeRoute);
        this.tvRouteName = view.findViewById(R.id.tvBusName);
        this.tvDest = view.findViewById(R.id.tvDest);
        this.tvOrigin = view.findViewById(R.id.tvOrigin);

        // Events for buttons
        this.btnCancel.setOnClickListener(v -> this.cancel());
        this.btnTakeRoute.setOnClickListener(v -> this.takeRoute());

        // Show route information
        this.setData(this.route);


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.route = savedInstanceState.getParcelable("route");
        }
    }

    private void takeRoute() {
        // Guardar el historial u otras operaciones necesarias
        this.presenter.saveHistory();

        // Cerrar el BottomSheetFragment
        dismiss();

        // Obtener la vista raíz del fragmento o actividad
        View rootView = getActivity().findViewById(android.R.id.content);

        if (infoLayout == null) {
            // Inflar el diseño personalizado solo si no hay una instancia activa
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            infoLayout = inflater.inflate(R.layout.info_layout, null);

            // Acceder al LinearLayout dentro de la vista personalizada
            LinearLayout linearLayout = infoLayout.findViewById(R.id.infoLinearLayout); // Usa el ID que agregaste en tu XML
            // Acceder a los TextView dentro del diseño personalizado
            TextView textView1 = infoLayout.findViewById(R.id.textView1);
            TextView textView2 = infoLayout.findViewById(R.id.textView2);
            TextView textView3 = infoLayout.findViewById(R.id.textView3);
            TextView textView4 = infoLayout.findViewById(R.id.textView4);

            // Personalizar el texto de cada TextView según sea necesario
            textView1.setText("Inicio");
            textView2.setText("Destino");
            textView3.setText("Debes subir en");
            textView4.setText("Debes bajar en");

            // Calcular el margen superior e inferior para centrar en la parte inferior
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenHeight = displayMetrics.heightPixels;
            int marginTop = screenHeight / 2 - dpToPixels(-210);  // Ajusta el valor según sea necesario
            int marginBottom = screenHeight / 2 - dpToPixels(400);  // Ajusta el valor según sea necesario

            // Crear parámetros de diseño con margen superior e inferior
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(dpToPixels(16), marginTop, 0, marginBottom);
            linearLayout.setLayoutParams(params);

            linearLayout.getLayoutParams().width = dpToPixels(380);

            // Añadir la vista personalizada al contenedor de la vista raíz
            ((ViewGroup) rootView).addView(infoLayout);

            // Puedes ajustar la visibilidad y animaciones según tus necesidades
            infoLayout.setVisibility(View.VISIBLE);

            // Configurar el botón de cierre solo después de inflar la vista
            setupCloseButton();

            // Configurar la funcionalidad de arrastrar la ventana
            setupDraggableWindow();
        }
    }


    private void cancel() {
        dismiss();
    }

    // Convierte dp a píxeles
    private int dpToPixels(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void setupCloseButton() {
        if (infoLayout != null) {
            ImageButton closeButton = infoLayout.findViewById(R.id.btnClose);  // Reemplaza "btnClose" con el ID de tu botón de cerrar

            closeButton.setOnClickListener(v -> {
                // Cerrar la vista personalizada al hacer clic en el botón de cerrar
                infoLayout.setVisibility(View.GONE);
            });
        }
    }

    private void setupDraggableWindow() {
        View infoLinearLayout = infoLayout.findViewById(R.id.infoLinearLayout);

        infoLinearLayout.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = v.getX() - event.getRawX();
                    dY = v.getY() - event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    v.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    break;

                default:
                    return false;
            }
            return true;
        });
    }
}

