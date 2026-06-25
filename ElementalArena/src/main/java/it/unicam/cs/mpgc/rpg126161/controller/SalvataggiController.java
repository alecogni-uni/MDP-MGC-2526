package it.unicam.cs.mpgc.rpg126161.controller;

import it.unicam.cs.mpgc.rpg126161.MainGUI;
import it.unicam.cs.mpgc.rpg126161.Sessione;
import it.unicam.cs.mpgc.rpg126161.model.*;
import it.unicam.cs.mpgc.rpg126161.utils.CaricatoreArmi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.util.List;

/**
 * Controller per la gestione dei salvataggi (caricamento esistenti e creazione nuovi).
 */
public class SalvataggiController {

    @FXML private FlowPane grigliaPartite;
    @FXML private TextField campoNomeNuovoEroe;

    @FXML
    public void initialize() {
        // Recupera tutte le partite dal DB tramite il repository
        List<Partita> elencoPartiteDB = Sessione.getPartitaRepo().getTutteLePartite();

        if (elencoPartiteDB.isEmpty()) {
            Label lblVuoto = new Label("Nessun salvataggio trovato. Crea un nuovo eroe qui sotto!");
            lblVuoto.setStyle("-fx-text-fill: #aaaaaa; -fx-font-style: italic;");
            grigliaPartite.getChildren().add(lblVuoto);
        } else {
            // Genera una card visuale per ogni partita salvata
            for (Partita p : elencoPartiteDB) {
                grigliaPartite.getChildren().add(creaCardPartita(p));
            }
        }
    }

    private VBox creaCardPartita(Partita p) {
        Eroe e = p.getEroe();
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(150, 120);
        card.setStyle("-fx-background-color: #3c3f41; -fx-background-radius: 10; -fx-padding: 10;");

        Label nome = new Label(e.getNome().toUpperCase());
        nome.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Label statistiche = new Label("Liv: " + e.getLivello() + " | Scontro: " + (e.getProgressoDungeon() + 1));
        statistiche.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 11px;");

        Button btnCarica = new Button("Carica");
        // Imposta la partita selezionata come sessione attiva
        btnCarica.setOnAction(event -> {
            Sessione.setPartitaCorrente(p);
            MainGUI.cambiaScena("/home.fxml");
        });

        card.getChildren().addAll(new Label("👤"), nome, statistiche, btnCarica);
        return card;
    }

    @FXML
    public void handleNuovoEroe(ActionEvent event) {
        String nome = campoNomeNuovoEroe.getText().trim();
        if (nome.isEmpty()) return;

        // Inizializzazione nuovo Eroe
        Eroe eroe = new Eroe(nome, Elemento.LUCE);
        eroe.aggiungiMonete(30);
        eroe.getInventario().aggiungi(new Pozione("Pozione Piccola", 25, 10));

        // Popolamento dinamico del negozio tramite file JSON
        Negozio negozio = new Negozio("Emporio di " + nome);
        negozio.popolaNegozio(CaricatoreArmi.caricaArmiDaJson());
        negozio.aggiungiArticolo(new Pozione("Elisir Totale", 100, 60));

        // Persistenza: salvataggio nuova istanza nel database
        Partita nuovaPartita = new Partita(eroe, negozio);
        Sessione.getPartitaRepo().salvaPartita(nuovaPartita);

        // Imposta come sessione attiva e cambia scena
        Sessione.setPartitaCorrente(nuovaPartita);
        MainGUI.cambiaScena("/home.fxml");
    }

    @FXML
    public void handleIndietro(ActionEvent event) {
        MainGUI.cambiaScena("/menu.fxml");
    }
}