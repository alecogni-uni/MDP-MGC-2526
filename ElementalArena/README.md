# ⚔️ Elemental Arena

Elemental Arena è un RPG a turni sviluppato in Java con UI JavaFX.
Il giocatore crea un eroe, affronta una serie di scontri a difficoltà crescente
in un dungeon, ne potenzia l'equipaggiamento acquistando armi e pozioni al negozio
e gestisce il proprio inventario.
## 🚀 Come eseguire il progetto

### Prerequisiti
- Java 21
- Gradle 

### Istruzioni

```bash
git clone <https://github.com/alecogni-uni/MDP-MGC-2526>
cd ElementalArena
```

### Build del progetto
```bash
./gradlew build
```

### Esecuzione
```bash
./gradlew run
```

## 🤖 Uso di strumenti di AI

Nel progetto è stato utilizzato un assistente AI come supporto durante la fase di
refactoring, con le seguenti modalità:

- **Introduzione delle Stream API**: conversione di alcuni pezzi su collezioni in stream,
  verificando di volta in volta che l'uso fosse pertinente e non forzato.
- **Pulizia del codice**: rimozione di codice ripetuto o non utilizzato.
- **Revisione**: analisi delle classi per individuare duplicazioni e responsabilità mal
  collocate (es. la gestione delle transazioni nel repository).
-**Gestione delle Dipendenze**: Supporto nella configurazione del file build.gradle.kts per 
il corretto collegamento e la risoluzione delle dipendenze esterne (es. librerie JavaFX, JPA/Hibernate, JSON parsers).

- **Sviluppo UI (JavaFX)**: Generazione e ottimizzazione della struttura dei file XML (.fxml) e dei fogli di 
stile CSS per creare layout responsivi, griglie di navigazione ed elementi grafici (es. barre della vita, card del negozio).

- **Popolamento Dati Statici (Mocking)**: Creazione massiva dei dataset in formato JSON (come mostri.json e armi.json), 
- includendo il bilanciamento delle statistiche, dei nomi e l'assegnazione degli elementi per arricchire il mondo di gioco in fase iniziale.

📌 Per una descrizione più dettagliata consultare la **Wiki**.