package discussionComponent.controllers;

import authentification.CurrentAccountSingleton;
import models.Account;
import models.ForumCategory;

import javax.persistence.EntityManager;

public class ViewCategoryController {
    private ForumCategory forumCategory;
    private EntityManager em = CurrentAccountSingleton.getInstance().getAccount().getConnection();

    public ViewCategoryController(ForumCategory fc){
        this.forumCategory = fc;
    }

}
