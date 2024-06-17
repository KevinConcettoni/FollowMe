package it.unicam.cs.followme.model;

public class Rectangle implements Shape{

    private final double base, height;
    private final String label;

    /**
     * Costruttore dell'oggetto
     * @param b base
     * @param h altezza
     * @param l label
     */
    public Rectangle (double b, double h, String l){
        if (b <= 0 || h <= 0)
            throw new IllegalArgumentException("Parametri non validi");
        this.base = b;
        this.height = h;
        this.label = l;
    }

    /**
     * Ci dice se una determinata posizione è contenuta all'interno della figura
     * @param c la coordinata
     * @return true se è contenuta, false altrimenti
     */
    @Override
    public boolean isContained(Coordinate c) {
        if (c == null)
            throw new IllegalArgumentException("Coordinate nulle");
        return c.getX() < this.base && c.getY() < this.height;
    }

    /**
     * @return tipo di figura
     */
    @Override
    public ShapeEnum getType() {
        return ShapeEnum.RECTANGLE;
    }

    public double getBase() {
        return this.base;
    }

    public double getHeight() {
        return this.height;
    }

    @Override
    public String getLabel() {
        return this.label;
    }
}
