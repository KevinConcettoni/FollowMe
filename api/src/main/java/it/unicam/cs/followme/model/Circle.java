package it.unicam.cs.followme.model;

public class Circle implements Shape{

    private final double radius;
    private final String label;

    /**
     * Costruttore della figura
     * @param r raggio del cerchio
     * @param l label del cerchio
     */
    public Circle (double r, String l){
        if (r <= 0)
            throw new IllegalArgumentException("Raggio non valido");
        this.radius = r;
        this.label = l;
    }

    /**
     * Metodo che indica se una cooridnata è contenuta all'interno della circonfernza
     * @param c la coordinata
     * @return ture se è contenuta, false altrimenti
     */
    @Override
    public boolean isContained(Coordinate c) {
        if (c == null)
            throw new IllegalArgumentException("Coordinate nulle");
        /*  Il punto sarà all'interno del cerchio
          se la sua distanza dall'origine è minore o uguale al raggio*/
        double dis = Math.sqrt((c.getX() * c.getX()) + (c.getY() * c.getY())); // teorema di Pitagora
        return dis <= radius;
    }

    /**
     * @return il tipo della forma
     */
    @Override
    public ShapeEnum getType() {
        return ShapeEnum.CIRCLE;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public double getRadius() {
        return this.radius;
    }
}
