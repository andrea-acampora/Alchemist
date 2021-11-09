/*
 * Copyright (C) 2010-2019, Danilo Pianini and contributors listed in the main project's alchemist/build.gradle file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.test

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.file.shouldExist
import it.unibo.alchemist.loader.InitializedEnvironment
import it.unibo.alchemist.loader.LoadAlchemist
import it.unibo.alchemist.core.implementations.Engine
import it.unibo.alchemist.loader.export.CSVExporter
import it.unibo.alchemist.loader.export.GlobalExporter
import it.unibo.alchemist.model.interfaces.Position
import org.junit.jupiter.api.Assertions.assertNotNull
import org.kaikikm.threadresloader.ResourceLoader
import java.io.File

class TestCSVExporter<T, P : Position<P>> : StringSpec({
    "test exporting data on CSV file" {
        val file = ResourceLoader.getResource("testCSVExporter.yml")
        assertNotNull(file)
        val loader = LoadAlchemist.from(file)
        assertNotNull(loader)
        val initialized: InitializedEnvironment<T, P> = loader.getDefault()
        val simulation = Engine(initialized.environment)
        initialized.exporters.forEach {
            it.bindVariables(loader.variables)
        }
        simulation.addOutputMonitor(GlobalExporter(initialized.exporters))
        simulation.play()
        simulation.run()
        val exporter = initialized.exporters.firstOrNull {
            it is CSVExporter
        }
        require(exporter is CSVExporter){
            exporter as CSVExporter
            val outputFile = File(exporter.outputFile)
            outputFile.shouldExist()
            outputFile.delete()
        }
    }
})
