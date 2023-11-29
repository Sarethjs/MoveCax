package dev.movecax.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import dev.movecax.R;
import dev.movecax.models.History;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    public List<History> historyList;

    public HistoryAdapter(List<History> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History historyItem = historyList.get(position);

        holder.textViewRouteName.setText(historyItem.getRouteName());
        holder.textViewOrigin.setText(historyItem.getOrigin());
        holder.textViewDest.setText(historyItem.getDest());

        // Formatear la fecha y hora
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String formattedDateTime = dateFormat.format(historyItem.getDate());
        holder.textViewDateTime.setText(formattedDateTime);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRouteName;
        TextView textViewOrigin;
        TextView textViewDest;
        TextView textViewDateTime;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRouteName = itemView.findViewById(R.id.textViewRouteName);
            textViewOrigin = itemView.findViewById(R.id.textViewOrigin);
            textViewDest = itemView.findViewById(R.id.textViewDest);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
        }
    }
}
