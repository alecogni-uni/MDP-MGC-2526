package it.unicam.cs.mpgc.rpg126161.model;

import lombok.Getter;
import jakarta.persistence.*;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Oggetto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int valore;


    protected Oggetto() {}

    public Oggetto(String nome, int valore) {
        this.nome = nome;
        this.valore = valore;
    }

    /**
     * Applica l'effetto dell'oggetto sull'eroe.
     */
    public abstract void usa(Eroe eroe);

    /**
     * Ritorna se l'oggetto deve essere rimosso dopo l'uso.
     * Di default è false per tutti gli oggetti (es. armi, equipaggiamenti).
     * Verrà sovrascritto (Override) impostandolo a true solo nelle classi consumabili (es. Pozione).
     */
    public boolean isConsumabile() {
        return false;
    }

    /**
     * Restituisce l'istanza da consegnare all'Eroe in fase di acquisto.
     */
    public abstract Oggetto copia();

    /**
     * Indica se l'oggetto è un pezzo unico nel negozio o se ha scorte infinite.
     * Di default è true (viene rimosso dal negozio dopo l'acquisto).
     */
    public boolean isPezzoUnico() {
        return true;
    }
}