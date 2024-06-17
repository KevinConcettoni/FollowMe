package it.unicam.cs.followme.model;

/**
 * Questa classe rappresenta lo spostamento di un entità in una certa direzione
 * (in una data coordinata) durante un lasso di tempo.
 */
public class Direction {

    private final double x, y;
    private final double speed;

    /**
     * Costruttore del metodo
     * @param x coordinata delle ascisse
     * @param y coordinata delle ordinate
     * @param speed velocità di movimento
     * @throws IllegalArgumentException cooridnate o velocità non congrue
     */
    public Direction(double x, double y, double speed){
        if (speed <= 0)
            throw new IllegalArgumentException("Velocità incongruente");
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    /**
     * Costruttore della direzine passano una coordinata ed un velocità
     * @param c coordinata formata da dai punti x e y
     * @param speed la velocità
     */
    public Direction (Coordinate c, double speed){
        this (c.getX(), c.getY(), speed);
    }

    /**
     * Applica la direzione
     * @return la coordinata applicata
     */
    public Coordinate apply(){
        return new Coordinate(this.x * speed, this.y * speed);
    }

}
