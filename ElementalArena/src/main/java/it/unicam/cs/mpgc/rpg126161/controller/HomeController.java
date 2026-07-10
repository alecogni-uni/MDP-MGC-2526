package it.unicam.cs.mpgc.rpg126161.controller;

import it.unicam.cs.mpgc.rpg126161.MainGUI;
import it.unicam.cs.mpgc.rpg126161.Sessione;
import it.unicam.cs.mpgc.rpg126161.model.Eroe;
import it.unicam.cs.mpgc.rpg126161.model.Partita;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.util.Optional;

public class HomeController {

    private static final String NOME_ARMA_DEFAULT = "Mani Nude";

    @FXML private Label lblDungeon;
    @FXML private Label lblNome;
    @FXML private Label lblHp;
    @FXML private Label lblMonete;
    @FXML private Label lblArma;

    @FXML
    public void initialize() {
        // Se non c'è una partita in sessione usciamo subito.
        Partita p = Sessione.getPartitaCorrente();
        if (p == null) return;

        Eroe e = p.getEroe();
        lblDungeon.setText("📍 SCONTRO ATTUALE: " + (e.getProgressoDungeon() + 1));
        lblNome.setText("Eroe: " + e.getNome().toUpperCase() + " (Livello " + e.getLivello() + ")");
        lblHp.setText("HP: " + e.getPuntiVitaAttuali() + " / " + e.getPuntiVitaMax());
        lblMonete.setText("Monete: 💰 " + e.getMonete());

        // se l'arma è assente si usa il valore di default.
        String arma = Optional.ofNullable(e.getArmaEquipaggiata())
                .map(a -> a.getNome())
                .orElse(NOME_ARMA_DEFAULT);
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

        // Feedback visuale all'utente dopo il salvataggio.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Salvataggio");
        alert.setHeaderText(null);
        alert.setContentText("Partita salvata correttamente!");
        alert.showAndWait();
    }

    @FXML
    public void handleEsci(ActionEvent event) {
        // Si azzera la sessione corrente e si torna al menu principale.
        Sessione.setPartitaCorrente(null);
        MainGUI.cambiaScena("/menu.fxml");
    }
}