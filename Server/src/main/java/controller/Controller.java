package controller;

import database.dao.*;
import entity.*;
import network.Packet;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private String currentUser;


    public boolean registerAccount(Packet packet) {
        PersonEntity person = new PersonEntity();
        PersonDao personDao = new PersonDao();

        person.setName(packet.getName());
        person.setUsername(packet.getUsername());
        person.setRole(packet.getRole());
        if(personDao.isUsernameTaken(person.getUsername()))
            return false;
        else
            personDao.create(person);

        return true;
    }

    public boolean loginAccount(Packet packet) {
        PersonDao personDao = new PersonDao();

        if(personDao.isUsernameTaken(packet.getUsername())) {
            this.currentUser = packet.getUsername();
            return true;
        }
        return false;
    }

    public String getCurrentUser() { return this.getCurrentUser(); }

    public boolean isAdmin(String username) {
        PersonDao personDao = new PersonDao();

        if(personDao.isAdmin(username))
            return true;
        return false;
    }

    public List<PersonEntity> getPersonRanking() {
        PersonDao person = new PersonDao();
        List<PersonEntity> ranking = person.getRanking();
        return ranking;
    }

    public String getScoreByUsername(String username) {
        PersonStageDao personDao = new PersonStageDao();
        String score = personDao.getScoreByUsername(username);
        return score;
    }

    public List<TeamEntity> getTeamRanking() {
        TeamDao team = new TeamDao();
        List<TeamEntity> ranking = team.getTeamRanking();
        return ranking;
    }

    public String getScoreByTeamName(String teamName) {
        TeamDao team = new TeamDao();
        int idTeam = team.getIdByName(teamName);

        PersonStageDao person = new PersonStageDao();
        String score = person.getScoreByTeamId(idTeam);
        return score;
    }

    public boolean setScore(String stage, String score) {
        PersonStageDao personStage = new PersonStageDao();
        if(personStage.setScore(stage, currentUser, score))
            return true;

        return false;
    }

    public boolean addCompetitorToTeam(String username, String teamName) {
        PersonDao personDao = new PersonDao();
        if(personDao.doesUserExist(username)) {
            TeamDao team = new TeamDao();
            if (team.isNotFull(teamName)) {
                team.addUserToTeam(username, teamName);
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean enrollCompetitor(String username, String stageName) {

        PersonDao person = new PersonDao();
        if(person.doesUserExist(username)) {

            StageDao stage = new StageDao();
            if(stage.doesStageExist(stageName)) {

                try {
                    PersonStageDao personStageDao = new PersonStageDao();
                    personStageDao.enrollCompetitor(username, stageName);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    public void createTeam(String teamName) {
        TeamDao teamDao = new TeamDao();

        TeamEntity team = new TeamEntity();
        team.setTeamName(teamName);

        teamDao.create(team);

        return;
    }

    public void createStage(String stageName) {
        StageDao stageDao = new StageDao();

        StageEntity stage = new StageEntity();
        stage.setStageName(stageName);
        stage.setIsFinished(Byte.parseByte("0"));

        stageDao.create(stage);

        return;
    }

    public boolean deleteTeam(String teamName) {
        TeamDao team = new TeamDao();

        if(team.deleteTeam(teamName))
            return true;

        return false;
    }

    public boolean deleteUser(String username) {
        PersonDao person = new PersonDao();

        if(person.deleteUser(username))
            return true;

        return false;
    }

    public boolean deleteStage(String stageName) {
        StageDao stage = new StageDao();

        if(stage.deleteStage(stageName))
            return true;

        return false;
    }

    public List<PersonStageEntity> getAllUnverifiedPersons() {
        PersonStageDao personStageDao = new PersonStageDao();

        List<PersonStageEntity> unverifiedPersons = personStageDao.getScoreBoard();

        return unverifiedPersons;
    }

    public String getUsernameById(int id) {
        PersonDao person = new PersonDao();

        String username = person.getUsernameById(id);

        return username;
    }

    public String getStageNameById(int id) {
        StageDao stage = new StageDao();

        String stageName = stage.getStageNameById(id);

        return stageName;
    }

    public boolean verifyCompetitor(String username, String stageName) {
        PersonDao personDao = new PersonDao();
        int idPerson = personDao.getIdByUsername(username);

        StageDao stageDao = new StageDao();
        int idStage = stageDao.getIdByName(stageName);

        PersonStageDao personStageDao = new PersonStageDao();
        if(personStageDao.verifyCompetitor(idStage, idPerson))
            return true;

        return false;
    }

    public List<PersonEntity> getCompetitorsByStage(String stageName) {
        PersonDao personDao = new PersonDao();
        List<PersonEntity> personEntities = personDao.getAllCompetitorsByStage(stageName);
        return personEntities;
    }

    public String getScoreByUsernameAndStage(String username, String stageName) {
        PersonDao person = new PersonDao();
        int idPerson = person.getIdByUsername(username);

        StageDao stage = new StageDao();
        int idStage = stage.getIdByName(stageName);

        PersonStageDao personStageDao = new PersonStageDao();
        return personStageDao.getScoreByID(idPerson, idStage);
    }

    public void sendNotification (String username, String stageName) {
        PersonDao personDao = new PersonDao();
        int idPerson = personDao.getIdByUsername(username);

        StageDao stageDao = new StageDao();
        int idStage = stageDao.getIdByName(stageName);

        NotificationsEntity notification = new NotificationsEntity();

        notification.setIdPerson(idPerson);
        notification.setIdStage(idStage);

        NotificationsDao notificationsDao = new NotificationsDao();
        notificationsDao.create(notification);

        return ;
    }

    public List<NotificationsEntity> getCurrentUserNotifications() {

        PersonDao personDao = new PersonDao();
        int idPerson = personDao.getIdByUsername(currentUser);

        NotificationsDao notificationsDao = new NotificationsDao();
        List<NotificationsEntity> notificationsEntities = notificationsDao.getNotificationsByID(idPerson);

        return notificationsEntities;
    }

    public void deleteNotification(String stageName) {

        PersonDao personDao = new PersonDao();
        int idPerson =personDao.getIdByUsername(currentUser);

        StageDao stageDao = new StageDao();
        int idStage = stageDao.getIdByName(stageName);

        NotificationsDao notificationsDao = new NotificationsDao();
        notificationsDao.deleteNotification(idPerson, idStage);
    }

}
