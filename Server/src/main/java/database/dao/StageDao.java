package database.dao;

import database.DatabaseConnection;
import entity.PersonStageEntity;
import entity.StageEntity;
import entity.TeamEntity;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class StageDao implements DaoI <StageEntity> {
    DatabaseConnection connection = new DatabaseConnection();

    @Override
    public StageEntity get(int id) {
        return connection.getEntityManager().find(StageEntity.class, id);
    }

    @Override
    public List<StageEntity> getAll() {
        TypedQuery<StageEntity> query = connection.getEntityManager().createQuery("Select s From StageEntity s", StageEntity.class);
        return query.getResultList();
    }

    @Override
    public void create(StageEntity stageEntity) {
        connection.executeTransaction(EntityManager -> EntityManager.persist(stageEntity));
    }

    public boolean deleteStage(String stageName) {

        if(doesStageExist(stageName)) {

            TypedQuery<StageEntity> query = connection.getEntityManager().createQuery("Select s From StageEntity s where s.stageName =:name", StageEntity.class);
            query.setParameter("name", stageName);

            StageEntity stage = query.getSingleResult();

            connection.getEntityManager().getTransaction().begin();

            Query query2 = connection.getEntityManager().createQuery("DELETE FROM StageEntity s Where s.idStage =:idStage");
            query2.setParameter("idStage", stage.getIdStage());
            query2.executeUpdate();

            connection.getEntityManager().getTransaction().commit();
            connection.getEntityManager().close();

            return true;
        }
        return false;
    }

    public boolean doesStageExist(String stageName) {
        Long count = (Long) connection.getEntityManager().createQuery("Select count(s) from StageEntity s " +
                "where s.stageName =:stageName").setParameter("stageName", stageName).getSingleResult();

        return ( ( count.equals(0L) ) ? false : true);
    }

    public String getStageNameById(int id) {
        TypedQuery <StageEntity> query = connection.getEntityManager().createQuery("Select s from StageEntity s " +
                "Where s.idStage =:id", StageEntity.class);
        query.setParameter("id", id);

        StageEntity stage = query.getSingleResult();

        return stage.getStageName();
    }

    public int getIdByName(String stageName) {
        TypedQuery <StageEntity> query = connection.getEntityManager().createQuery("Select s from StageEntity s " +
                "Where s.stageName =:name", StageEntity.class);
        query.setParameter("name", stageName);

        StageEntity stage = query.getSingleResult();

        return stage.getIdStage();
    }
}
