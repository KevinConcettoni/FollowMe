package it.unicam.cs.followme.parsing;

import it.unicam.cs.followme.model.Coordinate;
import it.unicam.cs.followme.model.Shape;
import it.unicam.cs.followme.utilities.ShapeData;

import java.util.List;
import java.util.Map;

/**
 * Genera un mappa di figure con le relative coordinate del centro
 * @param <S> figure
 */
public interface ShapeMapBuilder<S extends Shape> {

    /**
     * Costruisce la mappa di shapes partrendo da una lista dh shapedata
     * @param list lista di shapedata
     * @return la mappa costruita
     */
    Map<S, Coordinate> buildMap(List<ShapeData> list);
}
