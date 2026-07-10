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
            Label vuoto = new Label("Lo zaino è vuoto.");
            vuoto.setStyle("-fx-text-fill: #aaaaaa; -fx-font-style: italic;");
            grigliaOggetti.getChildren().add(vuoto);
            return;
        }

        // Si itera sugli indici della lista perché l'indice serve sia a costruire la card
        // sia a identificare l'oggetto in usaOggettoDallInventario. Per ogni indice si genera
        // la card corrispondente e la si aggiunge alla griglia.
        IntStream.range(0, oggetti.size())
                .mapToObj(i -> creaCardOggetto(oggetti.get(i), i))
                .forEach(card -> grigliaOggetti.getChildren().add(card));
    }

    private VBox creaCardOggetto(Oggetto o, int indice) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(160, 200); // Stesse dimensioni delle card del Negozio.
        card.getStyleClass().add("shop-card"); // Si riusa la classe CSS già definita per le card.

        //il percorso è fornito dall'oggetto.
        ImageView iconaOggetto = new ImageView();
        iconaOggetto.setFitHeight(40);
        iconaOggetto.setFitWidth(40);
        iconaOggetto.setImage(new Image(getClass().getResourceAsStream(o.getIconPath())));

        // Nome dell'oggetto.
        Label nome = new Label(o.getNome());
        nome.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        nome.setWrapText(true);
        nome.setTextAlignment(TextAlignment.CENTER);
        nome.setAlignment(Pos.CENTER);

        // Statistiche: descrizione fornita dall'oggetto, il colore dipende dal tipo.
        Label stats = new Label(o.getDescrizione());
        stats.setStyle(o.isEquipaggiabile()
                ? "-fx-text-fill: #b3d9ff; -fx-font-size: 11px;"
                : "-fx-text-fill: #ffb3b3; -fx-font-size: 11px;");

        // Stato equipaggiamento.
        boolean isEquipaggiato = o.isEquipaggiato(eroe);

        // Bottone di azione: etichetta di default dell'oggetto, o "Equipaggiato" se già impugnato.
        Button btnAzione = new Button(isEquipaggiato ? "Equipaggiato" : o.getEtichettaAzione());
        btnAzione.getStyleClass().add("menu-button");

        if (isEquipaggiato) {
            // Arma già impugnata: bottone disabilitato e in grigio.
            btnAzione.setStyle("-fx-font-size: 12px; -fx-padding: 5 10; -fx-min-width: 100px; -fx-background-color: #444444; -fx-text-fill: #888888; -fx-border-color: #666666;");
            btnAzione.setDisable(true);
        } else {
            // Colore del bottone in base al tipo: blu per gli equipaggiabili, verde per i consumabili.
            if (o.isEquipaggiabile()) {
                btnAzione.setStyle("-fx-font-size: 12px; -fx-padding: 5 10; -fx-min-width: 100px; -fx-background-color: #004085; -fx-border-color: #82b1ff;");
            } else {
                btnAzione.setStyle("-fx-font-size: 12px; -fx-padding: 5 10; -fx-min-width: 100px; -fx-background-color: #143d1f; -fx-border-color: #28a745;");
            }

            btnAzione.setOnAction(e -> {
                eroe.usaOggettoDallInventario(indice);
                lblFeedback.setText("Utilizzato/Equipaggiato: " + o.getNome());
                lblFeedback.setStyle("-fx-text-fill: #ffd700; -fx-font-weight: bold;");
                aggiornaGriglia();
            });
        }

        card.getChildren().addAll(iconaOggetto, nome, stats, btnAzione);
        return card;
    }

    @FXML
    public void handleIndietro(ActionEvent event) {
        MainGUI.cambiaScena("/home.fxml");
    }
}