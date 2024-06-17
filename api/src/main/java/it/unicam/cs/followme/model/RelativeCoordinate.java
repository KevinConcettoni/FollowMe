package it.unicam.cs.followme.model;

public class RelativeCoordinate {

    /**
     * Calcola la coordinata due relativamente alla prima coordinata
     * @param c1 prima coordinata
     * @param c2 seconda coordinata
     * @return la coordinata che sarà relativa a c1
     */
    public static Coordinate getRelative(Coordinate c1, Coordinate c2){
        return new Coordinate(c1.getX()-c2.getX(),c1.getY()-c2.getY());
    }
}
