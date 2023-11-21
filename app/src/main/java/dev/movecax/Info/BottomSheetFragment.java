package dev.movecax.Info;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import dev.movecax.R;
import dev.movecax.models.Route;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private TextView textViewTarifa, tvRouteName;
    private Button btnTakeRoute, btnCancel;
    private Route route;

    public BottomSheetFragment() {
    }

    public BottomSheetFragment(Route route) {
        this.route = route;
        Log.d("inf_route", "BottomSheetFragment: Show data");
    }

    public void setData(Route route) {
        // Setting price
        float price = route.getPrice();
        String strPrice = String.format("S./ %.2f", price);
        this.textViewTarifa.setText(strPrice);

        // Setting name
        this.tvRouteName.setText(route.getRouteName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        // Init components
        this.textViewTarifa = view.findViewById(R.id.textViewTarifa);
        this.btnCancel = view.findViewById(R.id.btnCancel);
        this.btnTakeRoute = view.findViewById(R.id.btnTakeRoute);
        //this.tvRouteName = view.findViewById(R.id.)

        // Asignar eventos
        this.btnCancel.setOnClickListener(v -> this.cancel());
        this.btnTakeRoute.setOnClickListener(v -> this.takeRoute());

        // Mostrar información de la ruta
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

    }
    private void cancel() {

    }
}

