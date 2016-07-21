/*
 * Copyright (C) 2010-2016, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model.implementations.conditions;

import it.unibo.alchemist.model.implementations.nodes.CellNodeImpl;
import it.unibo.alchemist.model.interfaces.Molecule;
/**
 *
 */
public class BiomolPresentInCell extends GenericMoleculePresent<Double> {

    private static final long serialVersionUID = -5772829360637946655L;

    /**
     * 
     * @param biomol 
     * @param concentration 
     * @param node 
     */
    public BiomolPresentInCell(final Molecule biomol, final Double concentration, final CellNodeImpl node) {
        super(biomol, node, concentration);
    }

    @Override
    public CellNodeImpl getNode() {
        return (CellNodeImpl) super.getNode();
    }

}