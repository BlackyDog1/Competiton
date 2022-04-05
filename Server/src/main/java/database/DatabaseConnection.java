package database;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.function.Consumer;

public class DatabaseConnection {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public DatabaseConnection() {
        this.initTransaction();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public boolean executeTransaction(Consumer<EntityManager> action)
    {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            action.accept(entityManager);
            transaction.commit();
        } catch (RuntimeException e) {
            System.err.println("Transaction error: " + e.getLocalizedMessage());
            transaction.rollback();
            return false;
        }
        return true;

    }

    public void initTransaction() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("default");
            entityManager = entityManagerFactory.createEntityManager();
        } catch (Exception e) {
            System.err.println("Error at initializing DatabaseManager " + e.getMessage());
        }
    }
}