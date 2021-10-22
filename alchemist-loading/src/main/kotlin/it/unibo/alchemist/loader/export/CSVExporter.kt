/*
 * Copyright (C) 2010-2021, Danilo Pianini and contributors
 * listed in the main project's alchemist/build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.loader.export

import com.google.common.base.Charsets
import it.unibo.alchemist.model.implementations.times.DoubleTime
import it.unibo.alchemist.model.interfaces.Environment
import it.unibo.alchemist.model.interfaces.Position
import it.unibo.alchemist.model.interfaces.Reaction
import it.unibo.alchemist.model.interfaces.Time
import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Writes on file data provided by a number of {@link Extractor}s. Produces a
 * CSV with '#' as comment character.e
 * @param filename the name of the file to export data to.
 */
class CSVExporter<T, P : Position<P>>(val filename: String) : AbstractExporter<T, P>() {

    companion object {
        /**
         * Character used to separate comments from data on export files.
         */
        const val SEPARATOR = "#####################################################################"
    }
    private val sampleSpace: Double = 1.0
    private val header: String? = null
    private var out: PrintStream? = null
    private var count = -1L // The 0th should be sampled

    override fun setupExportEnvironment(environment: Environment<T, P>?) {
        out = PrintStream(filename, Charsets.UTF_8.name())
        out!!.println(SEPARATOR)
        out!!.print("# Alchemist log file - simulation started at: ")
        val isoTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.US)
        isoTime.timeZone = TimeZone.getTimeZone("UTC")
        out!!.print(isoTime.format(Date()))
        out!!.println(" #")
        out!!.println(SEPARATOR)
        out!!.print("# ")
        out!!.println(header)
        out!!.println("#")
        out!!.println("# The columns have the following meaning: ")
        out!!.print("# ")
        dataExtractor.stream()
            .flatMap {
                it.names.stream()
            }.forEach {
                out!!.print(it)
                out!!.print(" ")
            }

        out!!.println()
        exportData(environment, null, DoubleTime(), 0)
    }

    override fun exportData(environment: Environment<T, P>?, reaction: Reaction<T>?, time: Time?, step: Long) {
        val curSample: Long = (time!!.toDouble() / sampleSpace).toLong()
        if (curSample > count) {
            count = curSample
            writeRow(environment!!, reaction, time, step)
        }
    }

    override fun closeExportEnvironment(environment: Environment<T, P>?, time: Time?, step: Long) {
        out!!.println(SEPARATOR)
        out!!.print("# End of data export. Simulation finished at: ")
        val isoTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.US)
        isoTime.timeZone = TimeZone.getTimeZone("UTC")
        out!!.print(isoTime.format(Date()))
        out!!.println(" #")
        out!!.println(SEPARATOR)
        out!!.close()
    }

    private fun printDatum(datum: Double) {
        out!!.print(datum)
        out!!.print(' ')
    }

    private fun writeRow(env: Environment<T, *>, r: Reaction<T>?, time: Time, step: Long) {
        dataExtractor.stream()
            .flatMapToDouble { Arrays.stream(it.extractData(env, r, time, step)) }
            .forEach {
                printDatum(it)
            }
        out!!.println()
    }
}
