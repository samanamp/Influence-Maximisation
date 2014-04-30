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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Random;

public class Node {
    public String name;
    public int cost;
    public double activationProbability;
    public boolean traversed = false;
    public boolean active = false;
    public int edgeTime;
    public boolean selected = false;
    private Random randomGen;

    public Node(String name) {
        this.name = name;
        randomGen = new Random(System.currentTimeMillis());
    }

    public void resetAll() {
        traversed = false;
        active = false;
    }

    public void resetTraveresed() {
        traversed = false;
    }

    public void addCostAndProbability(int inDegreeOfNode, int outDegreeOfNode) {
        cost = outDegreeOfNode;
        activationProbability = 10 / (inDegreeOfNode == 0 ? 10 : (float) (inDegreeOfNode + 10));
    }

    public boolean activationImpulse() {
        if (active) return true;

        if (randomGen.nextFloat() < activationProbability) {
            active = true;
            return true;
        }
        return false;
    }

    public String toString() {
        return "<" + name + ":" + cost + ">";
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof Node))
            return false;

        Node node = (Node) obj;
        return new EqualsBuilder().
                // if deriving: appendSuper(super.equals(obj)).
                        append(name, node.name).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                // if deriving: appendSuper(super.hashCode()).
                append(name).
                toHashCode();
    }
}
