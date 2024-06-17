package it.unicam.cs.followme.model;

public class Robot<D extends Direction> implements ProgrammableEntity<D>{

    private String label;
    private D direction;

    /**
     * Costruttore del robot
     * @param direction direzine che il robot dovrà seguire
     */
    public Robot (D direction){
        this.direction = direction;
        this.label = "";
    }

    /**
     * @param l label che il robot dovrà segnalare
     */
    @Override
    public void signalLabel(String l){
        /* label è un identificativo costituito da una
        sequenza di caratteri alfanumerici e dal simbolo ‘_’*/
        if (l.matches("^[_|[a-zA-Z0-9]]+$")) // uso un espressione regolare per verificare
            this.label = l;
    }

    @Override
    public void unsignalLabel(String l) {
        if (this.label.equals(l))
            this.label = "";
    }

    /**
     * @return la direzione del robot
     */
    @Override
    public D getDirection() {
        return this.direction;
    }

    /**
     * @param d la nuova direzione da seguire
     */
    @Override
    public void setDirection(D d) {
        this.direction = d;
    }


    /**
     * @return la label del robot
     */
    @Override
    public String getLabel() {
        return this.label;
    }
}
