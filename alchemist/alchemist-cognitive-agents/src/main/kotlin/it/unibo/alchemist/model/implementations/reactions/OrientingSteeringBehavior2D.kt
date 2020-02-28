package it.unibo.alchemist.model.implementations.reactions

import it.unibo.alchemist.model.implementations.actions.Combine
import it.unibo.alchemist.model.implementations.actions.Seek2D
import it.unibo.alchemist.model.implementations.actions.steeringstrategies.DistanceWeighted
import it.unibo.alchemist.model.implementations.geometry.angleBetween
import it.unibo.alchemist.model.implementations.geometry.magnitude
import it.unibo.alchemist.model.implementations.geometry.normal
import it.unibo.alchemist.model.implementations.geometry.resize
import it.unibo.alchemist.model.implementations.positions.Euclidean2DPosition
import it.unibo.alchemist.model.interfaces.Environment
import it.unibo.alchemist.model.interfaces.OrientingPedestrian
import it.unibo.alchemist.model.interfaces.SteeringStrategy
import it.unibo.alchemist.model.interfaces.TimeDistribution
import it.unibo.alchemist.model.interfaces.SteeringAction
import it.unibo.alchemist.model.interfaces.graph.GraphEdgeWithData
import it.unibo.alchemist.model.interfaces.graph.NavigationGraph
import it.unibo.alchemist.model.interfaces.geometry.euclidean.twod.ConvexEuclidean2DShape
import it.unibo.alchemist.model.interfaces.geometry.euclidean.twod.ConvexPolygon
import it.unibo.alchemist.model.interfaces.geometry.euclidean.twod.Euclidean2DSegment
import it.unibo.alchemist.model.interfaces.geometry.euclidean.twod.Euclidean2DTransformation
import it.unibo.alchemist.model.interfaces.graph.GraphEdge
import kotlin.math.PI

/**
 * An [OrientingBehavior2D] with [SteeringAction]s as well. This class implements a
 * basic algorithm capable of mixing the steering behavior and the orienting behavior
 * smartly (e.g. avoiding situations in which the agent is blocked due to the voiding
 * of opposite forces). However, the resulting movements still present some shaking.
 *
 * @param N1 the type of nodes of the [environmentGraph].
 * @param E1 the type of edges of the [environmentGraph].
 * @param N2 the type of landmarks of the pedestrian's cognitive map.
 * @param E2 the type of edges of the pedestrian's cognitive map.
 * @param T the concentration type.
 */
class OrientingSteeringBehavior2D<N1 : ConvexPolygon, E1 : GraphEdgeWithData<N1, Euclidean2DSegment>, N2 : ConvexEuclidean2DShape, E2 : GraphEdge<N2>, T> @JvmOverloads constructor(
    environment: Environment<T, Euclidean2DPosition>,
    pedestrian: OrientingPedestrian<Euclidean2DPosition, Euclidean2DTransformation, N2, E2, T>,
    timeDistribution: TimeDistribution<T>,
    environmentGraph: NavigationGraph<Euclidean2DPosition, Euclidean2DTransformation, N1, E1>,
    private val steerStrategy: SteeringStrategy<T, Euclidean2DPosition> = DistanceWeighted(environment, pedestrian)
) : OrientingBehavior2D<N1, E1, N2, E2, T>(environment, pedestrian, timeDistribution, environmentGraph) {

    override fun moveTowards(target: Euclidean2DPosition, currentRoom: N1?) {
        val currPos = environment.getPosition(pedestrian)
        var desiredMovement = Seek2D(environment, this, pedestrian, *target.cartesianCoordinates).nextPosition
        var disturbingMovement = Combine(environment, this, pedestrian, steerActions(), steerStrategy).nextPosition
        /*
         * When the angle between the desired movement and the movement deriving from
         * other disturbing forces is > 90 degrees, we adjust the disturbing movement
         * so as to have an angle of 90 degrees.
         */
        if (desiredMovement.angleBetween(disturbingMovement) > PI / 2) {
            disturbingMovement = adjustDisturbingMovement(desiredMovement, disturbingMovement)
        }
        /*
         * We also resize the desired movement to have a magnitude > of the disturbing movement
         * (empirically, resizing it so as to have a magnitude equal to the one of the disturbing
         * movement can still block the agent in some cases)
         */
        if (disturbingMovement.magnitude() > desiredMovement.magnitude()) {
            desiredMovement = desiredMovement.resize(disturbingMovement.magnitude() * 1.2)
        }
        val movement = desiredMovement + disturbingMovement
        super.moveTowards(currPos + movement, currentRoom)
    }

    private fun adjustDisturbingMovement(desiredMovement: Euclidean2DPosition, disturbingMovement: Euclidean2DPosition): Euclidean2DPosition {
        val n = desiredMovement.normal()
        val length = disturbingMovement.magnitude()
        val n1 = n.resize(length)
        val n2 = n.resize(-length)
        return if (n1.getDistanceTo(disturbingMovement) < n2.getDistanceTo(disturbingMovement)) {
            n1
        } else {
            n2
        }
    }

    private fun steerActions(): List<SteeringAction<T, Euclidean2DPosition>> =
        actions.filterIsInstance<SteeringAction<T, Euclidean2DPosition>>()
}
