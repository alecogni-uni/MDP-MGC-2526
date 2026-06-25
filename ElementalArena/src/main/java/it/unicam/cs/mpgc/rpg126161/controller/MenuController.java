package it.unicam.cs.mpgc.rpg126161.controller;

import it.unicam.cs.mpgc.rpg126161.MainGUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuController {

    @FXML
    public void handleNuovaPartita(ActionEvent event) {
        // Naviga verso la schermata di selezione/creazione
        MainGUI.cambiaScena("/salvataggi.fxml");
    }

    @FXML
    public void handleCaricaPartita(ActionEvent event) {
        // Condividono la stessa schermata dove mostriamo la lista o creiamo il nuovo eroe
        MainGUI.cambiaScena("/salvataggi.fxml");
    }

    @FXML
    public void handleEsci(ActionEvent event) {
        Platform.exit();
    }
}