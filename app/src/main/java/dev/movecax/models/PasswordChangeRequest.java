package dev.movecax.models;

public class PasswordChangeRequest {

    private final String actualPassword;
    private final String newPassword;


    public PasswordChangeRequest(String actualPassword, String newPassword) {
        this.actualPassword = actualPassword;
        this.newPassword = newPassword;
    }
}
