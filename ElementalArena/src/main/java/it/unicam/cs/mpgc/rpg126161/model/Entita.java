package it.unicam.cs.mpgc.rpg126161.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

/**
 * Classe base astratta che modella qualsiasi entità dotata di punti vita e nome.
 * Implementa {@link Combattente} per garantire la compatibilità con il sistema di scontro.
 * Utilizza la strategia di mapping JOINED per mantenere separate le tabelle nel DB.
 */
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Entita implements Combattente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int puntiVitaMax;
    private int puntiVitaAttuali;

    @Enumerated(EnumType.STRING)
    private Elemento elemento;

    protected Entita() {}

    public Entita(String nome, int puntiVitaMax, Elemento elemento) {
        this.nome = nome;
        this.puntiVitaMax = puntiVitaMax;
        this.puntiVitaAttuali = puntiVitaMax;
        this.elemento = elemento;
    }

    /**
     * Applica il danno subito, assicurandosi che i punti vita non scendano mai sotto lo zero.
     */
    @Override
    public void riceviDanno(int danno) {
        this.puntiVitaAttuali = Math.max(0, this.puntiVitaAttuali - danno);
    }

    /**
     * Ripristina i punti vita senza superare il limite massimo (puntiVitaMax).
     */
    public void curati(int quantita) {
        if (!this.isVivo()) return;
        this.puntiVitaAttuali = Math.min(this.puntiVitaMax, this.puntiVitaAttuali + quantita);
    }

    @Override
    public boolean isVivo() {
        return this.puntiVitaAttuali > 0;
    }

    @Override
    public Elemento getElementoDifesa() {
        return this.elemento;
    }

    /**
     * Resetta lo stato vitale all'inizio di un nuovo incontro.
     */
    public void ripristinaVitaCompletamente() {
        this.puntiVitaAttuali = this.puntiVitaMax;
    }
}