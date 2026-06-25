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
import javafx.scene.text.TextAlignment;
import java.util.List;

/**
 * Controller per la gestione dell'Emporio (Negozio).
 * Gestisce la creazione dinamica delle card per gli articoli in vendita
 * e le transazioni economiche del giocatore.
 */
public class NegozioController {

    @FXML private Label lblTitoloNegozio;
    @FXML private Label lblMonete;
    @FXML private Label lblFeedback;
    @FXML private FlowPane grigliaArticoli;

    private Eroe eroe;
    private Negozio negozio;

    /**
     * Inizializza l'interfaccia caricando i dati della sessione corrente.
     */
    @FXML
    public void initialize() {
        if (Sessione.getPartitaCorrente() == null) return;

        this.eroe = Sessione.getPartitaCorrente().getEroe();
        this.negozio = Sessione.getPartitaCorrente().getNegozio();

        lblTitoloNegozio.setText("≡ " + negozio.getNomeShop().toUpperCase());
        aggiornaGriglia();
    }

    /**
     * Svuota e ripopola la griglia visiva in base agli articoli attualmente in vendita.
     */
    private void aggiornaGriglia() {
        lblMonete.setText("Le tue monete: 💰 " + eroe.getMonete());
        grigliaArticoli.getChildren().clear();

        List<Oggetto> articoli = negozio.getArticoliInVendita();

        if (articoli.isEmpty()) {
            grigliaArticoli.getChildren().add(new Label("L'emporio ha esaurito le scorte!"));
            return;
        }

        for (int i = 0; i < articoli.size(); i++) {
            grigliaArticoli.getChildren().add(creaCardArticolo(articoli.get(i), i));
        }
    }

    /**
     * Genera dinamicamente un VBox contenente le informazioni e il bottone di acquisto per un singolo oggetto.
     */
    private VBox creaCardArticolo(Oggetto o, int indice) {
        VBox card = new VBox(5); // Spaziatura ridotta tra gli elementi
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(160, 180); // Altezza aumentata per far spazio alle statistiche
        card.setStyle("-fx-background-color: #3c3f41; -fx-background-radius: 10; -fx-padding: 10;");

        Label icona = new Label(o instanceof Arma ? "⚔" : "♥");
        icona.setStyle("-fx-font-size: 30px; -fx-text-fill: " + (o instanceof Arma ? "#ffaa00" : "#ff4d4d"));

        Label nome = new Label(o.getNome());
        nome.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        nome.setWrapText(true);
        nome.setTextAlignment(TextAlignment.CENTER);
        nome.setAlignment(Pos.CENTER);

        // Aggiunta di una Label dedicata per le statistiche (garantisce visibilità)
        Label stats = new Label();
        if (o instanceof Arma) {
            Arma a = (Arma) o;
            stats.setText(a.getElemento() + " | ATK: " + a.getDannoBase());
            stats.setStyle("-fx-text-fill: #b3d9ff; -fx-font-size: 11px;");
        } else {
            stats.setText("Consumabile");
            stats.setStyle("-fx-text-fill: #ffb3b3; -fx-font-size: 11px;");
        }

        Label prezzo = new Label("💰 " + o.getValore());
        prezzo.setStyle("-fx-text-fill: #ffeb3b; -fx-font-weight: bold; -fx-padding: 5 0 0 0;");

        Button btnCompra = new Button("Compra");
        btnCompra.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-cursor: hand;");

        btnCompra.setOnAction(e -> {
            if (eroe.getMonete() >= o.getValore()) {
                negozio.compraArticolo(eroe, indice + 1);
                Sessione.getPartitaRepo().salvaPartita(Sessione.getPartitaCorrente());

                lblFeedback.setText("✅ Acquistato: " + o.getNome());
                lblFeedback.setStyle("-fx-text-fill: #4CAF50;");
            } else {
                lblFeedback.setText("❌ Monete insufficienti!");
                lblFeedback.setStyle("-fx-text-fill: #ff4d4d;");
            }
            aggiornaGriglia();
        });

        // Inserita la Label stats subito sotto il nome
        card.getChildren().addAll(icona, nome, stats, prezzo, btnCompra);
        return card;
    }

    @FXML
    public void handleIndietro(ActionEvent event) {
        MainGUI.cambiaScena("/home.fxml");
    }
}