package discussionComponent.controllers;

import authentification.loginProcess.CurrentAccountSingleton;
import models.Account;
import models.Topic;

import javax.persistence.EntityManager;

public class ViewTopicController {
    private Topic topic;
    private Account currentUser = CurrentAccountSingleton.getInstance().getAccount();
    private EntityManager em = currentUser.getConnection();

    public ViewTopicController(Topic topic){
        this.topic = topic;
    }
}
