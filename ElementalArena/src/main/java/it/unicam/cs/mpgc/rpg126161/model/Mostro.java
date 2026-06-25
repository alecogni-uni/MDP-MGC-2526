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

    private int dannoBase;
    private int expFornita;
    private int moneteRilasciate;

    protected Mostro() {}

    public Mostro(String nome, int hp, Elemento elemento, int dannoBase, int exp, int monete) {
        super(nome, hp, elemento);
        this.dannoBase = dannoBase;
        this.expFornita = exp;
        this.moneteRilasciate = monete;
    }

    /**
     * Aumenta le statistiche del mostro in base al progresso nel dungeon.
     */
    public void scalaDifficolta(int fattore) {
        this.dannoBase += (fattore * 5);
        this.setPuntiVitaMax(this.getPuntiVitaMax() + (fattore * 20));
        this.ripristinaVitaCompletamente();
    }

    /**
     * Il mostro può curarsi (probabilità 20% sotto metà vita) o attaccare.
     */
    public String eseguiTurno(Combattente bersaglio, Scontro arena) {
        if (this.getPuntiVitaAttuali() < (this.getPuntiVitaMax() / 2) && Math.random() < 0.20) {
            return tentaCura();
        }
        return arena.eseguiAttacco(this, bersaglio);
    }

    private String tentaCura() {
        int recupero = (int) (this.getPuntiVitaMax() * 0.15);
        this.curati(recupero);
        return "👹 Il boss " + this.getNome() + " si cura di " + recupero + " HP!";
    }

    @Override
    public String attacca(Combattente bersaglio) {
        return "☠ Il " + this.getNome() + " si scaglia contro di te!";
    }

    @Override
    public Elemento getElementoAttacco() {
        return this.getElemento();
    }
}