package it.unibo.alchemist.boundary.gui.effects;

import it.unibo.alchemist.boundary.gui.utility.ResourceLoader;
import it.unibo.alchemist.boundary.gui.view.properties.PropertyFactory;
import it.unibo.alchemist.boundary.gui.view.properties.RangedDoubleProperty;
import it.unibo.alchemist.boundary.wormhole.interfaces.IWormhole2D;
import it.unibo.alchemist.model.interfaces.Environment;
import it.unibo.alchemist.model.interfaces.Node;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Simple effect that draws a colored dot for each {@link Node}.
 * <p>
 * It's possible to set the size and the color of the dots.
 */
public class DrawColoredDot extends DrawDot implements EffectFX {
    /**
     * Magic number used by auto-generated {@link #hashCode()} method.
     */
    private static final int HASHCODE_NUMBER_1 = 1231;
    /**
     * Magic number used by auto-generated {@link #hashCode()} method.
     */
    private static final int HASHCODE_NUMBER_2 = 1237;

    /**
     * Default generated Serial Version UID.
     */
    private static final long serialVersionUID = -2329825220099191395L;
    /**
     * Default effect name
     */
    private static final String DEFAULT_NAME = ResourceLoader.getStringRes("drawcoloreddot_default_name");
    private RangedDoubleProperty red;
    private RangedDoubleProperty green;
    private RangedDoubleProperty blue;
    private RangedDoubleProperty alpha;

    /**
     * Empty constructor.
     * <p>
     * Name is set to default name.
     * <p>
     * Default visibility is true.
     */
    public DrawColoredDot() {
        super(DEFAULT_NAME);

        // Set properties to default color of DrawDot
        red = PropertyFactory.getColorChannelProperty(ResourceLoader.getStringRes("drawcoloreddot_red"), super.getColor().getRed());
        green = PropertyFactory.getColorChannelProperty(ResourceLoader.getStringRes("drawcoloreddot_green"), super.getColor().getGreen());
        blue = PropertyFactory.getColorChannelProperty(ResourceLoader.getStringRes("drawcoloreddot_blue"), super.getColor().getBlue());
        alpha = PropertyFactory.getColorChannelProperty(ResourceLoader.getStringRes("drawcoloreddot_alpha"), super.getColor().getOpacity());

        // Update the color at each change
        red.addListener(this.updateColor());
        green.addListener(this.updateColor());
        blue.addListener(this.updateColor());
        alpha.addListener(this.updateColor());
    }

    /**
     * Default constructor.
     * <p>
     * Default visibility is true.
     *
     * @param name the name of the effect.
     */
    public DrawColoredDot(final String name) {
        this();
        this.setName(name);
    }

    @Override
    public Color getColor() { // NOPMD - Only widening method visibility
        return super.getColor();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates also all color-related properties.
     */
    @Override
    public void setColor(final Color color) {
        // Also widens method visibility from parent 
        this.setAlpha(color.getOpacity());
        this.setBlue(color.getBlue());
        this.setGreen(color.getGreen());
        this.setRed(color.getRed());
        super.setColor(color);
    }

    /**
     * {@inheritDoc}
     * <p>
     * For each {@link Node} in the specified {@link Environment}, it will draw
     * a dot of a specified {@link Color} (default: {@link Color#BLACK black}).
     */
    @Override
    public <T> void apply(final GraphicsContext graphic, final Environment<T> environment, final IWormhole2D wormhole) {
        super.apply(graphic, environment, wormhole);
    }

    /**
     * Returns a {@link ChangeListener} that updates the color of the dots.
     *
     * @return the {@code ChangeListener}
     */
    private ChangeListener<Number> updateColor() {
        return (final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue) -> {
            super.setColor(Color.rgb(
                    red.getValue().intValue(),
                    green.getValue().intValue(),
                    blue.getValue().intValue(),
                    alpha.getValue()));
        };
    }

    /**
     * The alpha channel of the color of the dots representing each {@link Node}
     * in the {@link Environment} specified when calling
     * {@link #apply(GraphicsContext, Environment, IWormhole2D) apply} in percentage.
     *
     * @return the alpha channel property
     */
    public DoubleProperty alphaProperty() {
        return this.alpha;
    }

    /**
     * Gets the value of {@code alphaProperty}.
     *
     * @return the alpha channel of the color of the dots
     */
    public double getAlpha() {
        return this.alpha.get();
    }

    /**
     * Sets the value of {@code alphaProperty}.
     *
     * @param alpha the alpha channel to set
     */
    public void setAlpha(final double alpha) {
        this.alpha.set(alpha);
    }

    /**
     * The blue channel of the color of the dots representing each {@link Node}
     * in the {@link Environment} specified when calling
     * {@link #apply(GraphicsContext, Environment, IWormhole2D) apply} in percentage.
     *
     * @return the blue channel property
     */
    public DoubleProperty blueProperty() {
        return this.blue;
    }

    /**
     * Gets the value of {@code blueProperty}.
     *
     * @return the blue channel of the color of the dots
     */
    public double getBlue() {
        return this.blue.get();
    }

    /**
     * Sets the value of {@code blueProperty}.
     *
     * @param blue the blue channel to set
     */
    public void setBlue(final double blue) {
        this.blue.set(blue);
    }

    /**
     * The green channel of the color of the dots representing each {@link Node}
     * in the {@link Environment} specified when calling
     * {@link #apply(GraphicsContext, Environment, IWormhole2D) apply} in percentage.
     *
     * @return the green channel property
     */
    public DoubleProperty greenProperty() {
        return this.green;
    }

    /**
     * Gets the value of {@code greenProperty}.
     *
     * @return the green channel of the color of the dots
     */
    public double getGreen() {
        return this.green.get();
    }

    /**
     * Sets the value of {@code greenProperty}.
     *
     * @param green the green channel to set
     */
    public void setGreen(final double green) {
        this.green.set(green);
    }

    /**
     * The red channel of the color of the dots representing each {@link Node}
     * in the {@link Environment} specified when calling
     * {@link #apply(GraphicsContext, Environment, IWormhole2D) apply} in percentage.
     *
     * @return the red channel property
     */
    public DoubleProperty redProperty() {
        return this.red;
    }

    /**
     * Gets the value of {@code redProperty}.
     *
     * @return the red channel of the color of the dots
     */
    public double getRed() {
        return this.red.get();
    }

    /**
     * Sets the value of {@code redProperty}.
     *
     * @param red the red channel to set
     */
    public void setRed(final double red) {
        this.red.set(red);
    }

    /**
     * Method needed for well working serialization.
     * <p>
     * From {@link Serializable}: <blockquote>The {@code writeObject} method is
     * responsible for writing the state of the object for its particular class
     * so that the corresponding readObject method can restore it. The default
     * mechanism for saving the Object's fields can be invoked by calling
     * {@code out.defaultWriteObject}. The method does not need to concern
     * itself with the state belonging to its superclasses or subclasses. State
     * is saved by writing the 3 individual fields to the
     * {@code ObjectOutputStream} using the {@code writeObject} method or by
     * using the methods for primitive data types supported by
     * {@code DataOutput}. </blockquote>
     *
     * @param stream
     *            the output stream
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(red);
        stream.writeObject(green);
        stream.writeObject(blue);
        stream.writeObject(alpha);
    }

    /**
     * Method needed for well working serialization.
     * <p>
     * From {@link Serializable}: <blockquote>The {@code readObject} method is
     * responsible for reading from the stream and restoring the classes fields.
     * It may call {@code in.defaultReadObject} to invoke the default mechanism
     * for restoring the object's non-static and non-transient fields. The
     * {@code defaultReadObject} method uses information in the stream to assign
     * the fields of the object saved in the stream with the correspondingly
     * named fields in the current object. This handles the case when the class
     * has evolved to add new fields. The method does not need to concern itself
     * with the state belonging to its superclasses or subclasses. State is
     * saved by writing the individual fields to the {@code ObjectOutputStream}
     * using the {@code writeObject} method or by using the methods for
     * primitive data types supported by {@code DataOutput}. </blockquote>
     *
     * @param stream
     *            the input stream
     */
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        red = (RangedDoubleProperty) stream.readObject();
        green = (RangedDoubleProperty) stream.readObject();
        blue = (RangedDoubleProperty) stream.readObject();
        alpha = (RangedDoubleProperty) stream.readObject();

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((alphaProperty() == null) ? 0 : alphaProperty().hashCode());
        result = prime * result + ((blueProperty() == null) ? 0 : blueProperty().hashCode());
        result = prime * result + ((greenProperty() == null) ? 0 : greenProperty().hashCode());
        result = prime * result + ((super.getName() == null) ? 0 : super.getName().hashCode());
        result = prime * result + ((redProperty() == null) ? 0 : redProperty().hashCode());
        result = prime * result + ((super.getSize() == null) ? 0 : super.getSize().hashCode());
        result = prime * result + (super.isVisibile() ? HASHCODE_NUMBER_1 : HASHCODE_NUMBER_2);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DrawColoredDot other = (DrawColoredDot) obj;
        if (super.isVisibile() != other.isVisibile()) {
            return false;
        }
        if (alphaProperty() == null) {
            if (other.alphaProperty() != null) {
                return false;
            }
        } else if (!alphaProperty().equals(other.alphaProperty())) {
            return false;
        }
        if (blueProperty() == null) {
            if (other.blueProperty() != null) {
                return false;
            }
        } else if (!blueProperty().equals(other.blueProperty())) {
            return false;
        }
        if (greenProperty() == null) {
            if (other.greenProperty() != null) {
                return false;
            }
        } else if (!greenProperty().equals(other.greenProperty())) {
            return false;
        }
        if (super.getName() == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!super.getName().equals(other.getName())) {
            return false;
        }
        if (redProperty() == null) {
            if (other.redProperty() != null) {
                return false;
            }
        } else if (!redProperty().equals(other.redProperty())) {
            return false;
        }
        if (super.getSize() == null) {
            if (other.getSize() != null) {
                return false;
            }
        } else if (!super.getSize().equals(other.getSize())) {
            return false;
        }
        return true;
    }
}
