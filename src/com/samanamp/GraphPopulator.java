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

package com.samanamp;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;


public class GraphPopulator {

    DirectedWeightedMultigraph<Node, DefaultWeightedEdge> graph;

    public GraphPopulator() {
        graph = new DirectedWeightedMultigraph<Node, DefaultWeightedEdge>(DefaultWeightedEdge.class);
    }

    public DirectedWeightedMultigraph<Node, DefaultWeightedEdge> startPopulating() {
        readFile();
        addCostAndProbability();
        addTimeToEdges();
        return graph;
    }

    private void addTimeToEdges() {
        Iterator<Node> nodeIterator = graph.vertexSet().iterator();
        Node tmp;
        int tmpNodeTime = 1;
        while (nodeIterator.hasNext()) {
            tmp = nodeIterator.next();
            tmp.edgeTime = tmpNodeTime;
            Iterator<DefaultWeightedEdge> edgeIt = graph.outgoingEdgesOf(tmp).iterator();
            while (edgeIt.hasNext()) {
                graph.setEdgeWeight(edgeIt.next(), tmpNodeTime);
            }
        }
    }

    private void addCostAndProbability() {
        Iterator<Node> nodeIterator = graph.vertexSet().iterator();
        while (nodeIterator.hasNext()) {
            Node tmp = nodeIterator.next();
            tmp.addCostAndProbability(graph.inDegreeOf(tmp), graph.outDegreeOf(tmp));
        }
    }

    private void readFile() {
        BufferedReader br = null;
        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader("soc-Epinions1.txt"));
            //skip the header
            br.readLine();
            br.readLine();
            br.readLine();
            br.readLine();
            while ((sCurrentLine = br.readLine()) != null) {
                transform(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void transform(String oneLine) {
        String[] splitNodes = (oneLine.split("\\s"));
        Node fromNode = new Node(splitNodes[0]);
        Node toNode = new Node(splitNodes[1]);
        graph.addVertex(fromNode);
        graph.addVertex(toNode);
        graph.addEdge(fromNode, toNode);
    }

    public void resetGraph() {
        Iterator<Node> nodeIt = graph.vertexSet().iterator();
        while (nodeIt.hasNext())
            nodeIt.next().resetAll();
    }

}
