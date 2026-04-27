package it.unicam.cs.mpgc.rpg126161.model;

import lombok.Getter;

/**
 * Classe base astratta per tutte le creature viventi del gioco.
 * Implementa i comportamenti comuni dell'interfaccia Combattente.
 */
@Getter
public abstract class Entita implements Combattente {

    private String nome;
    private int puntiVitaMax;
    private int puntiVitaAttuali;
    private Elemento elemento;

    public Entita(String nome, int puntiVitaMax, Elemento elemento) {
        this.nome = nome;
        this.puntiVitaMax = puntiVitaMax;
        this.puntiVitaAttuali = puntiVitaMax;
        this.elemento = elemento;
    }

    @Override
    public void riceviDanno(int danno) {
        this.puntiVitaAttuali -= danno;
        if (this.puntiVitaAttuali < 0) {
            this.puntiVitaAttuali = 0;
        }
    }

    /**
     * Ripristina i punti vita senza superare il limite massimo.
     */
    public void curati(int quantita) {
        // Blocco di sicurezza: se l'entità non è viva, non può essere curata
        if (!this.isVivo()) {
            System.out.println("💀 " + this.nome + " è a terra! Le cure normali non hanno effetto.");
            return;
        }

        this.puntiVitaAttuali += quantita;
        if (this.puntiVitaAttuali > this.puntiVitaMax) {
            this.puntiVitaAttuali = this.puntiVitaMax;
        }
    }

    @Override
    public boolean isVivo() {
        return this.puntiVitaAttuali > 0;
    }

    @Override
    public Elemento getElementoDifesa() {
        return this.elemento; // difesa col proprio elemento naturale
    }
}