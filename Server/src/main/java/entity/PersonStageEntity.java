package entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "person_stage", schema = "lab_mip")
@IdClass(PersonStageEntityPK.class)
public class PersonStageEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_person")
    private int idPerson;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_stage")
    private int idStage;
    @Basic
    @Column(name = "score")
    private BigDecimal score;
    @Basic
    @Column(name = "verified")
    private Byte verified;

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

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Byte getVerified() {
        return verified;
    }

    public void setVerified(Byte verified) {
        this.verified = verified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonStageEntity that = (PersonStageEntity) o;

        if (idPerson != that.idPerson) return false;
        if (idStage != that.idStage) return false;
        if (score != null ? !score.equals(that.score) : that.score != null) return false;
        if (verified != null ? !verified.equals(that.verified) : that.verified != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idPerson;
        result = 31 * result + idStage;
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (verified != null ? verified.hashCode() : 0);
        return result;
    }
}
