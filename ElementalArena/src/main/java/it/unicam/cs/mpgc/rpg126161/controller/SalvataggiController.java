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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Controller per il caricamento e la rimozione dei salvataggi esistenti.
 */
public class SalvataggiController {

    @FXML private FlowPane grigliaPartite;

    /**
     * Ricostruisce la griglia dei salvataggi leggendo le partite dal repository.
     * Viene richiamato anche dopo un'eliminazione per aggiornare la vista.
     */
    @FXML
    public void initialize() {
        grigliaPartite.getChildren().clear();

        List<Partita> partite = Sessione.getPartitaRepo().getTutteLePartite();

        if (partite.isEmpty()) {
            Label lblVuoto = new Label("Nessun salvataggio trovato. Torna al menu e crea una Nuova Partita!");
            lblVuoto.setStyle("-fx-text-fill: #aaaaaa; -fx-font-style: italic;");
            grigliaPartite.getChildren().add(lblVuoto);
            return;
        }

        partite.stream()
                .map(this::creaCardPartita)
                .forEach(grigliaPartite.getChildren()::add);
    }

    /**
     * Costruisce la card di un salvataggio con riepilogo dell'eroe e i bottoni Carica ed Elimina.
     * @param p la partita da rappresentare
     * @return il nodo VBox pronto per la griglia
     */
    private VBox creaCardPartita(Partita p) {
        Eroe e = p.getEroe();
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(160, 130);
        card.setStyle("-fx-background-color: #3c3f41; -fx-background-radius: 10; -fx-padding: 10;");

        Label nome = new Label(e.getNome().toUpperCase());
        nome.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Label statistiche = new Label("Liv: " + e.getLivello() + " | Scontro: " + (e.getProgressoDungeon() + 1));
        statistiche.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 11px;");

        HBox contenitoreBottoni = new HBox(10);
        contenitoreBottoni.setAlignment(Pos.CENTER);

        Button btnCarica = new Button("Carica");
        btnCarica.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;");
        btnCarica.setOnAction(event -> {
            Sessione.setPartitaCorrente(p);
            MainGUI.cambiaScena("/home.fxml");
        });

        Button btnElimina = new Button("🗑️");
        btnElimina.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand;");
        btnElimina.setOnAction(event -> {
            Sessione.getPartitaRepo().eliminaPartita(p);
            initialize(); // ricostruisce la griglia dopo l'eliminazione
        });

        contenitoreBottoni.getChildren().addAll(btnCarica, btnElimina);
        card.getChildren().addAll(new Label("👤"), nome, statistiche, contenitoreBottoni);

        return card;
    }

    /**
     * Torna al menu principale.
     */
    @FXML
    public void handleIndietro(ActionEvent event) {
        MainGUI.cambiaScena("/menu.fxml");
    }
}