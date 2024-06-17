package it.unicam.cs.followme.model;

public class Coordinate {

    private final double x;
    private final double y;

    /**
     * Costruttore di una cordinate dati i valori di x e y
     * @param x coordinata delle ascisse
     * @param y coordinata delle ordinate
     */
    public Coordinate(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    @Override
    public boolean equals(Object c){
        if(this==c)
            return true;
        if(c == null || getClass()!= c.getClass())
            return false;
        Coordinate that=(Coordinate) c;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0;
    }
}
