package it.unicam.cs.mpgc.rpg126161.model;

import lombok.Getter;

/**
 * Rappresenta il protagonista del gioco.
 * Questa classe delega la gestione degli oggetti alla classe Inventario (Principio SRP).
 */
@Getter
public class Eroe extends Entita {

    private int livello;
    private int esperienza;
    private int forza;
    private int monete;

    private Arma armaEquipaggiata;
    private Inventario inventario; // Composizione: l'Eroe HA UN inventario

    /**
     * Costruttore dell'Eroe.
     */
    public Eroe(String nome, Elemento elementoBase) {
        super(nome, 100, elementoBase); // Vita base 100
        this.livello = 1;
        this.esperienza = 0;
        this.forza = 5;
        this.monete = 0;
        this.inventario = new Inventario();
    }

    // --- GESTIONE OGGETTI (Interazione con l'Inventario) ---

    /**
     * Usa un oggetto presente nell'inventario in base all'indice.
     * Grazie al polimorfismo, l'oggetto saprà se deve curare o equipaggiarsi.
     */
    public void usaOggettoDallInventario(int indice) {
        Oggetto o = inventario.getOggetto(indice);
        if (o != null) {
            o.usa(this); // Chiama il metodo polimorfico

            // Se l'oggetto è una pozione, viene consumato e rimosso
            if (o instanceof Pozione) {
                inventario.rimuovi(o);
            }
        } else {
            System.out.println("❌ Oggetto non trovato all'indice selezionato.");
        }
    }

    /**
     * Metodo chiamato internamente dall'oggetto Arma quando viene usato.
     */
    public void equipaggiaArma(Arma nuovaArma) {
        this.armaEquipaggiata = nuovaArma;
        System.out.println("🔄 " + getNome() + " ora impugna: " + nuovaArma.getNome());
    }

    // --- LOGICA ECONOMIA ---

    public void aggiungiMonete(int quantita) {
        this.monete += quantita;
        System.out.println("💰 Portafoglio aggiornato: " + this.monete + " monete.");
    }

    public boolean spendiMonete(int costo) {
        if (this.monete >= costo) {
            this.monete -= costo;
            return true;
        }
        return false;
    }

    // --- LOGICA COMBATTIMENTO (Implementazione Combattente) ---

    @Override
    public Elemento getElementoAttacco() {
        // Se ha un'arma, attacca con l'elemento dell'arma, altrimenti col suo
        return (armaEquipaggiata != null) ? armaEquipaggiata.getElemento() : this.getElemento();
    }

    @Override
    public void attacca(Combattente bersaglio) {
        String nomeFonte = (armaEquipaggiata != null) ? armaEquipaggiata.getNome() : "Mani Nude";
        int dannoFonte = (armaEquipaggiata != null) ? armaEquipaggiata.getDannoBase() : 0;

        double moltiplicatore = this.getElementoAttacco().getMoltiplicatore(bersaglio.getElementoDifesa());
        int dannoFinale = (int) ((this.forza + dannoFonte) * moltiplicatore);

        System.out.println("🗡️ " + this.getNome() + " attacca " + bersaglio.getNome() + " con " + nomeFonte + "!");
        bersaglio.riceviDanno(dannoFinale);
        System.out.println("💥 Danno inflitto: " + dannoFinale + " (Moltiplicatore: x" + moltiplicatore + ")\n");
    }

    // --- LOGICA PROGRESSIONE ---

    public void guadagnaEsperienza(int expOttenuta) {
        this.esperienza += expOttenuta;
        System.out.println("🌟 Esperienza guadagnata: +" + expOttenuta);

        // Sistema di Level Up ogni 100 XP
        if (this.esperienza >= 100) {
            this.livello++;
            this.esperienza -= 100;
            this.forza += 3; // L'Eroe diventa più forte
            this.curati(this.getPuntiVitaMax()); // Cura completa al passaggio di livello
            System.out.println("🎉 LEVEL UP! " + getNome() + " è ora al livello " + this.livello + "!");
        }
    }
}