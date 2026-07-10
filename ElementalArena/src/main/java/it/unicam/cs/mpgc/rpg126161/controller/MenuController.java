package it.unicam.cs.mpgc.rpg126161.controller;

import it.unicam.cs.mpgc.rpg126161.MainGUI;
import it.unicam.cs.mpgc.rpg126161.Sessione;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {

    @FXML
    private Button btnCaricaSalvataggio;

    @FXML
    public void initialize() {
        // Il pulsante "Carica" viene mostrato solo se esiste almeno un salvataggio.
        boolean ciSonoSalvataggi = Sessione.getPartitaRepo().hasSalvataggi();
        btnCaricaSalvataggio.setVisible(ciSonoSalvataggi);
        btnCaricaSalvataggio.setManaged(ciSonoSalvataggi); // Se non visibile non occupa spazio nel layout
    }

    @FXML
    public void handleNuovaPartita(ActionEvent event) {
        MainGUI.cambiaScena("/nuova-partita.fxml");
    }

    @FXML
    public void handleCaricaPartita(ActionEvent event) {
        MainGUI.cambiaScena("/salvataggi.fxml");
    }

    @FXML
    public void handleEsci(ActionEvent event) {
        Platform.exit();
    }
}