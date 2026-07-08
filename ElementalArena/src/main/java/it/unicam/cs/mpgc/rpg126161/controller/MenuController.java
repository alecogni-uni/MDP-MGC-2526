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

        boolean ciSonoSalvataggi = Sessione.getPartitaRepo().hasSalvataggi();

        if (!ciSonoSalvataggi) {
            btnCaricaSalvataggio.setVisible(false);
            btnCaricaSalvataggio.setManaged(false);
        }
    }

    @FXML
    public void handleNuovaPartita(ActionEvent event) {
        // Naviga verso la schermata di selezione/creazione
        MainGUI.cambiaScena("/nuova-partita.fxml");
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