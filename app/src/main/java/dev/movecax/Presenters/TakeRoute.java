package dev.movecax.Presenters;

import dev.movecax.Info.BottomSheetFragment;
import dev.movecax.models.History;
import dev.movecax.singleton.UserSingleton;

public class TakeRoute {
    private final BottomSheetFragment view;

    public TakeRoute(BottomSheetFragment view) {
        this.view = view;
    }

    public void saveHistory() {

        History history = new History(
                UserSingleton.getCurrentUser().getEmail(),
                view.route.getRouteName(),
                view.destStreet
        );
    }
}
