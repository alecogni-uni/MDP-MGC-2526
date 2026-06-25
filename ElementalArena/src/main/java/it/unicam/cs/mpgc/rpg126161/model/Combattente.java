package it.unicam.cs.mpgc.rpg126161.model;

/**
 * Interfaccia che definisce le capacità base di un'entità in grado di partecipare a uno scontro.
 * Grazie al polimorfismo, permette di gestire attacchi e difesa
 * indipendentemente dal tipo di partecipante (Eroe o Mostro).
 */
public interface Combattente {

    String getNome();

    boolean isVivo();

    /**
     * Applica il danno subito all'entità, aggiornando i Punti Vita.
     * @param danno Il valore numerico del danno da sottrarre.
     */
    void riceviDanno(int danno);

    //elementi
    Elemento getElementoAttacco();
    Elemento getElementoDifesa();

    /**
     * Esegue un'azione di attacco contro un bersaglio.
     * @param bersaglio L'entità che subirà l'attacco.
     * @return Una stringa descrittiva dell'azione (log di battaglia).
     */
    String attacca(Combattente bersaglio);
}