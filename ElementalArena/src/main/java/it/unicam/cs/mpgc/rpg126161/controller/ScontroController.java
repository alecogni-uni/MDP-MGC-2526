package it.unicam.cs.mpgc.rpg126161.controller;

import it.unicam.cs.mpgc.rpg126161.MainGUI;
import it.unicam.cs.mpgc.rpg126161.Sessione;
import it.unicam.cs.mpgc.rpg126161.model.*;
import it.unicam.cs.mpgc.rpg126161.utils.CatalogoMostri;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

import java.util.List;

/**
 * Controller per la gestione dell'interfaccia di scontro (GUI).
 * Delega la logica di business ai modelli (Scontro, Mostro, Eroe)
 * e si occupa esclusivamente dell'aggiornamento della vista.
 */
public class ScontroController {

    @FXML
    private TextArea txtBattleLog;
    @FXML
    private Label lblNomeEroe, lblHpEroe, lblElementoEroe, lblForzaEroe, lblArmaEroe;
    @FXML
    private Label lblNomeMostro, lblHpMostro, lblElementoMostro, lblForzaMostro;
    @FXML
    private ProgressBar barraHpEroe, barraHpMostro;
    @FXML
    private Button btnAttacca, btnPozione, btnIndietro;
    @FXML
    private ComboBox<String> cmbArmi;
    @FXML
    private ComboBox<String> cmbPozioni; // Aggiunta tendina per le pozioni

    private Eroe eroe;
    private Mostro mostro;
    private Scontro arena;

    @FXML
    public void initialize() {
        this.eroe = Sessione.getPartitaCorrente().getEroe();
        List<Mostro> nemici = CatalogoMostri.caricaMostriDaJson();
        int livelloDungeon = eroe.getProgressoDungeon();

        if (livelloDungeon < nemici.size()) {
            mostro = nemici.get(livelloDungeon);
            mostro.scalaDifficolta(livelloDungeon / 3);
            arena = new Scontro(eroe, mostro);

            inizializzaStatistiche();
            txtBattleLog.setText("Un selvaggio " + mostro.getNome() + " appare!");
            aggiornaBarreVita();
        } else {
            txtBattleLog.setText("★ CAMPIONE DELL'ARENA ★");
            disabilitaAzioni();
            chiudiBattaglia("Torna alla Home", "#28a745");
        }
    }

    private void inizializzaStatistiche() {
        lblNomeEroe.setText(eroe.getNome().toUpperCase() + " (Lv." + eroe.getLivello() + ")");
        lblElementoEroe.setText("Elemento: " + eroe.getElemento());
        lblForzaEroe.setText("Forza Base: " + eroe.getForza());
        aggiornaTestoArmaEquipaggiata();

        lblNomeMostro.setText(mostro.getNome().toUpperCase());
        lblElementoMostro.setText("Elemento: " + mostro.getElemento());
        lblForzaMostro.setText("Danno Base: " + mostro.getDannoBase());

        cmbArmi.getItems().clear();
        cmbArmi.getItems().add("Mani Nude");

        for (Oggetto o : eroe.getInventario().getOggetti()) {
            if (o instanceof Arma) {
                Arma a = (Arma) o;
                cmbArmi.getItems().add(formaStringaArma(a));
            }
        }

        if (eroe.getArmaEquipaggiata() != null) {
            cmbArmi.setValue(formaStringaArma(eroe.getArmaEquipaggiata()));
        } else {
            cmbArmi.setValue("Mani Nude");
        }

        // Popola il menu delle pozioni e aggiorna lo stato dei bottoni
        aggiornaMenuPozioni();
    }

    /**
     * Scansiona l'inventario, cerca le pozioni, popola la tendina
     * e disabilita il bottone se lo zaino è vuoto.
     */
    private void aggiornaMenuPozioni() {
        cmbPozioni.getItems().clear();
        boolean haPozioni = false;

        for (Oggetto o : eroe.getInventario().getOggetti()) {
            if (o instanceof Pozione) {
                cmbPozioni.getItems().add(o.getNome());
                haPozioni = true;
            }
        }

        if (haPozioni) {
            cmbPozioni.getSelectionModel().selectFirst();
            btnPozione.setDisable(false);
            cmbPozioni.setDisable(false);
        } else {
            cmbPozioni.getItems().add("Nessuna Pozione");
            cmbPozioni.getSelectionModel().selectFirst();
            btnPozione.setDisable(true);
            cmbPozioni.setDisable(true);
        }
    }

    private String formaStringaArma(Arma a) {
        return a.getNome() + " [" + a.getElemento() + " | ATK: " + a.getDannoBase() + "]";
    }

    private void aggiornaTestoArmaEquipaggiata() {
        if (eroe.getArmaEquipaggiata() != null) {
            lblArmaEroe.setText("Arma: " + formaStringaArma(eroe.getArmaEquipaggiata()));
        } else {
            lblArmaEroe.setText("Arma: Mani Nude");
        }
    }

    private void aggiornaBarreVita() {
        lblHpEroe.setText("HP: " + eroe.getPuntiVitaAttuali() + " / " + eroe.getPuntiVitaMax());
        barraHpEroe.setProgress((double) eroe.getPuntiVitaAttuali() / eroe.getPuntiVitaMax());

        lblHpMostro.setText("HP: " + mostro.getPuntiVitaAttuali() + " / " + mostro.getPuntiVitaMax());
        barraHpMostro.setProgress((double) mostro.getPuntiVitaAttuali() / mostro.getPuntiVitaMax());
    }

    private void scriviLog(String messaggio) {
        txtBattleLog.appendText("\n" + messaggio);
        txtBattleLog.setScrollTop(Double.MAX_VALUE);
    }

    @FXML
    public void handleAttacca(ActionEvent event) {
        scriviLog(eroe.attacca(mostro));
        scriviLog(arena.eseguiAttacco(eroe, mostro));

        if (!mostro.isVivo()) {
            gestisciVittoria();
            return;
        }

        scriviLog(mostro.eseguiTurno(eroe, arena));

        if (!eroe.isVivo()) gestisciSconfitta();
        aggiornaBarreVita();
    }

    @FXML
    public void handlePozione(ActionEvent event) {
        String nomePozioneSelezionata = cmbPozioni.getValue();
        if (nomePozioneSelezionata == null || nomePozioneSelezionata.equals("Nessuna Pozione")) return;

        int indicePozione = -1;

        // Scorre l'inventario per trovare la pozione esatta
        for (int i = 0; i < eroe.getInventario().getDimensione(); i++) {
            Oggetto o = eroe.getInventario().getOggetto(i);
            if (o instanceof Pozione && o.getNome().equals(nomePozioneSelezionata)) {
                indicePozione = i;
                break;
            }
        }

        if (indicePozione != -1) {
            eroe.usaOggettoDallInventario(indicePozione);
            scriviLog("♥ Hai consumato: " + nomePozioneSelezionata + "!");

            // Aggiorniamo la tendina (una pozione è stata consumata)
            aggiornaMenuPozioni();

            // Il mostro contrattacca perché curarsi consuma un turno
            scriviLog(mostro.eseguiTurno(eroe, arena));
        }

        if (!eroe.isVivo()) gestisciSconfitta();
        aggiornaBarreVita();
    }

    @FXML
    public void handleCambiaArma(ActionEvent event) {
        String nomeArmaSelezionata = cmbArmi.getValue();
        if (nomeArmaSelezionata == null) return;

        if (nomeArmaSelezionata.equals("Mani Nude")) {
            eroe.equipaggiaArma(null);
        } else {
            for (Oggetto o : eroe.getInventario().getOggetti()) {
                if (o instanceof Arma) {
                    Arma a = (Arma) o;
                    if (formaStringaArma(a).equals(nomeArmaSelezionata)) {
                        eroe.equipaggiaArma(a);
                        break;
                    }
                }
            }
        }

        Sessione.getPartitaRepo().salvaPartita(Sessione.getPartitaCorrente());
        aggiornaTestoArmaEquipaggiata();
        scriviLog("Hai equipaggiato: " + (eroe.getArmaEquipaggiata() != null ? eroe.getArmaEquipaggiata().getNome() : "Mani Nude"));
    }

    private void gestisciVittoria() {
        scriviLog("VITTORIA! +XP e +Monete!");

        eroe.guadagnaEsperienza(mostro.getExpFornita());
        eroe.aggiungiMonete(mostro.getMoneteRilasciate());
        eroe.avanzaNelDungeon();

        Sessione.getPartitaRepo().salvaPartita(Sessione.getPartitaCorrente());
        chiudiBattaglia("Torna alla Home", "#28a745");
    }

    private void gestisciSconfitta() {
        scriviLog("SEI MORTO... GAME OVER.");

        Sessione.getPartitaRepo().eliminaPartita(Sessione.getPartitaCorrente());
        Sessione.setPartitaCorrente(null);

        chiudiBattaglia("GAME OVER - Torna al Menu", "#d9534f");
    }

    private void chiudiBattaglia(String testoBottone, String colore) {
        aggiornaBarreVita();
        disabilitaAzioni();

        btnIndietro.setVisible(true);
        btnIndietro.setManaged(true);
        btnIndietro.setText(testoBottone);
        btnIndietro.setStyle("-fx-background-color: " + colore + "; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    private void disabilitaAzioni() {
        btnAttacca.setDisable(true);
        btnPozione.setDisable(true);
        cmbArmi.setDisable(true);
        cmbPozioni.setDisable(true); // Blocco anche la tendina pozioni
    }

    @FXML
    public void handleIndietro(ActionEvent event) {
        if (Sessione.getPartitaCorrente() == null) {
            MainGUI.cambiaScena("/menu.fxml");
        } else {
            MainGUI.cambiaScena("/home.fxml");
        }
    }
}