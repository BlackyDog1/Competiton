package database.dao;

import database.DatabaseConnection;
import entity.PersonEntity;
import entity.StageEntity;
import entity.TeamEntity;
import org.hibernate.Session;

import javax.management.relation.Role;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

public class TeamDao implements DaoI<TeamEntity> {
    DatabaseConnection connection = new DatabaseConnection();

    @Override
    public TeamEntity get(int id) {
        return connection.getEntityManager().find(TeamEntity.class, id);
    }

    @Override
    public List<TeamEntity> getAll() {
        TypedQuery <TeamEntity> query = connection.getEntityManager().createQuery("Select t From TeamEntity t", TeamEntity.class);
        return query.getResultList();
    }

    @Override
    public void create(TeamEntity teamEntity) {
        connection.executeTransaction(entityManager -> entityManager.persist(teamEntity));
    }

    public List<TeamEntity> getTeamRanking() {
        TypedQuery <TeamEntity> query = connection.getEntityManager().createQuery("Select t From TeamEntity t, " +
                "PersonEntity p, PersonStageEntity ps Where t.idTeam = p.idTeam And p.idPerson = ps.idPerson " +
                "group by t ORDER BY SUM(ps.score) DESC", TeamEntity.class);


        return query.getResultList();
    }

    public int getIdByName(String teamName) {
        TypedQuery <TeamEntity> query = connection.getEntityManager().createQuery("Select t from TeamEntity t " +
                "Where t.teamName =:name", TeamEntity.class);
        query.setParameter("name", teamName);

        TeamEntity team = query.getSingleResult();

        return team.getIdTeam();
    }

    public boolean isNotFull(String teamName) { //verifies if the team already has 5 members

        if(doesTeamExist(teamName)) {

            TypedQuery<TeamEntity> query = connection.getEntityManager().createQuery("Select t From TeamEntity t where t.teamName =:name", TeamEntity.class);
            query.setParameter("name", teamName);

            TeamEntity team = query.getSingleResult();
            int idTeam = team.getIdTeam();
            Query query2 = connection.getEntityManager().createQuery("Select count(p.idTeam) from PersonEntity p, TeamEntity t where t.idTeam = p.idTeam" +
                    " and t.idTeam =:id");
            query2.setParameter("id", idTeam);

            int result = ((Long) query2.getSingleResult()).intValue();
            if (result < 5)
                return true;
            return false;
        }

        return false;
    }

    @Transactional
    public void addUserToTeam(String username, String teamName) {
        TypedQuery<TeamEntity> query = connection.getEntityManager().createQuery("Select  t From TeamEntity t where t.teamName =:name1", TeamEntity.class);
        query.setParameter("name1", teamName);

        TeamEntity team = query.getSingleResult();
        int idTeam = team.getIdTeam();

        TypedQuery<PersonEntity> query2 = connection.getEntityManager().createQuery("Select p From PersonEntity p where p.username =:name2", PersonEntity.class);
        query2.setParameter("name2", username);

        PersonEntity person = query2.getSingleResult();

        int idPerson = person.getIdPerson();

        System.out.println(idTeam);
        System.out.println(idPerson);

        connection.getEntityManager().getTransaction().begin();

        Query query3 = connection.getEntityManager().createQuery("UPDATE PersonEntity set idTeam =:id where idPerson =:idPerson");
        query3.setParameter("id", idTeam);
        query3.setParameter("idPerson", idPerson);
        query3.executeUpdate();

        connection.getEntityManager().getTransaction().commit();
        connection.getEntityManager().close();


        return;

    }

    public boolean deleteTeam(String teamName) {
        if(doesTeamExist(teamName)) {
            TypedQuery<TeamEntity> query = connection.getEntityManager().createQuery("Select  t From TeamEntity t where t.teamName =:name1", TeamEntity.class);
            query.setParameter("name1", teamName);

            TeamEntity team = query.getSingleResult();
            int idTeam = team.getIdTeam();

            connection.getEntityManager().getTransaction().begin();

            Query query2 = connection.getEntityManager().createQuery("DELETE FROM TeamEntity t Where t.idTeam =:idTeam");
            query2.setParameter("idTeam", idTeam);
            query2.executeUpdate();

            connection.getEntityManager().getTransaction().commit();
            connection.getEntityManager().close();
            return true;
        }
        return false;
    }

    public boolean doesTeamExist(String teamName) {
        Long count = (Long) connection.getEntityManager().createQuery("Select count(t) from TeamEntity t " +
                "where t.teamName =:teamName").setParameter("teamName", teamName).getSingleResult();

        return ( ( count.equals(0L) ) ? false : true);
    }

}
