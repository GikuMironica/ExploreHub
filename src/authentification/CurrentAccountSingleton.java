package authentification;

import models.Account;
import models.User;

public class CurrentAccountSingleton {
    private static CurrentAccountSingleton ourInstance = null;
    private static Account account;

    private CurrentAccountSingleton() {
        // c-tor
    }
    public static CurrentAccountSingleton getInstance() {
        if(ourInstance == null){
            ourInstance = new CurrentAccountSingleton();
        }
        return ourInstance;
    }
    public void setAccount(Account account){
        this.account = account;
    }
    public Account getAccount(){
        return account;
    }

}
