package dev.movecax.Presenters;

import java.util.List;

import dev.movecax.models.History;

public interface HistoryModelListener {
    interface get {
        void success(String msg, List<History> histories);
        void failure(String error);
    }

    interface create {
        void success(String msg);
        void failure(String error);
    }
}
