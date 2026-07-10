package it.unicam.cs.mpgc.rpg126161.controller;

import it.unicam.cs.mpgc.rpg126161.MainGUI;
import it.unicam.cs.mpgc.rpg126161.Sessione;
import it.unicam.cs.mpgc.rpg126161.model.*;
import it.unicam.cs.mpgc.rpg126161.utils.CaricatoreArmi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Controller per la creazione di una nuova partita a partire dal nome scelto dal giocatore.
 */
public class NuovaPartitaController {

    private static final int MONETE_INIZIALI = 30;
    private static final String NOME_POZIONE_INIZIALE = "Pozione Piccola";
    private static final int CURA_POZIONE_INIZIALE = 25;
    private static final int VALORE_POZIONE_INIZIALE = 10;
    private static final String NOME_ELISIR_NEGOZIO = "Elisir Totale";
    private static final int CURA_ELISIR_NEGOZIO = 100;
    private static final int VALORE_ELISIR_NEGOZIO = 60;

    @FXML private TextField campoNomeNuovoEroe;

    /**
     * Crea eroe e negozio iniziali dal nome inserito, salva la partita e apre la home.
     * Non fa nulla se il campo nome è vuoto.
     */
    @FXML
    public void handleNuovoEroe(ActionEvent event) {
        String nome = campoNomeNuovoEroe.getText().trim();
        if (nome.isEmpty()) return;

        Eroe eroe = creaEroeIniziale(nome);
        Negozio negozio = creaNegozioIniziale(nome);

        Partita nuovaPartita = new Partita(eroe, negozio);
        Sessione.getPartitaRepo().salvaPartita(nuovaPartita);
        Sessione.setPartitaCorrente(nuovaPartita);

        MainGUI.cambiaScena("/home.fxml");
    }

    /**
     * Costruisce l'eroe di partenza con monete e pozione iniziali.
     * @param nome il nome scelto dal giocatore
     * @return l'eroe pronto per la nuova partita
     */
    private Eroe creaEroeIniziale(String nome) {
        Eroe eroe = new Eroe(nome, Elemento.LUCE);
        eroe.aggiungiMonete(MONETE_INIZIALI);
        eroe.getInventario().aggiungi(
                new Pozione(NOME_POZIONE_INIZIALE, CURA_POZIONE_INIZIALE, VALORE_POZIONE_INIZIALE));
        return eroe;
    }

    /**
     * Costruisce il negozio iniziale con le armi caricate da JSON e pozioni
     * @param nomeEroe il nome dell'eroe, usato per intitolare l'emporio
     * @return il negozio pronto per la nuova partita
     */
    private Negozio creaNegozioIniziale(String nomeEroe) {
        Negozio negozio = new Negozio("Emporio di " + nomeEroe);
        negozio.popolaNegozio(CaricatoreArmi.caricaArmiDaJson());
        negozio.aggiungiArticolo(
                new Pozione(NOME_ELISIR_NEGOZIO, CURA_ELISIR_NEGOZIO, VALORE_ELISIR_NEGOZIO));
        return negozio;
    }

    /**
     * Torna al menu principale.
     */
    @FXML
    public void handleIndietro(ActionEvent event) {
        MainGUI.cambiaScena("/menu.fxml");
    }
}