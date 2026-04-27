package it.unicam.cs.mpgc.rpg126161.model;

import lombok.Getter;

@Getter
public class Arma extends Oggetto {
    private int dannoBase;
    private Elemento elemento;

    public Arma(String nome, int dannoBase, Elemento elemento, int valore) {
        super(nome, valore); // Passa nome e valore alla classe base
        this.dannoBase = dannoBase;
        this.elemento = elemento;
    }

    @Override
    public void usa(Eroe eroe) {
        eroe.equipaggiaArma(this);
    }
}