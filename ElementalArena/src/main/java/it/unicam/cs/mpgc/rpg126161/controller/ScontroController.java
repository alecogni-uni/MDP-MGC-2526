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

    @FXML private TextArea txtBattleLog;
    @FXML private Label lblNomeEroe, lblHpEroe, lblElementoEroe, lblForzaEroe, lblArmaEroe;
    @FXML private Label lblNomeMostro, lblHpMostro, lblElementoMostro, lblForzaMostro;
    @FXML private ProgressBar barraHpEroe, barraHpMostro;
    @FXML private Button btnAttacca, btnPozione, btnIndietro;
    @FXML private ComboBox<String> cmbArmi;

    private Eroe eroe;
    private Mostro mostro;
    private Scontro arena;

    /**
     * Inizializza la scena di combattimento.
     * Carica il nemico in base al progresso nel dungeon e ne scala la difficoltà.
     */
    @FXML
    public void initialize() {
        // Recupera l'eroe dalla sessione corrente
        this.eroe = Sessione.getPartitaCorrente().getEroe();

        // Carica la lista dei nemici disponibili dal file JSON
        List<Mostro> nemici = CatalogoMostri.caricaMostriDaJson();
        int livelloDungeon = eroe.getProgressoDungeon();

        // Controlla se ci sono ancora nemici da affrontare nel dungeon
        if (livelloDungeon < nemici.size()) {
            mostro = nemici.get(livelloDungeon);

            // Applica un moltiplicatore di difficoltà basato sul progresso dell'eroe
            mostro.scalaDifficolta(livelloDungeon / 3);

            // Inizializza l'oggetto Scontro che fungerà da arbitro
            arena = new Scontro(eroe, mostro);

            inizializzaStatistiche();
            txtBattleLog.setText("Un selvaggio " + mostro.getNome() + " appare!");
            aggiornaBarreVita();
        } else {
            // Se non ci sono più nemici, l'eroe ha completato il gioco/dungeon
            txtBattleLog.setText("★ CAMPIONE DELL'ARENA ★");
            disabilitaAzioni();
            chiudiBattaglia("Torna alla Home", "#28a745");
        }
    }

    /**
     * Associa i dati dei modelli agli elementi visivi (Label) e popola il menu delle armi.
     */
    private void inizializzaStatistiche() {
        // Imposta le etichette con i parametri base dell'eroe
        lblNomeEroe.setText(eroe.getNome().toUpperCase() + " (Lv." + eroe.getLivello() + ")");
        lblElementoEroe.setText("Elemento: " + eroe.getElemento());
        lblForzaEroe.setText("Forza Base: " + eroe.getForza());

        aggiornaTestoArmaEquipaggiata();

        // Imposta le etichette con i parametri del mostro generato
        lblNomeMostro.setText(mostro.getNome().toUpperCase());
        lblElementoMostro.setText("Elemento: " + mostro.getElemento());
        lblForzaMostro.setText("Danno Base: " + mostro.getDannoBase());

        // Pulisce e prepara il menu a tendina per il cambio rapido dell'arma
        cmbArmi.getItems().clear();
        cmbArmi.getItems().add("Mani Nude"); // Opzione predefinita senza armi

        // Scansiona l'inventario per trovare solo le armi e aggiungerle alla tendina
        for (Oggetto o : eroe.getInventario().getOggetti()) {
            if (o instanceof Arma) {
                Arma a = (Arma) o;
                cmbArmi.getItems().add(formaStringaArma(a));
            }
        }

        // Imposta il valore iniziale della tendina sull'arma attualmente in uso (o Mani Nude)
        if (eroe.getArmaEquipaggiata() != null) {
            cmbArmi.setValue(formaStringaArma(eroe.getArmaEquipaggiata()));
        } else {
            cmbArmi.setValue("Mani Nude");
        }
    }

    /**
     * Formatta i dettagli dell'arma per la visualizzazione nella ComboBox e nella Label.
     */
    private String formaStringaArma(Arma a) {
        return a.getNome() + " [" + a.getElemento() + " | ATK: " + a.getDannoBase() + "]";
    }

    private void aggiornaTestoArmaEquipaggiata() {
        // Aggiorna l'etichetta dell'arma a schermo controllando se è presente o nulla
        if (eroe.getArmaEquipaggiata() != null) {
            lblArmaEroe.setText("Arma: " + formaStringaArma(eroe.getArmaEquipaggiata()));
        } else {
            lblArmaEroe.setText("Arma: Mani Nude");
        }
    }

    private void aggiornaBarreVita() {
        // Aggiorna testo e percentuale di riempimento della barra dell'eroe
        lblHpEroe.setText("HP: " + eroe.getPuntiVitaAttuali() + " / " + eroe.getPuntiVitaMax());
        barraHpEroe.setProgress((double) eroe.getPuntiVitaAttuali() / eroe.getPuntiVitaMax());

        // Aggiorna testo e percentuale di riempimento della barra del mostro
        lblHpMostro.setText("HP: " + mostro.getPuntiVitaAttuali() + " / " + mostro.getPuntiVitaMax());
        barraHpMostro.setProgress((double) mostro.getPuntiVitaAttuali() / mostro.getPuntiVitaMax());
    }

    private void scriviLog(String messaggio) {
        // Aggiunge una nuova riga al log testuale dell'arena e scorre verso il basso
        txtBattleLog.appendText("\n" + messaggio);
        txtBattleLog.setScrollTop(Double.MAX_VALUE);
    }

    /**
     * Gestisce il flusso dell'attacco: l'eroe colpisce, controlla la vittoria, poi il nemico risponde.
     */
    @FXML
    public void handleAttacca(ActionEvent event) {
        // Stampa la dichiarazione d'attacco dell'eroe e poi l'effettivo calcolo dei danni fatto dall'arbitro
        scriviLog(eroe.attacca(mostro));
        scriviLog(arena.eseguiAttacco(eroe, mostro));

        // Se l'attacco è stato letale, innesca la procedura di vittoria e interrompe il turno
        if (!mostro.isVivo()) {
            gestisciVittoria();
            return;
        }

        // Il nemico è sopravvissuto, quindi esegue la sua mossa (cura o attacco)
        scriviLog(mostro.eseguiTurno(eroe, arena));

        // Verifica se l'eroe è morto dopo il contrattacco
        if (!eroe.isVivo()) gestisciSconfitta();

        // Aggiorna la vista per riflettere i nuovi HP
        aggiornaBarreVita();
    }

    /**
     * Cerca la prima pozione nell'inventario, la consuma e passa il turno al nemico.
     */
    @FXML
    public void handlePozione(ActionEvent event) {
        int indicePozione = -1;

        // Scorre l'inventario per trovare il primo oggetto di tipo Pozione
        for (int i = 0; i < eroe.getInventario().getDimensione(); i++) {
            if (eroe.getInventario().getOggetto(i) instanceof Pozione) {
                indicePozione = i;
                break; // Ferma la ricerca appena ne trova una
            }
        }

        // Se una pozione è stata trovata, viene consumata
        if (indicePozione != -1) {
            eroe.usaOggettoDallInventario(indicePozione);
            scriviLog("♥ Vita ripristinata!");
            // Consumare un oggetto costa un turno, il mostro agisce
            scriviLog(mostro.eseguiTurno(eroe, arena));
        } else {
            // L'eroe non ha pozioni, perde il turno
            scriviLog("⚠ Nessuna pozione!");
            scriviLog(mostro.eseguiTurno(eroe, arena));
        }

        if (!eroe.isVivo()) gestisciSconfitta();
        aggiornaBarreVita();
    }

    /**
     * Modifica l'arma equipaggiata in base alla selezione della ComboBox.
     */
    @FXML
    public void handleCambiaArma(ActionEvent event) {
        String nomeArmaSelezionata = cmbArmi.getValue();
        if (nomeArmaSelezionata == null) return;

        // Gestione speciale se l'utente decide di togliere l'arma
        if (nomeArmaSelezionata.equals("Mani Nude")) {
            eroe.equipaggiaArma(null);
        } else {
            // Cerca nell'inventario l'arma corrispondente alla stringa selezionata
            for (Oggetto o : eroe.getInventario().getOggetti()) {
                if (o instanceof Arma) {
                    Arma a = (Arma) o;
                    if (formaStringaArma(a).equals(nomeArmaSelezionata)) {
                        eroe.equipaggiaArma(a); // Applica il cambio
                        break;
                    }
                }
            }
        }

        // Salva immediatamente il nuovo equipaggiamento sul database
        Sessione.getPartitaRepo().salvaPartita(Sessione.getPartitaCorrente());
        aggiornaTestoArmaEquipaggiata();
        scriviLog("Hai equipaggiato: " + (eroe.getArmaEquipaggiata() != null ? eroe.getArmaEquipaggiata().getNome() : "Mani Nude"));
    }

    private void gestisciVittoria() {
        scriviLog("VITTORIA! +XP e +Monete!");

        // Assegna ricompense ed aggiorna il contatore dei nemici sconfitti
        eroe.guadagnaEsperienza(mostro.getExpFornita());
        eroe.aggiungiMonete(mostro.getMoneteRilasciate());
        eroe.avanzaNelDungeon();

        // Rende permanenti i progressi sul DB
        Sessione.getPartitaRepo().salvaPartita(Sessione.getPartitaCorrente());
        chiudiBattaglia("Torna alla Home", "#28a745");
    }

    private void gestisciSconfitta() {
        scriviLog("SEI MORTO... GAME OVER.");

        // Meccanica di permadeath: elimina fisicamente il record della partita dal database
        Sessione.getPartitaRepo().eliminaPartita(Sessione.getPartitaCorrente());
        Sessione.setPartitaCorrente(null); // Pulisce la sessione locale

        chiudiBattaglia("GAME OVER - Torna al Menu", "#d9534f");
    }

    private void chiudiBattaglia(String testoBottone, String colore) {
        aggiornaBarreVita();
        disabilitaAzioni();

        // Rende visibile e configurato il bottone di uscita dall'arena
        btnIndietro.setVisible(true);
        btnIndietro.setManaged(true);
        btnIndietro.setText(testoBottone);
        btnIndietro.setStyle("-fx-background-color: " + colore + "; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    private void disabilitaAzioni() {
        // Blocca i comandi per impedire azioni dopo la fine dello scontro
        btnAttacca.setDisable(true);
        btnPozione.setDisable(true);
        cmbArmi.setDisable(true);
    }

    @FXML
    public void handleIndietro(ActionEvent event) {
        // Redirige al menu principale se in Game Over, altrimenti alla base/Home
        if (Sessione.getPartitaCorrente() == null) {
            MainGUI.cambiaScena("/menu.fxml");
        } else {
            MainGUI.cambiaScena("/home.fxml");
        }
    }
}