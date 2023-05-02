package org.example.simulation;
public class Client implements Comparable<Client>{
    private int arrivalTime;
    private int serviceTime;
    private int number;
    private int finishTime;

    public Client(int arrivalTime, int servingTime, int number) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = servingTime;
        this.number = number;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getNumber() {
        return number;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public int compareTo(Client another) {
        if (this.getArrivalTime() < another.getArrivalTime()){
            return -1;
        } else if (this.getArrivalTime() == another.getArrivalTime()){
            return 0;
        } else {
            return 1;
        }
    }
}
