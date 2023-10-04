package dev.movecax.Presenters.contracts;

public interface LogoutUserContract {
    void userLogout(String message);
    void error(String message);
}
