/*
 * Copyright (C) 2010-2020, Danilo Pianini and contributors
 * listed in the main project's alchemist/build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.model.implementations.environments

import it.unibo.alchemist.model.implementations.geometry.AdimensionalShape
import it.unibo.alchemist.model.implementations.positions.Euclidean2DPosition
import it.unibo.alchemist.model.interfaces.Neighborhood
import it.unibo.alchemist.model.interfaces.Node
import it.unibo.alchemist.model.interfaces.environments.Euclidean2DEnvironment
import it.unibo.alchemist.model.interfaces.environments.Physics2DEnvironment
import it.unibo.alchemist.model.interfaces.geometry.GeometricShapeFactory
import it.unibo.alchemist.model.interfaces.geometry.euclidean2d.Euclidean2DShape
import it.unibo.alchemist.model.interfaces.geometry.euclidean2d.Euclidean2DShapeFactory
import it.unibo.alchemist.model.interfaces.geometry.euclidean2d.Euclidean2DTransformation
import it.unibo.alchemist.model.interfaces.nodes.NodeWithShape

/**
 * Implementation of [Physics2DEnvironment].
 */
open class Continuous2DEnvironment<T> :
    Euclidean2DEnvironment<T>,
    Abstract2DEnvironment<T, Euclidean2DPosition>(),
    Physics2DEnvironment<T> {

    companion object {
        @JvmStatic private val serialVersionUID: Long = 1L

        private val adimensional =
            AdimensionalShape<Euclidean2DPosition, Euclidean2DTransformation>(Euclidean2DEnvironment.origin)
    }

    override val shapeFactory: Euclidean2DShapeFactory = GeometricShapeFactory.getInstance()
    private val defaultHeading = Euclidean2DPosition(0.0, 0.0)
    private val nodeToHeading = mutableMapOf<Node<T>, Euclidean2DPosition>()
    private var largestShapeDiameter: Double = 0.0

    override fun getNodesWithin(shape: Euclidean2DShape): List<Node<T>> =
        if (shape.diameter + largestShapeDiameter <= 0) emptyList()
        else getNodesWithinRange(shape.centroid, (shape.diameter + largestShapeDiameter) / 2)
            .filter { shape.intersects(getShape(it)) }

    override fun getHeading(node: Node<T>) =
        nodeToHeading.getOrPut(node, { defaultHeading })

    override fun setHeading(node: Node<T>, direction: Euclidean2DPosition) {
        val oldHeading = getHeading(node)
        getPosition(node)?.also {
            nodeToHeading[node] = direction
            if (!canNodeFitPosition(node, it)) {
                nodeToHeading[node] = oldHeading
            }
        }
    }

    override fun getShape(node: Node<T>): Euclidean2DShape =
        if (node is NodeWithShape<*, *, *>) {
            shapeFactory.requireCompatible(node.shape)
                .transformed {
                    origin(getPosition(node))
                    rotate(getHeading(node))
                }
        } else {
            Companion.adimensional
        }

    /**
     * Keeps track of the largest diameter of the shapes.
     */
    override fun nodeAdded(node: Node<T>, position: Euclidean2DPosition, neighborhood: Neighborhood<T>) {
        super.nodeAdded(node, position, neighborhood)
        if (node is NodeWithShape<*, *, *> && node.shape.diameter > largestShapeDiameter) {
            largestShapeDiameter = node.shape.diameter
        }
    }

    /**
     * {@inheritDoc}.
     */
    override fun nodeRemoved(node: Node<T>, neighborhood: Neighborhood<T>) =
        super.nodeRemoved(node, neighborhood)
            .also {
                nodeToHeading.remove(node)
                if (node is NodeWithShape<*, *, *> && largestShapeDiameter <= node.shape.diameter) {
                    largestShapeDiameter = nodes.asSequence()
                        .filterIsInstance<NodeWithShape<*, *, *>>()
                        .map { it.shape.diameter }
                        .max() ?: 0.0
                }
            }

    /**
     * Moves the node only if it doesn't collide with others.
     */
    override fun moveNodeToPosition(node: Node<T>, newpos: Euclidean2DPosition) =
        if (canNodeFitPosition(node, newpos)) super.moveNodeToPosition(node, newpos) else Unit

    /**
     * A node should be added only if it doesn't collide with already existing nodes and fits in the environment's
     * limits.
     */
    override fun nodeShouldBeAdded(node: Node<T>, position: Euclidean2DPosition): Boolean =
        node !is NodeWithShape<*, *, *> ||
            getNodesWithin(shapeFactory.requireCompatible(node.shape).transformed { origin(position) }).isEmpty()

    /**
     * Creates an euclidean position from the given coordinates.
     * @param coordinates coordinates array
     * @return Euclidean2DPosition
     */
    override fun makePosition(vararg coordinates: Number) =
        with(coordinates) {
            require(size == 2)
            Euclidean2DPosition(coordinates[0].toDouble(), coordinates[1].toDouble())
        }

    override fun canNodeFitPosition(node: Node<T>, position: Euclidean2DPosition) =
        getNodesWithin(getShape(node).transformed { origin(position) })
            .minusElement(node)
            .isEmpty()
}