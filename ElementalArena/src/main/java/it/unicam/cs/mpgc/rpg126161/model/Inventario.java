package it.unicam.cs.mpgc.rpg126161.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce la collezione di oggetti posseduti da un'entità.
 */
public class Inventario {
    private List<Oggetto> oggetti;

    public Inventario() {
        this.oggetti = new ArrayList<>();
    }

    public void aggiungi(Oggetto o) {
        this.oggetti.add(o);
        System.out.println("📦 Aggiunto all'inventario: " + o.getNome());
    }

    public void rimuovi(Oggetto o) {
        this.oggetti.remove(o);
    }

    public Oggetto getOggetto(int indice) {
        if (indice >= 0 && indice < oggetti.size()) {
            return oggetti.get(indice);
        }
        return null;
    }

    public int getDimensione() {
        return oggetti.size();
    }

    public boolean isVuoto() {
        return oggetti.isEmpty();
    }

    public void mostraContenuto() {
        if (isVuoto()) {
            System.out.println("L'inventario è vuoto.");
            return;
        }
        for (int i = 0; i < oggetti.size(); i++) {
            System.out.println("[" + i + "] " + oggetti.get(i).getNome());
        }
    }

    // Serve a Gson per salvare i dati
    public List<Oggetto> getOggetti() {
        return oggetti;
    }
}