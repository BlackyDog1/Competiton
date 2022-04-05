package database.dao;

import database.DatabaseConnection;
import entity.PersonEntity;
import entity.PersonStageEntity;
import entity.StageEntity;
import entity.TeamEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;

public class PersonStageDao implements DaoI<PersonStageEntity> {
    DatabaseConnection connection = new DatabaseConnection();

    @Override
    public PersonStageEntity get(int id) {
        return connection.getEntityManager().find(PersonStageEntity.class, id);
    }

    @Override
    public List<PersonStageEntity> getAll() {
        TypedQuery<PersonStageEntity> query = connection.getEntityManager().createQuery("Select ps From PersonStageEntity ps", PersonStageEntity.class);
        return query.getResultList();
    }

    @Override
    public void create(PersonStageEntity personStageEntity) {
        connection.executeTransaction(EntityManager -> EntityManager.persist(personStageEntity));
    }

    public boolean setScore(String stageName, String username, String score) {

        StageDao exist = new StageDao();
        if(exist.doesStageExist(stageName)) {

            PersonStageEntity personStageEntity = new PersonStageEntity();
            double scoreNumber;
            try {
                scoreNumber = Double.parseDouble(score);
            } catch (NumberFormatException e) {
                System.out.println("Score is not a number");
                e.printStackTrace();
                return false;
            }

            personStageEntity.setScore(BigDecimal.valueOf(scoreNumber));

            TypedQuery<PersonEntity> query1 = connection.getEntityManager().createQuery("Select p From PersonEntity p Where p.username =:us", PersonEntity.class);
            query1.setParameter("us", username);
            PersonEntity person = query1.getSingleResult();
            personStageEntity.setIdPerson(person.getIdPerson());

            TypedQuery<StageEntity> query2 = connection.getEntityManager().createQuery("Select s From StageEntity s Where s.stageName =:name", StageEntity.class);
            query2.setParameter("name", stageName);
            StageEntity stage = query2.getSingleResult();
            personStageEntity.setIdStage(stage.getIdStage());

            personStageEntity.setVerified((byte) 0);

            connection.getEntityManager().getTransaction().begin();

            Query query3 = connection.getEntityManager().createQuery("UPDATE PersonStageEntity set score =:score where idPerson =:idPerson and " +
                    "idStage =:idStage");
            query3.setParameter("score", personStageEntity.getScore());
            query3.setParameter("idPerson", personStageEntity.getIdPerson());
            query3.setParameter("idStage", personStageEntity.getIdStage());
            query3.executeUpdate();

            connection.getEntityManager().getTransaction().commit();
            connection.getEntityManager().close();
            return true;
        }
        return false;
    }

    public void enrollCompetitor(String username, String stageName) {
        TypedQuery<StageEntity> query = connection.getEntityManager().createQuery("Select s From StageEntity s where s.stageName =:name1", StageEntity.class);
        query.setParameter("name1", stageName);

        StageEntity stage = query.getSingleResult();
        int idStage = stage.getIdStage();

        TypedQuery<PersonEntity> query2 = connection.getEntityManager().createQuery("Select p From PersonEntity p where p.username =:name2", PersonEntity.class);
        query2.setParameter("name2", username);

        PersonEntity person = query2.getSingleResult();

        int idPerson = person.getIdPerson();

        PersonStageEntity personStageEntity = new PersonStageEntity();

        personStageEntity.setVerified((byte) 0);
        personStageEntity.setIdPerson(idPerson);
        personStageEntity.setIdStage(idStage);
        personStageEntity.setScore(BigDecimal.valueOf(0));

        PersonStageDao personStageDao = new PersonStageDao();
        personStageDao.create(personStageEntity);

    }

    public List<PersonStageEntity> getScoreBoard() {

        TypedQuery <PersonStageEntity> query = connection.getEntityManager().createQuery("Select ps From PersonStageEntity ps, " +
                "PersonEntity p, StageEntity s Where ps.idStage = s.idStage And ps.idPerson = p.idPerson " +
                "And ps.verified =:param", PersonStageEntity.class);
        Integer verified = 0;
        query.setParameter("param", verified.byteValue());
        return query.getResultList();
    }

    public String getScoreByUsername(String username) {
        Query query = connection.getEntityManager().createQuery("Select SUM(ps.score) From PersonStageEntity ps, PersonEntity p " +
                "where p.username =:username and ps.idPerson = p.idPerson");
        query.setParameter("username", username);
        BigDecimal score = (BigDecimal) query.getSingleResult();
        String scoreReturned = String.valueOf(score);
        return scoreReturned;
    }

    public String getScoreByTeamId(int id) {
        Query query = connection.getEntityManager().createQuery("Select SUM(ps.score) From PersonStageEntity ps, PersonEntity p " +
                "Where p.idPerson = ps.idPerson and p.idTeam =:id");
        query.setParameter("id", id);
        BigDecimal score = (BigDecimal) query.getSingleResult();
        String scoreReturned = String.valueOf(score);
        return scoreReturned;
    }

    public boolean verifyCompetitor(int idStage, int idPerson) {
        try {
            connection.getEntityManager().getTransaction().begin();

            Integer verified = 1;
            Query query = connection.getEntityManager().createQuery("UPDATE PersonStageEntity set verified =:verified where idPerson =:idPerson and " +
                    "idStage =:idStage");
            query.setParameter("verified", verified.byteValue());
            query.setParameter("idPerson", idPerson);
            query.setParameter("idStage", idStage);
            query.executeUpdate();

            connection.getEntityManager().getTransaction().commit();
            connection.getEntityManager().close();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getScoreByID(int idPerson, int idStage) {
        Query query = connection.getEntityManager().createQuery("Select ps.score From PersonStageEntity ps " +
                "Where ps.idStage =:idStage and ps.idPerson =:idPerson");
        query.setParameter("idStage",idStage);
        query.setParameter("idPerson",idPerson);

        BigDecimal score = (BigDecimal) query.getSingleResult();
        String scoreReturned = String.valueOf(score);
        return scoreReturned;

    }
}
