package it.unicam.cs.mpgc.rpg126161.model;

/**
 * Gestisce il sistema delle affinità degli elementi del gioco.
 */
public enum Elemento {
    FUOCO, ACQUA, TERRA, ARIA, FULMINE, PIANTA, LUCE, OMBRA;

    /**
     * Calcola il moltiplicatore di danno basato sull'interazione tra elementi.
     * * @param difensore L'elemento del bersaglio.
     * @return 2.0 se efficace, 0.5 se resistito, 1.0 se neutro.
     */
    public double getMoltiplicatore(Elemento difensore) {
        // --- VANTAGGI (Danno x2) ---
        if ((this == FUOCO && (difensore == TERRA || difensore == PIANTA)) ||
                (this == TERRA && difensore == ACQUA) ||
                (this == ACQUA && difensore == FUOCO) ||
                (this == PIANTA && (difensore == TERRA || difensore == ACQUA)) ||
                (this == ARIA && (difensore == FULMINE || difensore == PIANTA)) ||
                (this == FULMINE && difensore == ARIA) ||
                (this == LUCE && difensore == OMBRA) ||
                (this == OMBRA && difensore == LUCE)) {
            return 2.0;
        }

        // --- SVANTAGGI (Danno x0.5) ---
        if ((this == TERRA && (difensore == FUOCO || difensore == PIANTA)) ||
                (this == PIANTA && difensore == FUOCO) ||
                (this == ACQUA && difensore == TERRA) ||
                (this == FUOCO && difensore == ACQUA) ||
                (this == FULMINE && difensore == TERRA)) {
            return 0.5;
        }

        return 1.0; // Neutro
    }

    @Override
    public String toString() {
        // Formatta il nome: FUOCO -> Fuoco
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}