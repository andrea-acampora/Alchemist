/*
 * Copyright (C) 2010-2021, Danilo Pianini and contributors
 * listed in the main project's alchemist/build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

import it.unibo.alchemist.model.interfaces.Position

/**
 * A generic exporter for the simulation.
 */
interface GenericExporter<T, P : Position<P>> {

    fun setupExportEnvironment()

    /**
     * Main method used by exporters to export data.
     */
    fun exportData()


    fun closeExportEnvironment()
}
