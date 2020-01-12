package it.unibo.alchemist.boundary.gui.view.property;

import com.google.common.base.Charsets;
import com.google.gson.reflect.TypeToken;
import it.unibo.alchemist.boundary.gui.effects.json.AbstractPropertySerializationTest;
import it.unibo.alchemist.boundary.gui.view.properties.RangedIntegerProperty;
import it.unibo.alchemist.test.TemporaryFile;
import javafx.beans.property.Property;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUint test for custom {@link Property} serialization.
 */
public final class RangedIntegerPropertySerializationTest extends AbstractPropertySerializationTest {
    private static final String TEST_NAME = "Pippo";
    private static final int TEST_INITIAL_VALUE = 5;
    private static final int TEST_LOWER_BOUND = 0;
    private static final int TEST_UPPER_BOUND = 100;

    @Test
    @Override
    public void testJavaSerialization() throws IOException, ClassNotFoundException {
        final File file = TemporaryFile.create();
        final RangedIntegerProperty rangedIntegerProperty = new RangedIntegerProperty(TEST_NAME, TEST_INITIAL_VALUE, TEST_LOWER_BOUND, TEST_UPPER_BOUND);
        RangedIntegerProperty deserialized;
        try (
                FileOutputStream fout = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                FileInputStream fin = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fin)
        ) {
            oos.writeObject(rangedIntegerProperty);
            deserialized = (RangedIntegerProperty) ois.readObject();
        }
        assertEquals(rangedIntegerProperty, deserialized, getMessage(rangedIntegerProperty, deserialized));
    }

    @Test
    @Override
    public void testGsonSerialization() throws Exception {
        final File file = TemporaryFile.create();
        final RangedIntegerProperty rangedIntegerProperty = new RangedIntegerProperty(TEST_NAME, TEST_INITIAL_VALUE, TEST_LOWER_BOUND, TEST_UPPER_BOUND);
        RangedIntegerProperty deserialized;
        try (
                Writer writer = new FileWriter(file, Charsets.UTF_8);
                Reader reader = new FileReader(file, Charsets.UTF_8)
        ) {
            GSON.toJson(rangedIntegerProperty, this.getGsonType(), writer);
            deserialized = GSON.fromJson(reader, this.getGsonType());
        }
        assertEquals(rangedIntegerProperty, deserialized, getMessage(rangedIntegerProperty, deserialized));
    }

    @Override
    protected Type getGsonType() {
        return new TypeToken<RangedIntegerProperty>() { }.getType();
    }

    @Override
    protected <T> String getMessage(final Property<T> origin, final Property<T> deserialized) {
        if (origin == null || deserialized == null) {
            return super.getMessage(origin, deserialized);
        }

        return super.getMessage(origin, deserialized)
                + System.lineSeparator() + "Origin range: (" + ((RangedIntegerProperty) origin).getLowerBound()
                + ", " + ((RangedIntegerProperty) origin).getUpperBound() + ")"
                + System.lineSeparator() + "Deserialized range: (" + ((RangedIntegerProperty) deserialized).getLowerBound()
                + ", " + ((RangedIntegerProperty) deserialized).getUpperBound() + ")";
    }
}
