package gRPCProject.domain;

import java.io.Serializable;

public class Ticket extends Entity<Integer>implements Serializable {
    private Integer idShow;
    private String nameOfCostumer;
    private Integer noOfSeats;

    public Ticket(Integer id, Integer idShow, String nameOfCostumer, Integer noOfSeats) {
        super(id);
        this.idShow = idShow;
        this.nameOfCostumer = nameOfCostumer;
        this.noOfSeats = noOfSeats;
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

    public Integer getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(Integer noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + getId() +
                ", idShow=" + idShow +
                ", nameOfCostumer='" + nameOfCostumer +
                ", noOfSeats=" + noOfSeats + '\'' +
                '}';
    }
}
