package database.dao;

import database.DatabaseConnection;
import entity.NotificationsEntity;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class NotificationsDao implements DaoI<NotificationsEntity> {
    DatabaseConnection connection = new DatabaseConnection();

    @Override
    public NotificationsEntity get(int id) {
        return connection.getEntityManager().find(NotificationsEntity.class, id);
    }

    @Override
    public List<NotificationsEntity> getAll() {
        TypedQuery<NotificationsEntity> query = connection.getEntityManager().createQuery("SELECT n FROM NotificationsEntity n", NotificationsEntity.class);
        return query.getResultList();
    }

    @Override
    public void create(NotificationsEntity notificationsEntity) {
        connection.executeTransaction(entityManager -> entityManager.persist(notificationsEntity));
    }

    public List<NotificationsEntity> getNotificationsByID(int id) {
        TypedQuery<NotificationsEntity> query = connection.getEntityManager().createQuery("SELECT n From NotificationsEntity n " +
                "Where n.idPerson =:id", NotificationsEntity.class);
        query.setParameter("id", id);

        return query.getResultList();
    }

    public void deleteNotification(int idPerson, int idStage) {

        connection.getEntityManager().getTransaction().begin();

        Query query = connection.getEntityManager().createQuery("DELETE FROM NotificationsEntity n Where n.idPerson =:idPerson" +
                " AND n.idStage =:idStage");
        query.setParameter("idPerson", idPerson);
        query.setParameter("idStage", idStage);
        query.executeUpdate();

        connection.getEntityManager().getTransaction().commit();
        connection.getEntityManager().close();

    }
}
