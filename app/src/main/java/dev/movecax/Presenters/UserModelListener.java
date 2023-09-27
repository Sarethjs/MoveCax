package dev.movecax.Presenters;

public interface UserModelListener {

    interface LoginListener{
        void userLogged(String message);
        void userNotLogged(String message);
        void onFailure();
    }


}
