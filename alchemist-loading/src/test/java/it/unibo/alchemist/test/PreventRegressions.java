/*
 * Copyright (C) 2010-2019, Danilo Pianini and contributors listed in the main project's alchemist/build.gradle file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.test;

import it.unibo.alchemist.loader.LoadAlchemist;
import it.unibo.alchemist.loader.export.MeanSquaredError;
import it.unibo.alchemist.model.interfaces.Environment;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;
import org.kaikikm.threadresloader.ResourceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests loading of a custom {@link it.unibo.alchemist.loader.export.GenericExporter}.
 */
class PreventRegressions {

    /**
     * Test the ability to inject variables.
     */
    @Test
    void testLoadCustomExport() {
        LoadAlchemist
            .from(ResourceLoader.getResource("testCustomExport.yml"))
            .getDefault()
            .getExporters()
            .stream()
            .findFirst()
            .ifPresent(exporter -> {
                assertEquals(1, exporter.getDataExtractors().size());
                assertEquals(MeanSquaredError.class, exporter.getDataExtractors().get(0).getClass());
            });
    }

    /**
     * Test environment serialization and incarnation restoration.
     */
    @Test
    void testLoadAndSerialize() {
        final Environment<?, ?> env = LoadAlchemist
            .from(ResourceLoader.getResource("testCustomExport.yml"))
            .getDefault()
            .getEnvironment();
        assertTrue(env.getIncarnation().isPresent());
        final byte[] serialized = SerializationUtils.serialize(env);
        assertNotNull(serialized);
        final Object deserialized = SerializationUtils.deserialize(SerializationUtils.serialize(env));
        assertNotNull(deserialized);
        assertEquals(env.getClass(), deserialized.getClass());
        assertTrue(((Environment<?, ?>) deserialized).getIncarnation().isPresent()); 
    }
}
