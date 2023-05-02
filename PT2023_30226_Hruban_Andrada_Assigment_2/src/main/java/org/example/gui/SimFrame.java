package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SimFrame {
    private JFrame frame;
    private JTextField simulationTimeTF;
    private JTextField strategyTF;
    private JTextField maxProcessingTimeTF;
    private JTextField minProcessingTimeTF;
    private JTextField maxArrivalTimeTF;
    private JTextField minArrivalTimeTF;
    private JTextField noQueuesTF;
    private JTextField noClientsTF;
    private JButton btnRun;
    private JLabel lblSimNotStarted;
    private JTextArea textArea;
    private JLabel peakTime;
    private JLabel serviceTime;
    private JLabel waitingTime;

    public SimFrame() {
        initialize();
    }

    public JFrame getFrame() {
        return frame;
    }

    public JLabel getLblSimNotStarted() {
        return lblSimNotStarted;
    }

    public JTextField getSimulationTimeTF() {
        return simulationTimeTF;
    }

    public JTextField getStrategyTF() {
        return strategyTF;
    }

    public JTextField getMaxProcessingTimeTF() {
        return maxProcessingTimeTF;
    }

    public JTextField getMinProcessingTimeTF() {
        return minProcessingTimeTF;
    }

    public JTextField getMaxArrivalTimeTF() {
        return maxArrivalTimeTF;
    }

    public JTextField getMinArrivalTimeTF() {
        return minArrivalTimeTF;
    }

    public JTextField getNoQueuesTF() {
        return noQueuesTF;
    }

    public JTextField getNoClientsTF() {
        return noClientsTF;
    }

    public JButton getBtnRun() {
        return btnRun;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JLabel getServiceTime() {
        return serviceTime;
    }

    public JLabel getWaitingTime() {
        return waitingTime;
    }

    public JLabel getPeakTime() {
        return peakTime;
    }

    public void ButtonsListener(ActionListener addListener) {
        getBtnRun().addActionListener(addListener);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 650, 615);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridBagLayout());

        addComponent(frame, new JLabel("Simulation Time:"), 0, 0);
        simulationTimeTF = createTextField();
        addComponent(frame, simulationTimeTF, 1, 0);
        setFixedSize(simulationTimeTF);

        addComponent(frame, new JLabel("Strategy (TIME/QUEUE):"), 2, 0);
        strategyTF = createTextField();
        addComponent(frame, strategyTF, 3, 0);
        setFixedSize(strategyTF);

        addComponent(frame, new JLabel("Max Service Time:"), 0, 1);
        maxProcessingTimeTF = createTextField();
        addComponent(frame, maxProcessingTimeTF, 1, 1);
        setFixedSize(maxProcessingTimeTF);

        addComponent(frame, new JLabel("Min Service Time:"), 2, 1);
        minProcessingTimeTF = createTextField();
        addComponent(frame, minProcessingTimeTF, 3, 1);
        setFixedSize(minProcessingTimeTF);

        addComponent(frame, new JLabel("Max Arrival Time:"), 0, 2);
        maxArrivalTimeTF = createTextField();
        addComponent(frame, maxArrivalTimeTF, 1, 2);
        setFixedSize(maxArrivalTimeTF);

        addComponent(frame, new JLabel("Min Arrival Time:"), 2, 2);
        minArrivalTimeTF = createTextField();
        addComponent(frame, minArrivalTimeTF, 3, 2);
        setFixedSize(minArrivalTimeTF);
        addComponent(frame, new JLabel("Number of Queues:"), 0, 3);
        noQueuesTF = createTextField();
        addComponent(frame, noQueuesTF, 1, 3);
        setFixedSize(noQueuesTF);

        addComponent(frame, new JLabel("Number of Clients:"), 2, 3);
        noClientsTF = createTextField();
        addComponent(frame, noClientsTF, 3, 3);
        setFixedSize(noClientsTF);

        btnRun = new JButton("Run!");
        addComponent(frame, btnRun, 1, 4);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        GridBagConstraints scrollConstraints = createConstraints(0, 5, 4, 1);
        scrollConstraints.fill = GridBagConstraints.BOTH;
        scrollConstraints.weightx = 1;
        scrollConstraints.weighty = 1;
        frame.getContentPane().add(scrollPane, scrollConstraints);

        addComponent(frame, new JLabel("Average waiting time:"), 0, 6);
        waitingTime = new JLabel("AVG");
        addComponent(frame, waitingTime, 1, 6);

        lblSimNotStarted = new JLabel("Simulation not started");
        addComponent(frame, lblSimNotStarted, 2, 4);
        addComponent(frame, new JLabel("Average Service Time:"), 2, 6);
        serviceTime = new JLabel("AVG");
        addComponent(frame, serviceTime, 3, 6);
        addComponent(frame, new JLabel("Peak Time:"), 0, 7);
        peakTime = new JLabel("PEAK");
        addComponent(frame, peakTime, 1, 7);
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setColumns(10);
        return textField;
    }

    private void addComponent(Container container, Component component, int gridx, int gridy) {
        GridBagConstraints constraints = createConstraints(gridx, gridy, 1, 1);
        container.add(component, constraints);
    }

    private GridBagConstraints createConstraints(int gridx, int gridy, int gridwidth, int gridheight) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;
        return constraints;
    }

    private void setFixedSize(JTextField textField) {
        Dimension preferredSize = textField.getPreferredSize();
        textField.setMaximumSize(preferredSize);
    }
}
