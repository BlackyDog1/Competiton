package entity;

import javax.persistence.*;




@Entity
@Table(name = "stage", schema = "lab_mip")
public class StageEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_stage")
    private int idStage;
    @Basic
    @Column(name = "stage_name")
    private String stageName;
    @Basic
    @Column(name = "is_finished")
    private Byte isFinished;

    public int getIdStage() {
        return idStage;
    }

    public void setIdStage(int idStage) {
        this.idStage = idStage;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Byte getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Byte isFinished) {
        this.isFinished = isFinished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StageEntity that = (StageEntity) o;

        if (idStage != that.idStage) return false;
        if (stageName != null ? !stageName.equals(that.stageName) : that.stageName != null) return false;
        if (isFinished != null ? !isFinished.equals(that.isFinished) : that.isFinished != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idStage;
        result = 31 * result + (stageName != null ? stageName.hashCode() : 0);
        result = 31 * result + (isFinished != null ? isFinished.hashCode() : 0);
        return result;
    }
}
