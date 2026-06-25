package it.unicam.cs.mpgc.rpg126161.model;

/**
 * Gestisce la logica di scontro tra due combattenti.
 * Funge da "arbitro": coordina gli attacchi e applica i danni,
 * mantenendo la logica di gioco separata dalle entità.
 */
public class Scontro {

    private final Combattente eroe;
    private final Combattente nemico;
    private int turno;

    public Scontro(Combattente eroe, Combattente nemico) {
        this.eroe = eroe;
        this.nemico = nemico;
        this.turno = 1;
    }

    /**
     * Esegue l'attacco tra due combattenti.
     * Centralizza il calcolo dei danni e l'applicazione al difensore.
     */
    public String eseguiAttacco(Combattente attaccante, Combattente difensore) {
        // Calcolo basato sugli elementi
        double mult = attaccante.getElementoAttacco().getMoltiplicatore(difensore.getElementoDifesa());

        // Estrazione valore offensivo polimorfico
        int forza = getForzaCombattente(attaccante);
        int danno = (int) (forza * mult);

        difensore.riceviDanno(danno);
        return attaccante.getNome() + " colpisce " + difensore.getNome() + " per " + danno + " danni!";
    }

    private int getForzaCombattente(Combattente c) {
        if (c instanceof Eroe) return ((Eroe) c).getForza() + (c.getElementoAttacco() != null ? 5 : 0); // Esempio logica
        if (c instanceof Mostro) return ((Mostro) c).getDannoBase();
        return 5; // Default
    }

    public String eseguiTurnoNemico() {
        turno++;
        return nemico.isVivo() ? eseguiAttacco(nemico, eroe) : "";
    }
}