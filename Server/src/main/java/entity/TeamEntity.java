package entity;

import javax.persistence.*;

@Entity
@Table(name = "team", schema = "lab_mip")
public class TeamEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_team")
    private int idTeam;
    @Basic
    @Column(name = "team_name")
    private String teamName;

    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamEntity team = (TeamEntity) o;

        if (idTeam != team.idTeam) return false;
        if (teamName != null ? !teamName.equals(team.teamName) : team.teamName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idTeam;
        result = 31 * result + (teamName != null ? teamName.hashCode() : 0);
        return result;
    }
}
