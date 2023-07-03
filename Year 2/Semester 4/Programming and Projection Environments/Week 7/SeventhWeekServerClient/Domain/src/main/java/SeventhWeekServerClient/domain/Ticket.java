package SeventhWeekServerClient.domain;

import java.io.Serializable;

public class Ticket extends Entity<Integer>implements Serializable {
    private Integer idShow;
    private String nameOfCostumer;

    public Ticket(Integer id, Integer idShow, String nameOfCostumer) {
        super(id);
        this.idShow = idShow;
        this.nameOfCostumer = nameOfCostumer;
    }

    public Integer getIdShow() {
        return idShow;
    }

    public void setIdShow(Integer idShow) {
        this.idShow = idShow;
    }

    public String getNameOfCostumer() {
        return nameOfCostumer;
    }

    public void setNameOfCostumer(String nameOfCostumer) {
        this.nameOfCostumer = nameOfCostumer;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + getId() +
                ", idShow=" + idShow +
                ", nameOfCostumer='" + nameOfCostumer + '\'' +
                '}';
    }
}
