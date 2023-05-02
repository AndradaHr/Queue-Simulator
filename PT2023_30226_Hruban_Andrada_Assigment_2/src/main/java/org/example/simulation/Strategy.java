package org.example.simulation;
import org.example.simulation.Client;
import org.example.simulation.Queue;

import java.util.List;

public interface Strategy {
    public void addClient(List<Queue> queues, Client c) throws InterruptedException;
}
