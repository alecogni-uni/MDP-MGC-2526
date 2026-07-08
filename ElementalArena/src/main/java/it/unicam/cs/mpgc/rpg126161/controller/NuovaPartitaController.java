package it.unicam.cs.mpgc.rpg126161.controller;

import it.unicam.cs.mpgc.rpg126161.MainGUI;
import it.unicam.cs.mpgc.rpg126161.Sessione;
import it.unicam.cs.mpgc.rpg126161.model.*;
import it.unicam.cs.mpgc.rpg126161.utils.CaricatoreArmi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NuovaPartitaController {

    @FXML private TextField campoNomeNuovoEroe;

    @FXML
    public void handleNuovoEroe(ActionEvent event) {
        String nome = campoNomeNuovoEroe.getText().trim();
        if (nome.isEmpty()) return;

        Eroe eroe = new Eroe(nome, Elemento.LUCE);
        eroe.aggiungiMonete(30);
        eroe.getInventario().aggiungi(new Pozione("Pozione Piccola", 25, 10));

        Negozio negozio = new Negozio("Emporio di " + nome);
        negozio.popolaNegozio(CaricatoreArmi.caricaArmiDaJson());
        negozio.aggiungiArticolo(new Pozione("Elisir Totale", 100, 60));

        Partita nuovaPartita = new Partita(eroe, negozio);
        Sessione.getPartitaRepo().salvaPartita(nuovaPartita);

        Sessione.setPartitaCorrente(nuovaPartita);
        MainGUI.cambiaScena("/home.fxml");
    }

    @FXML
    public void handleIndietro(ActionEvent event) {
        MainGUI.cambiaScena("/menu.fxml");
    }
}