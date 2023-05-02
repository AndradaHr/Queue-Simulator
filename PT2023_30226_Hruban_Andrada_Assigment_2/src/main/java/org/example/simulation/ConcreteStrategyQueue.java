package org.example.simulation;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {

    public void addClient(List<Queue> queues, Client c) {
        int minQueue = 100;
        int index = 0;
        for (Queue q: queues) {
            if (q.getQueue().size() <= minQueue) {
                index = q.getQueueNo();
                minQueue = q.getQueue().size();
            }
        }
        if (index == 0) {
            System.out.println("Nu s-au gasit cozi");
        } else {
            queues.get(index - 1).addClient(c);
        }
    }
}

