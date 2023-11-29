package dev.movecax.Presenters;

import android.util.Log;
import android.widget.Toast;

import dev.movecax.Info.BottomSheetFragment;
import dev.movecax.models.History;
import dev.movecax.models.Route;
import dev.movecax.singleton.UserSingleton;

public class TakeRoute {
    private final BottomSheetFragment view;

    public TakeRoute(BottomSheetFragment view) {
        this.view = view;
    }

    public void saveHistory() {

        String origin = this.view.originStreet, dest = this.view.destStreet;
        Route route = this.view.route;

        History movement = new History();
        movement.setRouteName(route.getRouteName());
        movement.setOrigin(origin);
        movement.setDest(dest);
        movement.setUserId(UserSingleton.getCurrentUser().getId());

        History.create(movement, new HistoryModelListener.create() {
            @Override
            public void success(String msg) {
                Log.d("save_move", "success: " + msg);
            }

            @Override
            public void failure(String error) {
                Toast.makeText(view.getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
