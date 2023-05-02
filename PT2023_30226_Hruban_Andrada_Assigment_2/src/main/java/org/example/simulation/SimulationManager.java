package org.example.simulation;
import org.example.logger.EventLogger;
import org.example.gui.SimFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;


public class SimulationManager implements Runnable {
    public int timeLimit = 200;
    public int maxServiceTime = 10;
    public int minServiceTime = 2;
    public int maxArrivalTime = 60;
    public int minArrivalTime = 2;
    public int numberOfQueues = 3;
    public int numberOfClients = 100;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
    public boolean start = false;

    private int peakTime = 0;
    private int peakNoClients = 0;
    private SimFrame frame;
    private Scheduler scheduler;
    public List<Client> generatedClients;
    private List<Client> onQueueClients;
    private EventLogger eventLogger;
    private int totalServiceTime=0;


    public SimulationManager() {
        onQueueClients = new ArrayList<Client>();
        scheduler = new Scheduler(numberOfQueues, 30);
        scheduler.changeStrategy(selectionPolicy);
        frame = new SimFrame();
        eventLogger = new EventLogger("events.txt");
    }

    private void generateNRandomClients() {
        for (int i = 1; i <= numberOfClients; i++) {
            Random rand = new Random();
            int  serviceTime = rand.nextInt(maxServiceTime - 1) + (minServiceTime);
            totalServiceTime+=serviceTime;
            int arrivalTime = rand.nextInt(maxArrivalTime - 1) + (minArrivalTime);
            generatedClients.add(new Client(arrivalTime, serviceTime, i));
        }
        Collections.sort(generatedClients);
    }

    public void run() {
        int servedClients = 0;
        System.out.println("Simulation started...");
        frame.getFrame().setVisible(true);
        frame.ButtonsListener(new Simu());

        while (!start) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) { }
        }

        while (start) {
            int currentTime = 0;

            for (int i = 0; i < scheduler.getQueues().size(); i++) {
                new Thread(scheduler.getQueues().get(i)).start();
            }

            while (currentTime <= timeLimit) {
                // Log the current time
                eventLogger.logEvent("Time " + currentTime);
                frame.getLblSimNotStarted().setText("" + currentTime);
                frame.getTextArea().append("Time "+currentTime+"\n");

                for(Client client: generatedClients) {
                    if (client.getArrivalTime() == currentTime) {
                        try {
                            scheduler.dispatchClient(client);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        int queueNo = scheduler.getQueuesNo(client);
                        if (scheduler.getQueue(queueNo) != null) {
                            client.setFinishTime(scheduler.getQueue(queueNo).getWaitingPeriod() + client.getServiceTime()+ client.getArrivalTime());
                            onQueueClients.add(client);

                        }
                    }
                }

                if (!generatedClients.isEmpty()) {
                    while (generatedClients.get(0).getArrivalTime() == currentTime) {
                        generatedClients.remove(generatedClients.get(0));
                        if(generatedClients.isEmpty()) {
                            break;
                        }
                    }
                }
                // Log the waiting clients
                StringBuilder waitingClients = new StringBuilder();
                for (Client client : generatedClients) {
                    waitingClients.append("(")
                            .append(client.getNumber()).append(",")
                            .append(client.getServiceTime()).append(",")
                            .append(client.getArrivalTime()).append("); ");
                }
                eventLogger.logEvent("Waiting clients: " + waitingClients.toString().trim());
                frame.getTextArea().append("Waiting clients: " + waitingClients.toString().trim());
                frame.getTextArea().append("\n");
                // Log the status of the queues

                for (int i = 0; i <  numberOfQueues; i++) {
                    Queue queue = scheduler.getQueues().get(i);
                    StringBuilder queueClients = new StringBuilder();

                    for (Client client : queue.getClients()) {
                        queueClients.append("(")
                                .append(client.getNumber()).append(",")
                                .append(client.getServiceTime()).append(",")
                                .append(client.getArrivalTime()).append("); ");
                    }

                    eventLogger.logEvent("Queue " + (i + 1) + ": " + (queueClients.length() > 0 ? queueClients.toString().trim() : "closed"));
                    frame.getTextArea().append("Queue " + (i + 1) + ": " + (queueClients.length() > 0 ? queueClients.toString().trim() : "closed"));
                    frame.getTextArea().append("\n");
                }

                if (!onQueueClients.isEmpty()) {
                    int i = 0;
                    while (i < onQueueClients.size()) {
                        if (onQueueClients.get(i).getFinishTime() == currentTime) {
                            onQueueClients.remove(i);
                            servedClients++;
                            } else {
                            i++;
                        }
                    }
                }
                if (servedClients == numberOfClients && scheduler.allQueuesEmpty()) {
                    start = false; // Stop the simulation
                    break;
                }

                if (onQueueClients.size() > peakNoClients) {
                    peakNoClients = onQueueClients.size();
                    peakTime = currentTime;
                }

                frame.getServiceTime().setText(""+computeAverageServiceTime());
                frame.getWaitingTime().setText(""+computeAverageWaitingTime());
                frame.getPeakTime().setText("" + peakTime);

               // currentTime++;
                try {
                    Thread.sleep(1000);
                } catch (Exception e) { }
                currentTime++;
            }
            start = false;
        }

        System.out.println("Simulation ended");

        // Calculate the average waiting time and log it
        double averageWaitingTime = computeAverageWaitingTime();
        eventLogger.logEvent("Average waiting time: " + averageWaitingTime);
        double averageServiceTime = computeAverageServiceTime();
        eventLogger.logEvent("Average serving time: " + averageServiceTime);
    }

    public double computeAverageWaitingTime() {
        double totalWaitingTime = 0;
      /*  for (Client client : onQueueClients) {
            int queueNo = scheduler.getQueuesNo(client);
            Queue queue = scheduler.getQueue(queueNo);
            int waitingTime = (int) (client.getFinishTime() - client.getArrivalTime() - client.getServiceTime());
            totalWaitingTime += waitingTime;
        }*/
        totalWaitingTime = scheduler.getTotalWaitingTime();

        return totalWaitingTime / numberOfClients;
    }
    public double computeAverageServiceTime() {
        return totalServiceTime / numberOfClients;
    }
    private class Simu implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                start = false;
                String reader=frame.getNoClientsTF().getText();
                numberOfClients = Integer.parseInt(reader);
                reader=frame.getNoQueuesTF().getText();
                numberOfQueues = Integer.parseInt(reader);
                scheduler.updateNumberOfQueues(numberOfQueues); // Add this line
                reader=frame.getMinProcessingTimeTF() .getText();
                minServiceTime = Integer.parseInt(reader);
                reader=frame.getMaxProcessingTimeTF().getText();
                maxServiceTime = Integer.parseInt(reader);
                reader=frame.getMinArrivalTimeTF() .getText();
                minArrivalTime = Integer.parseInt(reader);
                reader=frame.getMaxArrivalTimeTF().getText();
                maxArrivalTime = Integer.parseInt(reader);
                reader=frame.getSimulationTimeTF().getText();
                timeLimit = Integer.parseInt(reader);
                reader=frame.getStrategyTF().getText();

                if (reader.equals("TIME")) {
                    selectionPolicy = SelectionPolicy.SHORTEST_TIME;
                } else if (reader.equals("QUEUE")){
                    selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
                } else {
                    JOptionPane.showMessageDialog(null,"Policy does not exist! ","", 0);
                }

                if (numberOfQueues > 20) {
                    JOptionPane.showMessageDialog(null,"Max 20 queues! ","", 0);
                }
                else if(numberOfClients > 1000){
                    JOptionPane.showMessageDialog(null,"Max 1000 clients! ","", 0);
                }
                else
                {
                    generatedClients = new ArrayList<Client>();
                    generateNRandomClients();
                    for (Client c: generatedClients) {
                        System.out.println(c.toString());
                    }

                    start = true;
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,"Wrong data! ","", 2);
            }

        }
    }
    public static void main(String[] args) {
        SimulationManager simulationManager = new SimulationManager();
        Thread t = new Thread(simulationManager);
        t.start();
    }

}
