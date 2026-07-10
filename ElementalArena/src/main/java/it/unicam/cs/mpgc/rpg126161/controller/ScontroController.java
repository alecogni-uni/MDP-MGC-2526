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
import java.util.stream.IntStream;

/**
 * Controller dell'interfaccia di scontro.
 * Delega la logica di combattimento ai modelli (Scontro, Mostro, Eroe)
 * e si occupa esclusivamente dell'aggiornamento della vista.
 */
public class ScontroController {

    private static final String MANI_NUDE = "Mani Nude";
    private static final String NESSUN_CONSUMABILE = "Nessun Consumabile";
    private static final int MOSTRI_PER_LIVELLO_DIFFICOLTA = 3;

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
    private ComboBox<String> cmbPozioni;

    private Eroe eroe;
    private Mostro mostro;
    private Scontro arena;

    /**
     * Prepara lo scontro col nemico corrispondente al progresso nel dungeon.
     * Se il dungeon è stato completato, mostra lo stato di vittoria finale.
     */
    @FXML
    public void initialize() {
        this.eroe = Sessione.getPartitaCorrente().getEroe();
        List<Mostro> nemici = CatalogoMostri.caricaMostriDaJson();
        int livelloDungeon = eroe.getProgressoDungeon();

        if (livelloDungeon < nemici.size()) {
            mostro = nemici.get(livelloDungeon);
            // La difficoltà sale di un gradino ogni MOSTRI_PER_LIVELLO_DIFFICOLTA scontri superati.
            mostro.scalaDifficolta(livelloDungeon / MOSTRI_PER_LIVELLO_DIFFICOLTA);
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

    /**
     * Popola le etichette di eroe e mostro e inizializza le tendine di armi e consumabili.
     */
    private void inizializzaStatistiche() {
        lblNomeEroe.setText(eroe.getNome().toUpperCase() + " (Lv." + eroe.getLivello() + ")");
        lblElementoEroe.setText("Elemento: " + eroe.getElemento());
        lblForzaEroe.setText("Forza Base: " + eroe.getForza());
        aggiornaTestoArmaEquipaggiata();

        lblNomeMostro.setText(mostro.getNome().toUpperCase());
        lblElementoMostro.setText("Elemento: " + mostro.getElemento());
        lblForzaMostro.setText("Danno Base: " + mostro.getDannoBase());

        aggiornaMenuArmi();
        aggiornaMenuConsumabili();
    }

    /**
     * Popola la tendina delle armi con quelle presenti nell'inventario,
     * più l'opzione "Mani Nude", e seleziona l'arma attualmente equipaggiata.
     */
    private void aggiornaMenuArmi() {
        cmbArmi.getItems().clear();
        cmbArmi.getItems().add(MANI_NUDE);

        eroe.getInventario().getOggetti().stream()
                .filter(Oggetto::isEquipaggiabile)   // filtro polimorfico: nessun instanceof
                .map(o -> (Arma) o)                  // cast sicuro: solo le armi sono equipaggiabili
                .map(Arma::getDescrizione)           // descrizione completa fornita dall'arma
                .forEach(cmbArmi.getItems()::add);

        Arma equipaggiata = eroe.getArmaEquipaggiata();
        cmbArmi.setValue(equipaggiata != null ? equipaggiata.getDescrizione() : MANI_NUDE);
    }

    /**
     * Popola la tendina dei consumabili e abilita i relativi controlli
     * solo se nell'inventario è presente almeno un consumabile.
     */
    private void aggiornaMenuConsumabili() {
        cmbPozioni.getItems().clear();

        eroe.getInventario().getOggetti().stream()
                .filter(Oggetto::isConsumabile)      // filtro polimorfico: nessun instanceof
                .map(Oggetto::getNome)
                .forEach(cmbPozioni.getItems()::add);

        boolean haConsumabili = !cmbPozioni.getItems().isEmpty();
        if (!haConsumabili) {
            cmbPozioni.getItems().add(NESSUN_CONSUMABILE);
        }

        cmbPozioni.getSelectionModel().selectFirst();
        btnPozione.setDisable(!haConsumabili);
        cmbPozioni.setDisable(!haConsumabili);
    }



    /**
     * Aggiorna l'etichetta dell'arma dell'eroe, o mostra "Mani Nude" se disarmato.
     */
    private void aggiornaTestoArmaEquipaggiata() {
        Arma equipaggiata = eroe.getArmaEquipaggiata();
        lblArmaEroe.setText("Arma: " + (equipaggiata != null ? equipaggiata.getDescrizione() : MANI_NUDE));
    }

    /**
     * Sincronizza etichette e barre dei punti vita di eroe e mostro col loro stato attuale.
     */
    private void aggiornaBarreVita() {
        lblHpEroe.setText("HP: " + eroe.getPuntiVitaAttuali() + " / " + eroe.getPuntiVitaMax());
        barraHpEroe.setProgress((double) eroe.getPuntiVitaAttuali() / eroe.getPuntiVitaMax());

        lblHpMostro.setText("HP: " + mostro.getPuntiVitaAttuali() + " / " + mostro.getPuntiVitaMax());
        barraHpMostro.setProgress((double) mostro.getPuntiVitaAttuali() / mostro.getPuntiVitaMax());
    }

    /**
     * Aggiunge una riga al log di battaglia e scorre fino in fondo.
     * @param messaggio il testo da accodare
     */
    private void scriviLog(String messaggio) {
        txtBattleLog.appendText("\n" + messaggio);
        txtBattleLog.setScrollTop(Double.MAX_VALUE);
    }

    /**
     * Esegue il turno d'attacco del primo combattente e la reazione del secondo,
     * gestendo il risultato .
     */
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

    /**
     * Usa il consumabile selezionato .
     */
    @FXML
    public void handlePozione(ActionEvent event) {
        String nomeSelezionato = cmbPozioni.getValue();
        if (nomeSelezionato == null || nomeSelezionato.equals(NESSUN_CONSUMABILE)) return;

        int indice = trovaIndiceConsumabile(nomeSelezionato);

        if (indice != -1) {
            eroe.usaOggettoDallInventario(indice);
            scriviLog("♥ Hai consumato: " + nomeSelezionato + "!");
            aggiornaMenuConsumabili();

            // Usare un consumabile fa saltare il turno.
            scriviLog(mostro.eseguiTurno(eroe, arena));
        }

        if (!eroe.isVivo()) gestisciSconfitta();
        aggiornaBarreVita();
    }

    /**
     * Cerca nell'inventario l'indice del primo consumabile con il nome dato.
     * @param nome il nome del consumabile da cercare
     * @return l'indice nell'inventario, oppure -1 se non presente
     */
    private int trovaIndiceConsumabile(String nome) {
        Inventario inventario = eroe.getInventario();
        return IntStream.range(0, inventario.getDimensione())
                .filter(i -> {
                    Oggetto o = inventario.getOggetto(i);
                    return o.isConsumabile() && o.getNome().equals(nome);
                })
                .findFirst()
                .orElse(-1); // -1 = consumabile non trovato
    }

    /**
     * Equipaggia l'arma scelta nella tendina e lo salva nella partita.
     */
    @FXML
    public void handleCambiaArma(ActionEvent event) {
        String nomeArmaSelezionata = cmbArmi.getValue();
        if (nomeArmaSelezionata == null) return;

        if (nomeArmaSelezionata.equals(MANI_NUDE)) {
            eroe.equipaggiaArma(null);
        } else {
            // Si individua nell'inventario l'arma la cui descrizione coincide con quella selezionata.
            eroe.getInventario().getOggetti().stream()
                    .filter(Oggetto::isEquipaggiabile)
                    .map(o -> (Arma) o)
                    .filter(a -> a.getDescrizione().equals(nomeArmaSelezionata))
                    .findFirst()
                    .ifPresent(eroe::equipaggiaArma);
        }

        Sessione.getPartitaRepo().salvaPartita(Sessione.getPartitaCorrente());
        aggiornaTestoArmaEquipaggiata();

        Arma equipaggiata = eroe.getArmaEquipaggiata();
        scriviLog("Hai equipaggiato: " + (equipaggiata != null ? equipaggiata.getNome() : MANI_NUDE));
    }

    /**
     * Assegna ricompense , avanza nel dungeon e chiude lo scontro come vinto.
     */
    private void gestisciVittoria() {
        scriviLog("VITTORIA! +XP e +Monete!");

        eroe.guadagnaEsperienza(mostro.getExpFornita());
        eroe.aggiungiMonete(mostro.getMoneteRilasciate());
        eroe.avanzaNelDungeon();

        Sessione.getPartitaRepo().salvaPartita(Sessione.getPartitaCorrente());
        chiudiBattaglia("Torna alla Home", "#28a745");
    }

    /**
     * Elimina il salvataggio e chiude lo scontro.
     */
    private void gestisciSconfitta() {
        scriviLog("SEI MORTO... GAME OVER.");

        // Sconfitta = il salvataggio viene rimosso dal DB.
        Sessione.getPartitaRepo().eliminaPartita(Sessione.getPartitaCorrente());
        Sessione.setPartitaCorrente(null);

        chiudiBattaglia("GAME OVER - Torna al Menu", "#d9534f");
    }

    /**
     * Disabilita le azioni e configura il bottone di uscita con testo e colore dati.
     * @param testoBottone etichetta del bottone di uscita
     * @param colore colore di sfondo
     */
    private void chiudiBattaglia(String testoBottone, String colore) {
        aggiornaBarreVita();
        disabilitaAzioni();

        btnIndietro.setVisible(true);
        btnIndietro.setManaged(true);
        btnIndietro.setText(testoBottone);
        btnIndietro.setStyle("-fx-background-color: " + colore + "; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    /**
     * Disabilita tutti i controlli di gioco
     */
    private void disabilitaAzioni() {
        btnAttacca.setDisable(true);
        btnPozione.setDisable(true);
        cmbArmi.setDisable(true);
        cmbPozioni.setDisable(true);
    }

    /**
     * Torna al menu se non c'è una partita attiva , altrimenti alla home.
     */
    @FXML
    public void handleIndietro(ActionEvent event) {
        if (Sessione.getPartitaCorrente() == null) {
            MainGUI.cambiaScena("/menu.fxml");
        } else {
            MainGUI.cambiaScena("/home.fxml");
        }
    }
}