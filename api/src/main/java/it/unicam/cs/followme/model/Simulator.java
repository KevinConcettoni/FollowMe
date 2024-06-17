package it.unicam.cs.followme.model;

/**
 * Interfaccia che consente la simulazione di alcuni comandi predefiniti
 * per un certo numero di passi
 */
public interface Simulator {

    /**
     * Metodo che consente di simulare
     * @param dt tempo che richiederà ogni comando per l'esecuzione
     * @param time tempo totale della simulazione
     */
    void simulate(double dt, double time);

}
