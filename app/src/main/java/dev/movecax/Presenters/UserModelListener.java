package dev.movecax.Presenters;

public interface UserModelListener {

    interface LoginListener{
        void userLogged(String message);
        void userNotLogged(String message);
        void onFailure();
    }

    interface LogoutListener {
        void userLogout(String message);
        void onFailure(String message);
    }

    interface ChangePassListener {
        void  passwordChanged(String message);
        void passwordNotChanged(String message);
    }

    interface RegisterListener {
        void userCreated(String msg);
        void userNotCreated(String err);
    }

    interface RestorePassword {
        void success(String msg);
        void failure(String err);
    }
}
