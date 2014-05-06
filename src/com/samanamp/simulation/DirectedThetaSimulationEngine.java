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

package com.samanamp.simulation;

import com.samanamp.Node;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import java.util.Iterator;

/**
 * Created by saman on 6/05/2014.
 */
public class DirectedThetaSimulationEngine implements SimulationEngine {
    private final float MAX_PROBABILITY = 1;
    DirectedWeightedMultigraph<Node, DefaultWeightedEdge> graph;

    @Override
    public void setGraph(DirectedWeightedMultigraph graph) {
        this.graph = graph;
    }

    public int sigmaOfNode(Node node, int budget) {
        node.active = true;
        node.selected = true;
        resetTraversed();
        return (int) sigmaOfNode(node, budget, 0);
    }

    public double floatSigmaOfNode(Node node, int budget) {
        node.active = true;
        node.selected = true;
        resetTraversed();
        return sigmaOfNode(node, budget, 0);
    }

    private double sigmaOfNode(Node node, int timeBudget, int currentTime) {
        Node targetNode;
        if (currentTime > timeBudget) return 0;
        if (node.active && !node.selected) return 0;
        node.traversed = true;
        double nodeSigma = 0;
        if (node.activationImpulse(MAX_PROBABILITY)) {
            nodeSigma++;
            Iterator<DefaultWeightedEdge> edgeIterator = graph.outgoingEdgesOf(node).iterator();
            DefaultWeightedEdge tmpEdge;
            int nextTime = currentTime + node.edgeTime;
            while (edgeIterator.hasNext()) {
                tmpEdge = edgeIterator.next();
                targetNode = graph.getEdgeTarget(tmpEdge);

                nodeSigma += sigmaOfNode(targetNode, timeBudget, nextTime) * targetNode.activationProbability;
                System.out.println(nodeSigma + "::" + node.name + ":" + targetNode.name + ":" + targetNode.activationProbability);
            }
        }
        System.out.println(node.name + ":" + nodeSigma);
        return nodeSigma;
    }

    public void resetTraversed() {
        Iterator<Node> nodeIt = graph.vertexSet().iterator();
        while (nodeIt.hasNext())
            nodeIt.next().traversed = false;
    }
}
