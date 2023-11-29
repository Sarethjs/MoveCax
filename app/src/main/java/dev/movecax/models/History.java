package dev.movecax.models;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.movecax.Presenters.HistoryModelListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class History {

    private int userId;
    private String routeName;
    private String dest, origin;
    private Date date;
    private static final HistoryService service = RouteService.retrofit.create(HistoryService.class);

    public History() { }
    public History(String routeName, String origin, String destination, Date dateTime) {
        this.routeName = routeName;
        this.origin = origin;
        this.dest = destination;
        this.date = dateTime;
    }

    public static void get(int userId, HistoryModelListener.get listener) {
        Call<List<History>> call = History.service.getHistory(userId);
        call.enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(@NonNull Call<List<History>> call,
                                   @NonNull Response<List<History>> response) {

                List<History> histories = new ArrayList<>();

                if (response.isSuccessful()) {
                   if (response.body() != null)
                       histories.addAll(response.body());

                    listener.success("Loaded", histories);

                } else
                    listener.failure("Error fetching data");
            }

            @Override
            public void onFailure(@NonNull Call<List<History>> call,
                                  @NonNull Throwable t) {
                listener.failure("Error: " + t.getMessage());
                Log.e("fetch_history", "onFailure: ", t);
            }
        });
    }

    public static void create(History history, HistoryModelListener.create listener) {
        final Call<Void> call = History.service.create(history);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.success("Movement saved");
                } else
                    listener.failure("Error saving movement");
            }

            @Override
            public void onFailure(@NonNull Call<Void> call,
                                  @NonNull Throwable t) {
                listener.failure("Error: " + t.getCause());
            }
        });
    }

    @NonNull
    @Override
    public String toString() {
        return "History{" +
                "routeName='" + routeName + '\'' +
                ", dest='" + dest + '\'' +
                ", origin='" + origin + '\'' +
                ", date=" + date +
                '}';
    }

    public String getDest() {
        return dest;
    }

    public String getOrigin() {
        return origin;
    }

    public Date getDate() {
        return date;
    }
    public String getRouteName() {
        return routeName;
    }

    public int getUserId(int id) {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
