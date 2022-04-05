package entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "notifications", schema = "lab_mip")
public class NotificationsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_notification")
    private int idNotification;
    @Basic
    @Column(name = "id_person")
    private int idPerson;
    @Basic
    @Column(name = "id_stage")
    private int idStage;

    public void setIdStage(int idStage) {
        this.idStage = idStage;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public void setIdNotification(int idNotification) {
        this.idNotification = idNotification;
    }

    public int getIdStage() {
        return idStage;
    }

    public int getIdPerson() {
        return idPerson;
    }

    public int getIdNotification() {
        return idNotification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationsEntity that = (NotificationsEntity) o;
        return idNotification == that.idNotification && idPerson == that.idPerson && idStage == that.idStage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNotification, idPerson, idStage);
    }
}

