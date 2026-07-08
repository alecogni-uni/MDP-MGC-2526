package it.unicam.cs.mpgc.rpg126161.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Rappresenta un avversario nel gioco.
 * Gestisce internamente la propria scalabilità di potenza e le decisioni tattiche in battaglia.
 */
@Getter
@Setter
@Entity
public class Mostro extends Entita {

    // --- COSTANTI:  ---
    private static final int INCREMENTO_DANNO_PER_LIVELLO = 5; // Danno extra guadagnato dal mostro per ogni livello di difficoltà
    private static final int INCREMENTO_HP_PER_LIVELLO = 20; // HP massimi extra per ogni livello di difficoltà
    private static final double PROBABILITA_CURA = 0.20; // 20% di probabilità che il mostro provi a curarsi
    private static final double PERCENTUALE_RECUPERO_CURA = 0.15; // Il mostro recupera il 15% della sua vita massima
    // per semplicità di implementazione in questa prima versione ho deciso di lasciare le costanti fisse ma in futuro
    // se si volesse offrire la possibilità di cambiare difficoltà queste diventerebbero parametri


    private int dannoBase; // Il danno fisico base inflitto dal mostro
    private int expFornita; // Punti esperienza dati all'Eroe in caso di sconfitta del mostro
    private int moneteRilasciate; // Monete guadagnate dall'Eroe sconfiggendo questo mostro

    protected Mostro() {}


    public Mostro(String nome, int hp, Elemento elemento, int dannoBase, int exp, int monete) {
        super(nome, hp, elemento);
        this.dannoBase = dannoBase;
        this.expFornita = exp;
        this.moneteRilasciate = monete;
    }

    /**
     * Aumenta le statistiche del mostro in base al progresso nel dungeon (livello/scontro attuale).
     */
    public void scalaDifficolta(int fattore) {
        this.dannoBase += (fattore * INCREMENTO_DANNO_PER_LIVELLO); // Aumenta il danno base scalando col fattore fornito
        this.setPuntiVitaMax(this.getPuntiVitaMax() + (fattore * INCREMENTO_HP_PER_LIVELLO)); // Aumenta la vita massima
        this.ripristinaVitaCompletamente(); // Riporta gli HP attuali al nuovo valore massimo appena calcolato
    }

    /**
     * Il mostro può effettuare il suo attacco o curarsi con una probabilita di PROBABILITA_CURA% (in questo caso costante 20)
     */
    public String eseguiTurno(Combattente bersaglio, Scontro arena) {
        // Controlla se la vita è sotto la metà e tenta la cura
        if (this.getPuntiVitaAttuali() < (this.getPuntiVitaMax() / 2) && Math.random() < PROBABILITA_CURA) {
            return tentaCura(); //esegue la cura e salta l'attacco
        }
        // attacco normale verso il bersaglio
        return arena.eseguiAttacco(this, bersaglio);
    }

    // Metodo privato di supporto per gestire la logica della cura
    private String tentaCura() {
        // Calcola gli HP da recuperare basandosi sulla percentuale fissa della vita massima
        int recupero = (int) (this.getPuntiVitaMax() * PERCENTUALE_RECUPERO_CURA);
        this.curati(recupero);
        return "👹 Il boss " + this.getNome() + " si cura di " + recupero + " HP!"; // stringa per GUI
    }

    @Override
    public String attacca(Combattente bersaglio) {
        // stringa per GUI
        return "☠ Il " + this.getNome() + " si scaglia contro di te!";
    }

    @Override
    public Elemento getElementoAttacco() {
        return this.getElemento();
    }

    @Override
    public int getPotenzaAttacco() {
        return this.dannoBase;
    }
}