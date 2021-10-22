/*
 * Copyright (C) 2010-2021, Danilo Pianini and contributors
 * listed in the main project's alchemist/build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.loader.export

import it.unibo.alchemist.model.interfaces.Position

/**
 * TODO.
 * @param url TODO
 */
class MongoDBExporter<T, P : Position<P>>(val url: String) : AbstractExporter<T, P>() {

    override fun setupExportEnvironment() {
        TODO("Not yet implemented")
    }

    override fun exportData() {
        TODO("Not yet implemented")
    }

    override fun closeExportEnvironment() {
        TODO("Not yet implemented")
    }
}
