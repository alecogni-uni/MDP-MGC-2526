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

    private static final int INCREMENTO_DANNO_PER_LIVELLO = 5;    // danno extra per ogni livello di difficoltà
    private static final int INCREMENTO_HP_PER_LIVELLO = 20;      // HP massimi extra per ogni livello di difficoltà
    private static final double PROBABILITA_CURA = 0.20;          // 20% di probabilità che il mostro provi a curarsi
    private static final double PERCENTUALE_RECUPERO_CURA = 0.15; // recupera il 15% della vita massima
    private static final int DIVISORE_META_VITA = 2;             // soglia sotto cui il mostro valuta la cura (metà vita)
    // Per semplicità di implementazione in questa prima versione le costanti sono fisse; in futuro,
    // per offrire livelli di difficoltà selezionabili, diventerebbero parametri configurabili.

    private int dannoBase;        // danno fisico base inflitto dal mostro
    private int expFornita;       // esperienza data all'Eroe quando il mostro viene sconfitto
    private int moneteRilasciate; // monete guadagnate dall'Eroe sconfiggendo questo mostro

    protected Mostro() {}

    public Mostro(String nome, int hp, Elemento elemento, int dannoBase, int exp, int monete) {
        super(nome, hp, elemento);
        this.dannoBase = dannoBase;
        this.expFornita = exp;
        this.moneteRilasciate = monete;
    }

    /**
     * Aumenta le statistiche del mostro in base al progresso nel dungeon.
     * @param fattore il livello di difficoltà da applicare (0 = statistiche base)
     */
    public void scalaDifficolta(int fattore) {
        this.dannoBase += (fattore * INCREMENTO_DANNO_PER_LIVELLO);
        this.setPuntiVitaMax(this.getPuntiVitaMax() + (fattore * INCREMENTO_HP_PER_LIVELLO));
        this.ripristinaVitaCompletamente(); // riporta gli HP attuali al nuovo massimo
    }

    /**
     * Esegue il turno del mostro: se la vita è sotto la metà tenta la cura (con probabilità PROBABILITA_CURA),
     * altrimenti attacca il bersaglio.
     * @param bersaglio il combattente da attaccare
     * @param arena l'arbitro che applica il danno
     * @return il messaggio di log dell'azione svolta
     */
    public String eseguiTurno(Combattente bersaglio, Scontro arena) {
        boolean sottoMetaVita = this.getPuntiVitaAttuali() < (this.getPuntiVitaMax() / DIVISORE_META_VITA);
        if (sottoMetaVita && Math.random() < PROBABILITA_CURA) {
            return tentaCura(); // la cura sostituisce l'attacco per questo turno
        }
        return arena.eseguiAttacco(this, bersaglio);
    }

    /**
     * Cura il mostro di una quota fissa della sua vita massima.
     * @return il messaggio di log della cura
     */
    private String tentaCura() {
        int recupero = (int) (this.getPuntiVitaMax() * PERCENTUALE_RECUPERO_CURA);
        this.curati(recupero);
        return "👹 Il boss " + this.getNome() + " si cura di " + recupero + " HP!"; // stringa per GUI
    }

    /**
     * {@inheritDoc}
     * @return il messaggio di log dell'attacco del mostro
     */
    @Override
    public String attacca(Combattente bersaglio) {
        return "☠ Il " + this.getNome() + " si scaglia contro di te!"; // stringa per GUI
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