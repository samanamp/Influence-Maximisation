/*
 * Influence Maximization -- Influence Maximization
 *
 * Copyright (c) 2014 by Saman Abdolmohammadpour.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.samanamp.algorithms;

import com.samanamp.Node;
import com.samanamp.simulation.SimulationEngine;
import org.apache.commons.collections4.iterators.LoopingIterator;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import java.util.Iterator;
import java.util.LinkedList;


public class GreedySelectionAlgorithm implements Algorithm {

    private DirectedWeightedMultigraph<Node, DefaultWeightedEdge> graph;
    private SimulationEngine simulator;
    private int maxCost;
    private int maxTime;
    private int runs;
    private LinkedList<Node> selectedNodes;

    public GreedySelectionAlgorithm(DirectedWeightedMultigraph graph, Class<? extends SimulationEngine> SimulationClass) throws IllegalAccessException, InstantiationException {
        this.graph = graph;
        this.simulator = SimulationClass.newInstance();
        simulator.setGraph(graph);
        selectedNodes = new LinkedList<Node>();
    }

    public void runAlgoAndSimulation(int maxCost, int maxTime, int runs) {
        this.maxCost = maxCost;
        this.maxTime = maxTime;
        this.runs = runs;


        selectNodes();
        System.out.println("#nodes selected: " + selectedNodes);
        runSimulation();
    }

    private void selectNodes() {
        int currentBudget = maxCost;
        Iterator<Node> nodes;
        Iterator<Node> selectedNodesIterator;
        Node currentNode;
        Node highestNode;
        Node dummyNode = new Node("Dummy");
        dummyNode.cost = 1;
        dummyNode.reward = 0;
        int count;
        DirectedWeightedMultigraph<Node, DefaultWeightedEdge> tempGraph = graph;
        long time1;
        long time2;
        long time3;
        long time4;
        System.out.println("Starting greedy algorithm...");
        while (currentBudget > 0) {
            nodes = graph.vertexSet().iterator();
            highestNode = dummyNode;

            count = 0;
            time1 = System.currentTimeMillis();
            while (nodes.hasNext()) {
                count++;
                currentNode = nodes.next();
                System.out.print("\n" + count);
                if (selectedNodes.contains(currentNode) || currentNode.active) continue;
                if (currentBudget < currentNode.cost || currentNode.cost == 0) continue;


                System.out.print(".");

                if (selectedNodes.size() > 0) {
                    selectedNodesIterator = selectedNodes.iterator();
                    while (selectedNodesIterator.hasNext())
                        simulator.sigmaOfNode(selectedNodesIterator.next(), maxTime);

                }

                currentNode.reward = simulator.sigmaOfNode(currentNode, maxTime);
                //System.out.print("S");
                if (currentNode.reward > highestNode.reward) {
                    //System.out.print(".");
                    highestNode = currentNode;
                    //System.out.print("H");
                }

                //System.out.print("F:" + (time2 - time1) + ":" + (time3 - time2) + ":" + (time4 - time3) + ":" + (time4 - time1) + "+");
                resetGraph();
            }
            time2 = System.currentTimeMillis();

            selectedNodesIterator = selectedNodes.iterator();
            while (selectedNodesIterator.hasNext()) {
                simulator.sigmaOfNode(selectedNodesIterator.next(), maxTime);
            }
            tempGraph = (DirectedWeightedMultigraph<Node, DefaultWeightedEdge>) graph.clone();
            selectedNodes.add(highestNode);
            currentBudget -= highestNode.cost;
            System.out.print(selectedNodes.size() + ":" + highestNode + ": in " + (time2 - time1) / 60000 + "minutes");
        }
    }

    private void runSimulation() {
        LoopingIterator<Node> selectedNodesIterator = new LoopingIterator<Node>(selectedNodes);

        int sigma = 0;
        for (int i = 0; i < runs; i++) {
            selectedNodesIterator.reset();
            resetGraph();
            for (int j = 0; j < selectedNodes.size(); j++) {
                sigma += simulator.sigmaOfNode(selectedNodesIterator.next(), maxTime);
            }
        }
        selectedNodesIterator.reset();
        int finalSigma = sigma / runs;

        printResults(finalSigma, selectedNodesIterator);
    }

    private void printResults(int finalSigma, LoopingIterator<Node> selectedNodesIterator) {
        String result = "\n======================================" +
                "\nRunning Simulation on Greedy Algorithm" +
                "\n--------------------------------------" +
                "\nTotal Cost Budget: " + maxCost +
                "\nTotal Time Budget: " + maxTime +
                "\nTotal Sim Runs: " + runs +
                "\n#Selected Nodes: " + selectedNodesIterator.size() +
                "\nTotal Influence Achieved:" + finalSigma +
                "\n======================================";
        System.out.println(result);
    }

    public void resetGraph() {
        Iterator<Node> nodeIt = graph.vertexSet().iterator();

        while (nodeIt.hasNext())
            nodeIt.next().resetAll();
    }
}