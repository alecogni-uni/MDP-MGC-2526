package it.unicam.cs.mpgc.rpg126161.model;

import lombok.Getter;

/**
 * Classe base per tutti gli oggetti del gioco (Armi, Pozioni, etc.)
 */
@Getter
public abstract class Oggetto {
    private String nome;
    private int valore;

    public Oggetto(String nome, int valore) {
        this.nome = nome;
        this.valore = valore;
    }

    // Metodo astratto: ogni oggetto si "usa" in modo diverso
    public abstract void usa(Eroe eroe);
}