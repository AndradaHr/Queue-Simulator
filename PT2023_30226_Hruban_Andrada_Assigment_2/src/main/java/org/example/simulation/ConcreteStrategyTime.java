package org.example.simulation;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {

    public void addClient(List<Queue> queues, Client c) {
        int minTime = 1000;
        int index = 0;
        Queue coada=queues.get(0);
        for (Queue q: queues) {
            if (q.getWaitingPeriod() <= minTime) {
                index = q.getQueueNo();
                minTime = q.getWaitingPeriod();
                coada=q;
            }
        }
        if (index == 0) {
            System.out.println("Nu s-au gasit cozi");
        } else {
            coada.addClient(c);
            System.out.println("Client in coada "+ (index-1));
        }

    }
}
