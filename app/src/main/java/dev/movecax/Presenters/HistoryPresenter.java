package dev.movecax.Presenters;

import java.util.List;

import dev.movecax.Fragment.HistorialFragment;
import dev.movecax.models.History;

public class HistoryPresenter {
    private final HistorialFragment view;

    public HistoryPresenter(HistorialFragment view) {
        this.view = view;
    }

    public void makeGetRequest(int userId) {
        view.showMessage("Fetching history...");
        History.get(userId, new HistoryModelListener.get() {
            @Override
            public void success(String msg, List<History> histories) {
                if (!histories.isEmpty()) {
                    view.historyAdapter.historyList = histories;
                    view.historyAdapter.notifyDataSetChanged();
                    view.hideAnimation();
                    view.showMessage(msg);
                } else
                    view.showMessage("No history");
            }

            @Override
            public void failure(String error) {
                view.showMessage(error);
            }
        });
    }

}
