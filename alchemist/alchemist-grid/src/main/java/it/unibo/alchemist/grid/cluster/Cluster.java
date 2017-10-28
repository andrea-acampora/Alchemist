package it.unibo.alchemist.grid.cluster;

import it.unibo.alchemist.grid.simulation.Complexity;

public interface Cluster extends AutoCloseable {

    /**
     * 
     * @param complexity a siumulation's complexity
     * @return Workers' set that can execute a simulation with given complexity 
     */
    WorkersSet getWorkersSet(Complexity complexity);

    /**
     * Leave the cluster.
     */
    void close();
}
