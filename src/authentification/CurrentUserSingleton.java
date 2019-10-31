package authentification;

import models.User;

public class CurrentUserSingleton {
    private static CurrentUserSingleton ourInstance = null;
    private User user;
    public static CurrentUserSingleton getInstance() {
        if(ourInstance == null){
            ourInstance = new CurrentUserSingleton();
        }
        return ourInstance;
    }
    public void setUser(User u1){
        user = u1;
    }
    public User getUser(){
        return user;
    }
    private CurrentUserSingleton() {
        // c-tor
    }

}
