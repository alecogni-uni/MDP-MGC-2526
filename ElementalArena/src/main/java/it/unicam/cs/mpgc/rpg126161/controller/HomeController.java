package it.unicam.cs.mpgc.rpg126161.controller;

import it.unicam.cs.mpgc.rpg126161.MainGUI;
import it.unicam.cs.mpgc.rpg126161.Sessione;
import it.unicam.cs.mpgc.rpg126161.model.Eroe;
import it.unicam.cs.mpgc.rpg126161.model.Partita;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class HomeController {

    @FXML private Label lblDungeon;
    @FXML private Label lblNome;
    @FXML private Label lblHp;
    @FXML private Label lblMonete;
    @FXML private Label lblArma;

    @FXML
    public void initialize() {
        Partita p = Sessione.getPartitaCorrente();
        if (p == null) return;

        Eroe e = p.getEroe();
        lblDungeon.setText("📍 SCONTRO ATTUALE: " + (e.getProgressoDungeon() + 1));
        lblNome.setText("Eroe: " + e.getNome().toUpperCase() + " (Livello " + e.getLivello() + ")");
        lblHp.setText("HP: " + e.getPuntiVitaAttuali() + " / " + e.getPuntiVitaMax());
        lblMonete.setText("Monete: 💰 " + e.getMonete());

        String arma = (e.getArmaEquipaggiata() != null) ? e.getArmaEquipaggiata().getNome() : "Mani Nude";
        lblArma.setText("Arma: 🗡️ " + arma);
    }

    @FXML
    public void handleCombattimento(ActionEvent event) {
        MainGUI.cambiaScena("/scontro.fxml");
    }

    @FXML
    public void handleNegozio(ActionEvent event) {
        MainGUI.cambiaScena("/negozio.fxml");
    }

    @FXML
    public void handleInventario(ActionEvent event) {
        MainGUI.cambiaScena("/inventario.fxml");
    }

    @FXML
    public void handleSalva(ActionEvent event) {
        Sessione.getPartitaRepo().salvaPartita(Sessione.getPartitaCorrente());

        // Feedback visuale per l'utente
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Salvataggio");
        alert.setHeaderText(null);
        alert.setContentText("Partita salvata correttamente!");
        alert.showAndWait();
    }

    @FXML
    public void handleEsci(ActionEvent event) {
        Sessione.setPartitaCorrente(null);
        MainGUI.cambiaScena("/menu.fxml");
    }
}