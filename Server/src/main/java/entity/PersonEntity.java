package entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Entity
@Table(name = "person", schema = "lab_mip")
@Transactional
public class PersonEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_person")
    private int idPerson;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "role")
    private String role;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "id_team")
    private Integer idTeam;


    public PersonEntity() {
    }

    public int getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Integer idTeam) {
        this.idTeam = idTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonEntity that = (PersonEntity) o;

        if (idPerson != that.idPerson) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (idTeam != null ? !idTeam.equals(that.idTeam) : that.idTeam != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idPerson;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (idTeam != null ? idTeam.hashCode() : 0);
        return result;
    }
}
