package dev.movecax.Info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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
        String strPrice = String.format(Locale.getDefault(),"S./ %.2f", price);
        this.tvRoutePrice.setText(strPrice);

        // Setting name
        this.tvRouteName.setText(route.getRouteName());

        // Setting destination
        this.tvDest.setText(this.destStreet == null ? "Not resolved" : this.destStreet);

        // Setting origin
        this.tvOrigin.setText(this.originStreet == null ? "Not resolved" : this.originStreet);
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
        this.presenter.saveHistory();
    }
    private void cancel() {
        dismiss();
    }
}

