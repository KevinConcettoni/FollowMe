package it.unicam.cs.followme.model;

/**
 * Applica una direzione, toglie la responsabilità alla classe Direction
 */
@FunctionalInterface
public interface DirectionApply<D extends Direction, C extends Coordinate> {

    /**
     * La direzione base viene calcolata sommano le coordinate di dafault (0,0)
     * per la time unit
     */
    DirectionApply<Direction,Coordinate> DIRECTION_APPLY = (d,c,t) -> {
        Coordinate coord = d.apply();
        return new Coordinate(c.getX() + coord.getX()*t, c.getY() + coord.getY() * t);
    };

    /**
     * Altrimenti è possibile applicarle passano direzione, coordinate e time unit
     * @return le nuove coordinate
     */
    Coordinate apply (D direction, C coordinate, double time);
}
