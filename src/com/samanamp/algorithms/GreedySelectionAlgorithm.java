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

    public GreedySelectionAlgorithm(DirectedWeightedMultigraph graph, Class<? extends SimulationEngine> SimulationClass) throws IllegalAccessException, InstantiationException {
        this.graph = graph;
        this.simulator = SimulationClass.newInstance();
        simulator.setGraph(graph);
    }

    public void runAlgoAndSimulation(int maxCost, int maxTime, int runs) {
        this.maxCost = maxCost;
        this.maxTime = maxTime;
        this.runs = runs;

        Iterator<Node> nodes = graph.vertexSet().iterator();
        LinkedList<Node> selectedNodes = new LinkedList<Node>();
        Node tmpNode;
        for (int currentCost = 0; currentCost < maxCost && nodes.hasNext(); ) {
            tmpNode = nodes.next();
            if (tmpNode.cost + currentCost <= maxCost) {
                selectedNodes.add(tmpNode);
                currentCost += tmpNode.cost;
            }
        }
        System.out.println("#nodes selected: " + selectedNodes.size());
        runSimulation(selectedNodes);
    }

    private void runSimulation(LinkedList<Node> selectedNodes) {
        LoopingIterator<Node> selectedNodesIterator = new LoopingIterator<Node>(selectedNodes);

        int sigma = 0;
        for (int i = 0; i < runs; i++) {
            selectedNodesIterator.reset();
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
}

