package dev.movecax.models;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

import dev.movecax.Presenters.HistoryModelListener;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class History {

    private final String email, routeName;
    private String dest, origin;
    private Date date;
    private static final HistoryService service = RouteService.retrofit.create(HistoryService.class);

    public History(String email, String routeName, String dest) {
        this.email = email;
        this.routeName = routeName;
    }


    public static void get(HistoryModelListener.get listener, int userId) {
        Call<List<History>> call = History.service.getHistory(userId);
        call.enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(@NonNull Call<List<History>> call,
                                   @NonNull Response<List<History>> response) {

            }

            @Override
            public void onFailure(@NonNull Call<List<History>> call,
                                  @NonNull Throwable t) {

            }
        });
    }


}
