package it.unicam.cs.mpgc.rpg126161.controller;

import it.unicam.cs.mpgc.rpg126161.MainGUI;
import it.unicam.cs.mpgc.rpg126161.Sessione;
import it.unicam.cs.mpgc.rpg126161.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.util.List;

/**
 * Controller per la gestione dinamica dell'inventario.
 * Visualizza gli oggetti posseduti e gestisce l'uso di pozioni o equipaggiamento armi.
 */
public class InventarioController {

    @FXML private FlowPane grigliaOggetti;
    @FXML private Label lblFeedback;

    private Eroe eroe;

    @FXML
    public void initialize() {
        this.eroe = Sessione.getPartitaCorrente().getEroe();
        aggiornaGriglia();
    }

    private void aggiornaGriglia() {
        grigliaOggetti.getChildren().clear();
        List<Oggetto> oggetti = eroe.getInventario().getOggetti();

        if (oggetti.isEmpty()) {
            grigliaOggetti.getChildren().add(new Label("Lo zaino è vuoto."));
            return;
        }

        // Genero una card per ogni oggetto nell'inventario
        for (int i = 0; i < oggetti.size(); i++) {
            grigliaOggetti.getChildren().add(creaCardOggetto(oggetti.get(i), i));
        }
    }

    private VBox creaCardOggetto(Oggetto o, int indice) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(140, 140);
        card.setStyle("-fx-background-color: #3c3f41; -fx-background-radius: 10; -fx-padding: 10;");

        // Icona differenziata per tipo oggetto
        Label icona = new Label(o instanceof Arma ? "⚔" : "♥");
        icona.setStyle("-fx-font-size: 36px; -fx-text-fill: " + (o instanceof Arma ? "#ffaa00" : "#ff4d4d"));

        Label nome = new Label(o.getNome());
        nome.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        // Verifica stato equipaggiamento
        boolean isEquipaggiato = (o instanceof Arma) &&
                (eroe.getArmaEquipaggiata() != null) &&
                (o.getNome().equals(eroe.getArmaEquipaggiata().getNome()));

        Button btnAzione = new Button(o instanceof Arma ? (isEquipaggiato ? "Equipaggiato" : "Equipaggia") : "Usa");

        // Gestione interazione bottone
        if (isEquipaggiato) {
            btnAzione.setStyle("-fx-background-color: #555; -fx-text-fill: #aaa;");
            btnAzione.setDisable(true);
        } else {
            btnAzione.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-cursor: hand;");
            btnAzione.setOnAction(e -> {
                eroe.usaOggettoDallInventario(indice);
                lblFeedback.setText("Utilizzato: " + o.getNome());
                aggiornaGriglia(); // Refresh per aggiornare lo stato del tasto
            });
        }

        card.getChildren().addAll(icona, nome, btnAzione);
        return card;
    }

    @FXML
    public void handleIndietro(ActionEvent event) {
        MainGUI.cambiaScena("/home.fxml");
    }
}