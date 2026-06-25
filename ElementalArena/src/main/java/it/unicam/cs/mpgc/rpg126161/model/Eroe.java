package it.unicam.cs.mpgc.rpg126161.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Rappresenta il protagonista della partita.
 * Estende {@link Entita} aggiungendo logiche di progressione (livello, esperienza)
 * e la gestione dell'inventario e dell'equipaggiamento.
 */
@Getter
@Setter
@Entity
public class Eroe extends Entita {

    private int livello;
    private int esperienza;
    private int forza;
    private int monete;
    private int progressoDungeon;

    @OneToOne(cascade = CascadeType.ALL)
    private Arma armaEquipaggiata;

    @OneToOne(cascade = CascadeType.ALL)
    private Inventario inventario;

    protected Eroe() {}

    public Eroe(String nome, Elemento elementoBase) {
        super(nome, 70, elementoBase);
        this.livello = 1;
        this.esperienza = 0;
        this.forza = 8;
        this.monete = 70;
        this.progressoDungeon = 0;
        this.inventario = new Inventario();
    }

    public void equipaggiaArma(Arma arma) {
        this.armaEquipaggiata = arma;
    }

    public void aggiungiMonete(int quantita) {
        this.monete += quantita;
    }

    public boolean spendiMonete(int costo) {
        if (this.monete >= costo) {
            this.monete -= costo;
            return true;
        }
        return false;
    }

    /**
     * Delega l'utilizzo e la rimozione dell'oggetto all'inventario.
     * Principio di Responsabilità Singola: l'Eroe non deve sapere come funziona l'inventario.
     */
    public void usaOggettoDallInventario(int indice) {
        this.inventario.consumaOggetto(indice, this);
    }

    public void avanzaNelDungeon() {
        this.progressoDungeon++;
    }

    /**
     * Gestisce l'aumento di livello.
     * Incrementa forza e salute, ripristinando la vita dopo il passaggio di livello.
     */
    public void guadagnaEsperienza(int expOttenuta) {
        this.esperienza += expOttenuta;
        while (this.esperienza >= 100) {
            this.livello++;
            this.esperienza -= 100;
            this.forza += 1;
            this.setPuntiVitaMax(this.getPuntiVitaMax() + 5);
            this.ripristinaVitaCompletamente();
        }
    }

    @Override
    public Elemento getElementoAttacco() {
        return (armaEquipaggiata != null) ? armaEquipaggiata.getElemento() : this.getElemento();
    }

    /**
     * L'Eroe dichiara l'intenzione di attaccare.
     * Il calcolo del danno e l'applicazione al bersaglio sono delegati alla classe {@link Scontro}.
     */
    @Override
    public String attacca(Combattente bersaglio) {
        return "⚔ " + this.getNome() + " si prepara ad attaccare con " +
                (armaEquipaggiata != null ? armaEquipaggiata.getNome() : "Mani Nude");
    }
}