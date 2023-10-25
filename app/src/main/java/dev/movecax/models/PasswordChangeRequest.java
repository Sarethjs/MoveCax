package dev.movecax.models;

public class PasswordChangeRequest {

    private final String email;
    private final String password;
    private final String newPassword;


    public PasswordChangeRequest(String email, String password, String newPassword) {
        this.email = email;
        this.password = password;
        this.newPassword = newPassword;
    }
}
