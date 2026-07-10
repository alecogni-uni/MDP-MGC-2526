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

        // Genero una card per ogni oggetto nell'inventario
        for (int i = 0; i < oggetti.size(); i++) {
            grigliaOggetti.getChildren().add(creaCardOggetto(oggetti.get(i), i));
        }
    }

    private VBox creaCardOggetto(Oggetto o, int indice) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(160, 200); // Stesse dimensioni del Negozio
        card.getStyleClass().add("shop-card"); // Ricicliamo la classe CSS fighissima!

        // 1. Icona Pixel Art
        ImageView iconaOggetto = new ImageView();
        iconaOggetto.setFitHeight(40);
        iconaOggetto.setFitWidth(40);

        String imagePath = (o instanceof Arma) ? "/images/sword.png" : "/images/potion.png";
        iconaOggetto.setImage(new Image(getClass().getResourceAsStream(imagePath)));

        // 2. Nome
        Label nome = new Label(o.getNome());
        nome.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        nome.setWrapText(true);
        nome.setTextAlignment(TextAlignment.CENTER);
        nome.setAlignment(Pos.CENTER);

        // 3. Statistiche dell'oggetto
        Label stats = new Label();
        if (o instanceof Arma) {
            Arma a = (Arma) o;
            stats.setText(a.getElemento() + " | ATK: " + a.getDannoBase());
            stats.setStyle("-fx-text-fill: #b3d9ff; -fx-font-size: 11px;");
        } else {
            stats.setText("Consumabile");
            stats.setStyle("-fx-text-fill: #ffb3b3; -fx-font-size: 11px;");
        }

        // Verifica stato equipaggiamento
        boolean isEquipaggiato = (o instanceof Arma) &&
                (eroe.getArmaEquipaggiata() != null) &&
                (o.getNome().equals(eroe.getArmaEquipaggiata().getNome()));

        // 4. Bottone di Azione
        Button btnAzione = new Button(o instanceof Arma ? (isEquipaggiato ? "Equipaggiato" : "Equipaggia") : "Usa");
        btnAzione.getStyleClass().add("menu-button");

        // Gestione stili interattivi
        if (isEquipaggiato) {
            // Disabilitato e grigio se l'arma è già impugnata
            btnAzione.setStyle("-fx-font-size: 12px; -fx-padding: 5 10; -fx-min-width: 100px; -fx-background-color: #444444; -fx-text-fill: #888888; -fx-border-color: #666666;");
            btnAzione.setDisable(true);
        } else {
            // Colore dinamico: Blu per le armi, Verde per le pozioni
            if (o instanceof Arma) {
                btnAzione.setStyle("-fx-font-size: 12px; -fx-padding: 5 10; -fx-min-width: 100px; -fx-background-color: #004085; -fx-border-color: #82b1ff;");
            } else {
                btnAzione.setStyle("-fx-font-size: 12px; -fx-padding: 5 10; -fx-min-width: 100px; -fx-background-color: #143d1f; -fx-border-color: #28a745;");
            }

            btnAzione.setOnAction(e -> {
                eroe.usaOggettoDallInventario(indice);
                lblFeedback.setText("Utilizzato/Equipaggiato: " + o.getNome());
                lblFeedback.setStyle("-fx-text-fill: #ffd700; -fx-font-weight: bold;"); // Feedback dorato
                aggiornaGriglia(); // Refresh della pagina per aggiornare lo stato del tasto
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