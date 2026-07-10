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

    @Override
    public Oggetto copia() {
        return this; // restituisce l'arma stesa, senza clonarla.
    }

    @Override
    public String getIconPath() {
        return "/images/sword.png";
    }

    @Override
    public String getDescrizione() {
        // nome seguito da elemento e danno
        return getNome() + " " + elemento + " | ATK: " + dannoBase;
    }

    @Override
    public String getEtichettaAzione() {
        return "Equipaggia";
    }

    @Override
    public boolean isEquipaggiabile() {
        return true;
    }

    /**
     * L'arma è equipaggiata se coincide (per nome) con quella attualmente impugnata dall'eroe.
     */
    @Override
    public boolean isEquipaggiato(Eroe eroe) {
        Arma equipaggiata = eroe.getArmaEquipaggiata();
        return equipaggiata != null && getNome().equals(equipaggiata.getNome());
    }
}