package org.example.simulation;
import org.example.simulation.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class Queue implements Runnable{
    private BlockingQueue<Client> tasks;
    private AtomicInteger waitingPeriod;
    private double averageWaitingTime;
    private int queueNo;

    public Queue(int no, int capacity) {
        tasks = new ArrayBlockingQueue<Client>(capacity);
        waitingPeriod = new AtomicInteger();
        averageWaitingTime = 0.0;
        queueNo = no;
    }

    public void addClient(Client newClient) {
        tasks.add(newClient);
        waitingPeriod.addAndGet(newClient.getServiceTime());
          }
    public void run() {
        while(true) {
            Client nextClient = tasks.peek();
            if (nextClient== null) {
                               try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
            } else {
                for (int waitingTime = 1; waitingTime <= nextClient.getServiceTime(); waitingTime++) {
                    try {
                        //Thread.sleep(1000);
                        nextClient.setServiceTime(nextClient.getServiceTime() - 1);
                        if(nextClient.getServiceTime()!=0)
                        Thread.sleep(1000);
                        if(nextClient.getServiceTime()==0) {
                            waitingPeriod.decrementAndGet();
                        tasks.poll();
                        Thread.sleep(1000);
                            }
                    } catch (InterruptedException e) {}
                }
            }
        }
    }

    public BlockingQueue<Client> getQueue() {
        return tasks;
    }

    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }

    public int getQueueNo() {
        return queueNo;
    }

    public List<Client> getClients() {
        List<Client> clients = new ArrayList<>(tasks);
        return clients;
    }


}
