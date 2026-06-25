package it.unicam.cs.mpgc.rpg126161;

import it.unicam.cs.mpgc.rpg126161.model.Partita;
import it.unicam.cs.mpgc.rpg126161.repository.PartitaRepository;

/**
 * Mantiene lo stato globale della sessione di gioco attiva in JavaFX.
 */
public class Sessione {
    private static Partita partitaCorrente;
    private static final PartitaRepository partitaRepo = new PartitaRepository();

    public static Partita getPartitaCorrente() {
        return partitaCorrente;
    }

    public static void setPartitaCorrente(Partita partita) {
        partitaCorrente = partita;
    }

    public static PartitaRepository getPartitaRepo() {
        return partitaRepo;
    }
}