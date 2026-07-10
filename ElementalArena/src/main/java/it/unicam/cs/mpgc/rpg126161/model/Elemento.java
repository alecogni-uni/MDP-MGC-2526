package it.unicam.cs.mpgc.rpg126161.model;

/**
 * Gestisce il sistema delle affinità elementali del gioco.
 * Ogni elemento può risultare in vantaggio, svantaggio o neutro rispetto a un altro.
 */
public enum Elemento {
    FUOCO, ACQUA, TERRA, ARIA, FULMINE, PIANTA, LUCE, OMBRA;

    private static final double MOLTIPLICATORE_VANTAGGIO = 2.0;   // danno raddoppiato in caso di vantaggio
    private static final double MOLTIPLICATORE_SVANTAGGIO = 0.5;  // danno dimezzato in caso di svantaggio
    private static final double MOLTIPLICATORE_NEUTRO = 1.0;      // nessuna variazione

    /**
     * Calcola il moltiplicatore di danno di questo elemento contro l'elemento difensore.
     * @param difensore l'elemento del bersaglio
     * @return il moltiplicatore da applicare al danno (vantaggio, svantaggio o neutro)
     */
    public double getMoltiplicatore(Elemento difensore) {
        // Vantaggi (danno x2)
        if ((this == FUOCO && (difensore == TERRA || difensore == PIANTA)) ||
                (this == TERRA && difensore == ACQUA) ||
                (this == ACQUA && difensore == FUOCO) ||
                (this == PIANTA && (difensore == TERRA || difensore == ACQUA)) ||
                (this == ARIA && (difensore == FULMINE || difensore == PIANTA)) ||
                (this == FULMINE && difensore == ARIA) ||
                (this == LUCE && difensore == OMBRA) ||
                (this == OMBRA && difensore == LUCE)) {
            return MOLTIPLICATORE_VANTAGGIO;
        }
        // Svantaggi (danno x0.5)
        if ((this == TERRA && (difensore == FUOCO || difensore == PIANTA)) ||
                (this == PIANTA && difensore == FUOCO) ||
                (this == ACQUA && difensore == TERRA) ||
                (this == FUOCO && difensore == ACQUA) ||
                (this == FULMINE && difensore == TERRA)) {
            return MOLTIPLICATORE_SVANTAGGIO;
        }
        return MOLTIPLICATORE_NEUTRO;
    }

    /**
     * Restituisce il nome dell'elemento con la sola iniziale maiuscola (es. "Fuoco").
     * @return il nome formattato per la visualizzazione
     */
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}