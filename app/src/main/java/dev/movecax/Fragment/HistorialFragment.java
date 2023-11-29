package dev.movecax.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.movecax.Adapters.HistoryAdapter;
import dev.movecax.Presenters.HistoryPresenter;
import dev.movecax.R;
import dev.movecax.models.History;
import dev.movecax.singleton.UserSingleton;


public class HistorialFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public HistoryAdapter historyAdapter;
    private HistoryPresenter historyPresenter;
    private LottieAnimationView historyAnimation;

    private String mParam1;
    private String mParam2;

    public HistorialFragment() { }

    public static HistorialFragment newInstance(String param1, String param2) {
        HistorialFragment fragment = new HistorialFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_third, container, false);

        this.historyAnimation = rootView.findViewById(R.id.lottieAnimation);

        // Crea y establece el adaptador para el RecyclerView
        RecyclerView recyclerViewHistory = rootView.findViewById(R.id.recyclerViewHistory);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        this.historyAdapter = new HistoryAdapter(new ArrayList<>());
        recyclerViewHistory.setAdapter(this.historyAdapter);

        // Setting presenter and making request
        this.historyPresenter = new HistoryPresenter(this);
        this.historyPresenter.makeGetRequest(UserSingleton.getCurrentUser().getId());

        return rootView;
    }

    public void showMessage(String msg) {
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void hideAnimation() {
        this.historyAnimation.setVisibility(View.GONE);
    }

}
