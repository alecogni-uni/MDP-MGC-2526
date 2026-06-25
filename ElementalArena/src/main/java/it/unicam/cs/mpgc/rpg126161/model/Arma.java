package it.unicam.cs.mpgc.rpg126161.model;

import lombok.Getter;
import jakarta.persistence.*;

/**
 * Rappresenta un'arma equipaggiabile nel gioco.
 * Estende la classe base Oggetto.
 */
@Getter
@Entity
public class Arma extends Oggetto {

    private int dannoBase;

    @Enumerated(EnumType.STRING)
    private Elemento elemento;

    // Costruttore per JPA
    protected Arma() {}

    public Arma(String nome, int dannoBase, Elemento elemento, int valore) {
        super(nome, valore);
        this.dannoBase = dannoBase;
        this.elemento = elemento;
    }

    /**
     * Implementa la logica di utilizzo specifica per le armi:
     * l'oggetto viene equipaggiato sull'eroe anziché essere consumato.
     */
    @Override
    public void usa(Eroe eroe) {
        eroe.equipaggiaArma(this);
    }
}