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
     * Viene sovrascritto a true solo nelle classi consumabili (es. Pozione).
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

    /**
     * Percorso dell'icona associata a questo tipo di oggetto.
     * Ogni sottoclasse dichiara la propria immagine: la vista non deve
     * conoscere i tipi concreti per scegliere l'icona.
     */
    public abstract String getIconPath();

    /**
     * Descrizione sintetica delle statistiche mostrata nella card dell'inventario.
     */
    public abstract String getDescrizione();

    /**
     * Etichetta dell'azione principale sul bottone della card.
     * Di default "Usa"; le sottoclassi possono sovrascriverla.
     */
    public String getEtichettaAzione() {
        return "Usa";
    }

    /**
     * Indica se l'oggetto occupa uno slot di equipaggiamento (es. armi)
     * anziché essere consumato. Di default false.
     */
    public boolean isEquipaggiabile() {
        return false;
    }

    /**
     * Indica se questo oggetto risulta attualmente equipaggiato dall'eroe.
     * Di default false; viene sovrascritto dagli oggetti equipaggiabili.
     */
    public boolean isEquipaggiato(Eroe eroe) {
        return false;
    }
}