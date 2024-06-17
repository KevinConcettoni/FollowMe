package it.unicam.cs.followme.parsing;

import it.unicam.cs.followme.model.Circle;
import it.unicam.cs.followme.model.Rectangle;
import it.unicam.cs.followme.model.Coordinate;
import it.unicam.cs.followme.model.Shape;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

import it.unicam.cs.followme.utilities.ShapeData;

/**
 * Classe che costruisce cerchi o rettangoli per l'ambiente
 */
public class ShapeFactory implements ShapeMapBuilder<Shape> {

    /**
     * Ritorna una mappa di figure con relative coordinate (riferite al centro)
     * @param l lista di ShapeData
     */
    @Override
    public Map<Shape, Coordinate> buildMap(List<ShapeData> l) {
        return l.stream()
                .collect(Collectors.toMap(
                        k -> k.shape().equals("RECTANGLE") ? new Rectangle(k.args()[3], k.args()[2], k.label())
                                : new Circle(k.args()[2], k.label()),
                        v -> new Coordinate(v.args()[0], v.args()[1])
                ));
    }
}
