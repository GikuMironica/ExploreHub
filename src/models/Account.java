package models;

import javax.persistence.EntityManager;

public interface Account {
    EntityManager getConnection();
}
