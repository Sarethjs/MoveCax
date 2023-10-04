package dev.movecax.singleton;

import android.util.Log;

import dev.movecax.models.User;

public class UserSingleton {

    private static User currentUser = null;

    public static void setCurrentUser(User user){
        if (currentUser == null)
            currentUser = user;
        Log.d("uses", "setCurrentUser: User singleton: " + user);
    }

    public static User getCurrentUser(){
        return currentUser;
    }
}
