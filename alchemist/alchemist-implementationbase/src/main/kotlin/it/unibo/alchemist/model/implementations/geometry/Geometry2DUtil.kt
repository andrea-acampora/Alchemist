package it.unibo.alchemist.model.implementations.geometry

import it.unibo.alchemist.model.implementations.positions.Euclidean2DPosition
import it.unibo.alchemist.model.interfaces.geometry.Vector2D
import it.unibo.alchemist.model.interfaces.geometry.euclidean2d.Segment2D
import org.danilopianini.lang.MathUtils.fuzzyEquals

@JvmName("Geometry2DUtil")

/**
 * Determines if three points are collinear (i.e. they lie on the same line).
 */
fun <P : Vector2D<P>> areCollinear(p1: P, p2: P, p3: P): Boolean =
    when {
        fuzzyEquals(p1.x, p2.x) -> fuzzyEquals(p1.x, p3.x)
        else -> {
            val m = Segment2D(p1, p2).slope
            val q = p1.y - m * p1.x
            fuzzyEquals((m * p3.x + q), p3.y)
        }
    }
