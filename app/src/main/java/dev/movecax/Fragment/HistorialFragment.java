package dev.movecax.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.movecax.Adapters.HistoryAdapter;
import dev.movecax.R;
import dev.movecax.models.History;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistorialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistorialFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistorialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistorialFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        // Crea una lista de ejemplo con objetos History (cámbiala según tus necesidades)
        List<History> historyList = new ArrayList<>();
        historyList.add(new History("email1", "Route 1", "Dest 1", new Date()));
        historyList.add(new History("email2", "Route 2", "Dest 2", new Date()));
        historyList.add(new History("email3", "Route 3", "Dest 3", new Date()));
        historyList.add(new History("email1", "Route 1", "Dest 1", new Date()));
        historyList.add(new History("email2", "Route 2", "Dest 2", new Date()));
        historyList.add(new History("email3", "Route 3", "Dest 3", new Date()));
        historyList.add(new History("email1", "Route 1", "Dest 1", new Date()));
        historyList.add(new History("email2", "Route 2", "Dest 2", new Date()));
        historyList.add(new History("email3", "Route 3", "Dest 3", new Date()));

        // Crea y establece el adaptador para el RecyclerView
        RecyclerView recyclerViewHistory = rootView.findViewById(R.id.recyclerViewHistory);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        HistoryAdapter historyAdapter = new HistoryAdapter(historyList);
        recyclerViewHistory.setAdapter(historyAdapter);

        // Oculta la animación Lottie si hay datos en la lista
        if (historyList != null && !historyList.isEmpty()) {
            rootView.findViewById(R.id.lottieAnimationView).setVisibility(View.GONE);
        }

        return rootView;
    }
}
