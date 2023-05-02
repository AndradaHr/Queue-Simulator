package org.example.simulation;
import org.example.logger.EventLogger;

import java.util.*;

public class Scheduler {
    private List<org.example.simulation.Queue> queues;
    private int maxNoQueues;
    private int maxClientsPerQueue;
    private Strategy strategy;

    private EventLogger eventLogger;

    public Scheduler(int maxNoQueues, int maxClientsPerQueue) {
        this.maxClientsPerQueue = maxClientsPerQueue;
        this. maxNoQueues = maxNoQueues;
        queues = new ArrayList<org.example.simulation.Queue>();
        for (int i = 1; i < maxNoQueues; i++) {
            queues.add(new org.example.simulation.Queue(i , maxClientsPerQueue));
        }
    }


    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ConcreteStrategyTime();
        }
    }

    public void dispatchClient(Client c) throws InterruptedException {
        strategy.addClient(this.queues, c);
    }

    public List<org.example.simulation.Queue> getQueues() {
        return queues;
    }

    public int getQueuesNo(Client c) {
        for (org.example.simulation.Queue q: queues) {
            for (Client client: q.getQueue()) {
                if (client.getNumber() == c.getNumber()) {
                    System.out.println("Queue n "+q.getQueueNo());
                    return q.getQueueNo();

                }
            }
        }
        return 1;
    }

    public org.example.simulation.Queue getQueue(int queueNo) {
        for (org.example.simulation.Queue q: queues) {
            if (q.getQueueNo() == queueNo) {
                return q;
            }
        }
        return null;
    }
    public int getTotalWaitingTime() {
        int totalWaitingTime = 0;
        for (org.example.simulation.Queue q : queues) {
            totalWaitingTime += q.getWaitingPeriod();
        }

        return totalWaitingTime;
    }
    public void updateNumberOfQueues(int numberOfQueues) {
        this.queues = new ArrayList<org.example.simulation.Queue>(numberOfQueues);
        for (int i = 0; i < numberOfQueues; i++) {
            this.queues.add(new Queue(i + 1, 30)); // Assuming 30 is the initial capacity for the queues
        }
    }
    public boolean allQueuesEmpty() {
        for (Queue queue : queues) {
            if (!queue.getClients().isEmpty()) {
                return false;
            }
        }
        return true;
    }

}
