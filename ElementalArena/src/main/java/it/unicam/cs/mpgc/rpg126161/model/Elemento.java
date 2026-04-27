package it.unicam.cs.mpgc.rpg126161.model;

/**
 * Gestisce il sistema delle affinità elementali del gioco.
 */
public enum Elemento {
    FUOCO, ACQUA, TERRA, ARIA, FULMINE, PIANTA, LUCE, OMBRA;

    public double getMoltiplicatore(Elemento difensore) {
        // Vantaggi (Danno x2)
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
        // Svantaggi (Danno x0.5)
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
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}