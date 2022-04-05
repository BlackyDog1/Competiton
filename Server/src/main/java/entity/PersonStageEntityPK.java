package entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class PersonStageEntityPK implements Serializable {
    @Column(name = "id_person")
    @Id
    private int idPerson;
    @Column(name = "id_stage")
    @Id
    private int idStage;

    public int getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public int getIdStage() {
        return idStage;
    }

    public void setIdStage(int idStage) {
        this.idStage = idStage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonStageEntityPK that = (PersonStageEntityPK) o;

        if (idPerson != that.idPerson) return false;
        if (idStage != that.idStage) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idPerson;
        result = 31 * result + idStage;
        return result;
    }
}
