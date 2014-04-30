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

/**
 * Created by saman on 30/04/2014.
 */

import com.samanamp.Node;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import java.util.Iterator;

/**
 * Created by saman on 29/04/2014.
 */
public class DirectedSimulationEngine implements SimulationEngine {
    DirectedWeightedMultigraph<Node, DefaultWeightedEdge> graph;

    @Override
    public void setGraph(DirectedWeightedMultigraph graph) {
        this.graph = graph;
    }

    public int sigmaOfNode(Node node, int budget) {
        node.active = true;
        node.selected = true;
        //long startTime = System.nanoTime();
        //System.out.print(node);
        //sigmaOfNode(node, budget, 0);
        //long finishTime = System.nanoTime();
        //System.out.println("Time: "+(finishTime-startTime));
        return sigmaOfNode(node, budget, 0);
    }

    private int sigmaOfNode(Node node, int timeBudget, int currentTime) {

        if (currentTime > timeBudget) return 0;
        if (node.active && !node.selected) return 0;
        node.traversed = true;
        int nodeSigma = 0;
        if (node.activationImpulse()) {
            nodeSigma++;
            Iterator<DefaultWeightedEdge> edgeIterator = graph.outgoingEdgesOf(node).iterator();
            DefaultWeightedEdge tmpEdge;
            int nextTime = currentTime + node.edgeTime;
            while (edgeIterator.hasNext()) {
                tmpEdge = edgeIterator.next();
                nodeSigma += sigmaOfNode(graph.getEdgeTarget(tmpEdge), timeBudget, nextTime);
            }
        }
        return nodeSigma;
    }
}
