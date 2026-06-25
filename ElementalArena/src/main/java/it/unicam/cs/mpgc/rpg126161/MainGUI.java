package it.unicam.cs.mpgc.rpg126161;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainGUI extends Application {

    private static Stage stagePrincipale;

    @Override
    public void start(Stage finestraPrincipale) throws Exception {
        stagePrincipale = finestraPrincipale;

        // Carichiamo il menu iniziale
        cambiaScena("/menu.fxml");

        finestraPrincipale.setTitle("Elemental Arena");
        finestraPrincipale.show();
    }

    /**
     * Sostituisce la schermata attuale della finestra con quella passata come argomento.
     */
    public static void cambiaScena(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(MainGUI.class.getResource(fxmlPath));
            Scene nuovaScena = new Scene(root, 700, 500); // Leggermente più grande per la grafica
            stagePrincipale.setScene(nuovaScena);
        } catch (Exception e) {
            System.err.println("❌ Impossibile caricare la scena: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}