package it.unicam.cs.followme.model;

public interface Shape {

    /**
     * Stabilisce se un punto è contenuto all'interno della figura
     * @param c la coordinata
     * @return se la coordinata è contenuta oppure no
     */
    boolean isContained(Coordinate c);

    /**
     * @return label della shape
     */
    String getLabel();

    /**
     * @return il tipo di figura (rettangolo o cerchio)
     */
    ShapeEnum getType();
}
