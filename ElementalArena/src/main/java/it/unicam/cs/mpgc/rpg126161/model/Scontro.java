package it.unicam.cs.mpgc.rpg126161.model;

/**
 * Gestisce la logica di scontro tra due combattenti.
 * Funge da "arbitro": coordina gli attacchi e applica i danni,
 * mantenendo la logica di gioco separata dalle entità.
 */
public class Scontro {

    private final Combattente eroe;
    private final Combattente nemico;


    public Scontro(Combattente eroe, Combattente nemico) {
        this.eroe = eroe;
        this.nemico = nemico;
    }

    /**
     * Esegue l'attacco tra due combattenti sfruttando il polimorfismo.
     */
    public String eseguiAttacco(Combattente attaccante, Combattente difensore) {
        // Calcolo basato sugli elementi
        double mult = attaccante.getElementoAttacco().getMoltiplicatore(difensore.getElementoDifesa());

        // uso interfaccia per ottenere la forza di attacco
        int forza = attaccante.getPotenzaAttacco();

        int danno = (int) (forza * mult);

        difensore.riceviDanno(danno);
        return attaccante.getNome() + " colpisce " + difensore.getNome() + " per " + danno + " danni!";
    }

}