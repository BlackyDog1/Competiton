package database.dao;

import database.DatabaseConnection;
import entity.PersonEntity;
import entity.PersonStageEntity;
import entity.StageEntity;
import entity.TeamEntity;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class PersonDao implements DaoI<PersonEntity> {
    DatabaseConnection connection = new DatabaseConnection();


    @Override
    public PersonEntity get(int id) {
        return connection.getEntityManager().find(PersonEntity.class, id);
    }

    @Override
    public List<PersonEntity> getAll() {
        TypedQuery<PersonEntity> query = connection.getEntityManager().createQuery("SELECT p FROM PersonEntity p", PersonEntity.class);
        return query.getResultList();
    }

    @Override
    public void create(PersonEntity personEntity) {
        connection.executeTransaction(entityManager -> entityManager.persist(personEntity));
    }

    public boolean isUsernameTaken(String username) {
        TypedQuery<PersonEntity> query = connection.getEntityManager().createQuery("SELECT p FROM PersonEntity p WHERE p.username =:usernameSearched", PersonEntity.class);
        query.setParameter("usernameSearched", username);
        return query.getResultList().size() == 1;
    }

    public boolean isAdmin(String username) {
        TypedQuery<PersonEntity> query = connection.getEntityManager().createQuery("SELECT p FROM PersonEntity p WHERE p.username =:usernameSearched", PersonEntity.class);
        query.setParameter("usernameSearched", username);
        PersonEntity result = query.getSingleResult();
        System.out.println(result.getRole());
        if(result.getRole().equals("Administrator"))
            return true;
        return false;
    }

    public List<PersonEntity> getRanking() {
        TypedQuery<PersonEntity> query = connection.getEntityManager().createQuery("Select p FROM PersonEntity p, PersonStageEntity ps" +
                " WHERE p.idPerson = ps.idPerson GROUP BY p ORDER BY SUM(ps.score) DESC", PersonEntity.class);
        return query.getResultList();
    }

    public boolean deleteUser(String username) {
        if(doesUserExist(username)) {
            TypedQuery<PersonEntity> query = connection.getEntityManager().createQuery("Select p From PersonEntity p where p.username =:username", PersonEntity.class);
            query.setParameter("username", username);

            System.out.println(username);

            PersonEntity person = query.getSingleResult();

            System.out.println(person.getUsername());
            System.out.println(person.getIdPerson());
            connection.getEntityManager().getTransaction().begin();

            Query query2 = connection.getEntityManager().createQuery("DELETE FROM PersonEntity p Where p.idPerson =:idPerson");
            query2.setParameter("idPerson", person.getIdPerson());
            query2.executeUpdate();

            connection.getEntityManager().getTransaction().commit();
            connection.getEntityManager().close();

            return true;
        }
        return false;
    }

    public boolean doesUserExist(String username) {
        Long count = (Long) connection.getEntityManager().createQuery("Select count(p) from PersonEntity p " +
                "where p.username =:username").setParameter("username", username).getSingleResult();

        return ( ( count.equals(0L) ) ? false : true);
    }

    public String getUsernameById(int id) {
        TypedQuery <PersonEntity> query = connection.getEntityManager().createQuery("Select p from PersonEntity p " +
                "Where p.idPerson =:id", PersonEntity.class);
        query.setParameter("id", id);

        PersonEntity person = query.getSingleResult();

        return person.getUsername();
    }

    public int getIdByUsername(String username) {
        TypedQuery <PersonEntity> query = connection.getEntityManager().createQuery("Select p from PersonEntity p " +
                "Where p.username =:username", PersonEntity.class);
        query.setParameter("username", username);

        PersonEntity person = query.getSingleResult();

        return person.getIdPerson();
    }

    public List<PersonEntity> getAllCompetitorsByStage(String stageName) {
        StageDao stage = new StageDao();
        if(stage.doesStageExist(stageName)) {

            int idStage = stage.getIdByName(stageName);
            TypedQuery<PersonEntity> query = connection.getEntityManager().createQuery("Select p from PersonEntity p " +
                    ", PersonStageEntity ps WHERE p.idPerson = ps.idPerson and ps.idStage =:id  and ps.verified =:verif " +
                    " ORDER BY ps.score DESC", PersonEntity.class);
            query.setParameter("id", idStage);
            query.setParameter("verif", (byte) 1);

            List<PersonEntity> personEntities = query.getResultList();
            return personEntities;
        }
        return null;
    }
}
