package it.unicam.cs.mpgc.rpg126161.model;

/**
 * Interfaccia che astrae il concetto di partecipante a uno scontro.
 */
public interface Combattente {
    String getNome();
    boolean isVivo();
    void riceviDanno(int danno);

    Elemento getElementoAttacco();
    Elemento getElementoDifesa();

    void attacca(Combattente bersaglio);
}