package it.unicam.cs.mpgc.rpg126161.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

/**
 * Rappresenta una sessione di gioco (Save Slot) persistente.
 * Aggrega l'Eroe e il Negozio di quella specifica partita.
 */
@Getter
@Setter
@Entity
public class Partita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Eroe eroe;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Negozio negozio;

    protected Partita() {} // Per JPA

    public Partita(Eroe eroe, Negozio negozio) {
        this.eroe = eroe;
        this.negozio = negozio;
    }
}