package it.unicam.cs.followme.model;

/**
 * Interfaccia che rappresenta un comando
 */
public interface Command <E extends ProgrammableEntity<Direction>, S extends Shape> {

    /**
     * applica un determinato comando su un ambiente passato come parametro
     * @param environment l'ambiente specificato
     */
    void apply (Environment<E,S> environment);

}
