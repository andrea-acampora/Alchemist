/*
 * Copyright (C) 2010-2020, Danilo Pianini and contributors
 * listed in the main project's alchemist/build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.test

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import it.unibo.alchemist.loader.GraphStreamSupport
import it.unibo.alchemist.model.implementations.environments.Continuous2DEnvironment
import it.unibo.alchemist.model.implementations.nodes.AbstractNode
import org.apache.commons.math3.random.MersenneTwister

typealias EnvironmentDisplacement = List<Pair<List<Double>, List<Int>>>

/**
 * A test creating graphstream displacements and verifying that they work reproducibly.
 */
class TestGraphStreamReproducibility : FreeSpec({
    "GraphStream displacement" - {
        mapOf(
            "Lobster" to listOf(2, 10),
            "RandomEuclidean" to emptyList(),
            "BarabasiAlbert" to listOf(2)
        ).forEach { (graphType, parameters) ->
            graphType - {
                val generator = MersenneTwister(1)
                val ids = generateSequence { generator.nextLong() }.take(10).toList()
                fun generateGraphs(): List<EnvironmentDisplacement> = ids.map { uniqueId ->
                    val environment = Continuous2DEnvironment<Any>()
                    val graphStream = GraphStreamSupport.generateGraphStream(
                        environment = environment,
                        nodeCount = 100,
                        generatorName = graphType,
                        uniqueId = uniqueId.toLong(),
                        layoutQuality = 0.1,
                        parameters = parameters.toTypedArray()
                    )
                    environment.linkingRule = graphStream.linkingRule
                    graphStream.displacement.forEach {
                        environment.addNode(
                            object : AbstractNode<Any>(environment) { override fun createT(): Any = Any() },
                            it
                        )
                    }
                    environment.nodes.map { node ->
                        environment.getPosition(node).coordinates.toList() to
                            environment.getNeighborhood(node).neighbors.map { it.id }
                    }
                }
                val graphs1 = generateGraphs()
                "with different seeds should differ" {
                    graphs1.distinct().size shouldBe graphs1.size
                }
                "with the same seeds should be equal" {
                    graphs1 shouldBe generateGraphs()
                }
            }
        }
    }
})