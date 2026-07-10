package it.unicam.cs.mpgc.rpg126161.controller;

import it.unicam.cs.mpgc.rpg126161.MainGUI;
import it.unicam.cs.mpgc.rpg126161.Sessione;
import it.unicam.cs.mpgc.rpg126161.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Controller dell'Emporio: costruisce le card degli articoli in vendita
 * e gestisce le transazioni economiche del giocatore.
 */
public class NegozioController {

    @FXML private Label lblTitoloNegozio;
    @FXML private Label lblMonete;
    @FXML private Label lblFeedback;
    @FXML private FlowPane grigliaArticoli;

    private Eroe eroe;
    private Negozio negozio;

    @FXML
    public void initialize() {
        if (Sessione.getPartitaCorrente() == null) return;

        this.eroe = Sessione.getPartitaCorrente().getEroe();
        this.negozio = Sessione.getPartitaCorrente().getNegozio();

        lblTitoloNegozio.setText("≡ " + negozio.getNomeShop().toUpperCase());
        aggiornaGriglia();
    }

    private void aggiornaGriglia() {
        lblMonete.setText("Le tue monete: 💰 " + eroe.getMonete());
        grigliaArticoli.getChildren().clear();

        List<Oggetto> articoli = negozio.getArticoliInVendita();

        if (articoli.isEmpty()) {
            grigliaArticoli.getChildren().add(new Label("L'emporio ha esaurito le scorte!"));
            return;
        }

        // Si itera sugli indici perché l'indice identifica l'articolo nella transazione d'acquisto.
        IntStream.range(0, articoli.size())
                .mapToObj(i -> creaCardArticolo(articoli.get(i), i))
                .forEach(card -> grigliaArticoli.getChildren().add(card));
    }

    private VBox creaCardArticolo(Oggetto o, int indice) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(160, 200);
        card.getStyleClass().add("shop-card");

        // Icona e descrizione sono fornite polimorficamente dall'oggetto.
        ImageView iconaOggetto = new ImageView();
        iconaOggetto.setFitHeight(40);
        iconaOggetto.setFitWidth(40);
        iconaOggetto.setImage(new Image(getClass().getResourceAsStream(o.getIconPath())));

        Label nome = new Label(o.getNome());
        nome.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        nome.setWrapText(true);
        nome.setTextAlignment(TextAlignment.CENTER);
        nome.setAlignment(Pos.CENTER);

        // Colore delle stat in base al tipo
        Label stats = new Label(o.getDescrizione());
        stats.setStyle(o.isEquipaggiabile()
                ? "-fx-text-fill: #b3d9ff; -fx-font-size: 11px;"
                : "-fx-text-fill: #ffb3b3; -fx-font-size: 11px;");

        Label prezzo = new Label("💰 " + o.getValore());
        prezzo.setStyle("-fx-text-fill: #ffd700; -fx-font-weight: bold; -fx-padding: 5 0 0 0;");

        Button btnCompra = new Button("Compra");
        btnCompra.getStyleClass().add("menu-button");
        btnCompra.setStyle("-fx-font-size: 12px; -fx-padding: 5 10; -fx-min-width: 100px;");
        btnCompra.setOnAction(e -> acquista(o, indice));

        card.getChildren().addAll(iconaOggetto, nome, stats, prezzo, btnCompra);
        return card;
    }

    /**
     * Verifica la disponibilità di monete, delega la transazione al negozio,
     * persiste la partita e aggiorna il feedback a schermo.
     */
    private void acquista(Oggetto articolo, int indice) {
        if (eroe.getMonete() < articolo.getValore()) {
            lblFeedback.setText("❌ Monete insufficienti!");
            lblFeedback.setStyle("-fx-text-fill: #ff4d4d;");
            return;
        }

        negozio.compraArticolo(eroe, indice + 1);
        Sessione.getPartitaRepo().salvaPartita(Sessione.getPartitaCorrente());

        lblFeedback.setText("✅ Acquistato: " + articolo.getNome());
        lblFeedback.setStyle("-fx-text-fill: #4CAF50;");
        aggiornaGriglia();
    }

    @FXML
    public void handleIndietro(ActionEvent event) {
        MainGUI.cambiaScena("/home.fxml");
    }
}