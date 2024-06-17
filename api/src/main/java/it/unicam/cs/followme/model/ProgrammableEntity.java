package it.unicam.cs.followme.model;

/**
 * Definisce le entità che si possono muovere all'interno dell'ambiente
 */
public interface ProgrammableEntity <D extends Direction> {

    /**
     * Ritorna la direzione dell'entità
     */
    D getDirection();

    /**
     * Setta la direzione dell'entità
     * @param direction la direzione da applicargli
     */
    void setDirection(D direction);
    /**
     * @return la label segnalata dalle entità
     */
    String getLabel();

    /**
     *
     * @param label etichetta da segnalare
     */
    void signalLabel(String label);

    /**
     *
     * @param label etichetta che l'entità non deve più segnalare
     */
    void unsignalLabel(String label);


}
