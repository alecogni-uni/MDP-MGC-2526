package it.unicam.cs.mpgc.rpg126161.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce l'acquisto di armi e pozioni.
 */
public class Negozio {
    private List<Arma> armiInVendita;
    private List<Pozione> pozioniInVendita;

    public Negozio() {
        this.armiInVendita = new ArrayList<>();
        this.pozioniInVendita = new ArrayList<>();

        // Reparto Armi (Si esauriscono quando comprate)
        armiInVendita.add(new Arma("Ascia di Terra", 15, Elemento.TERRA, 30));
        armiInVendita.add(new Arma("Arco del Vento", 20, Elemento.ARIA, 60));
        armiInVendita.add(new Arma("Spada della Luce", 35, Elemento.LUCE, 120));

        // Reparto Pozioni (Rifornimento infinito)
        pozioniInVendita.add(new Pozione("Pozione Piccola (Cur. 25%)", 25, 10));
        pozioniInVendita.add(new Pozione("Pozione Media (Cur. 50%)", 50, 25));
        pozioniInVendita.add(new Pozione("Elisir Totale (Cur. 100%)", 100, 60));
    }

    public void mostraRepartoArmi(Eroe eroe) {
        System.out.println("\n⚔️ --- REPARTO ARMI --- ⚔️");
        for (int i = 0; i < armiInVendita.size(); i++) {
            Arma a = armiInVendita.get(i);
            System.out.println("[" + (i + 1) + "] " + a.getNome() + " | Danno: " + a.getDannoBase() + " | Elem: " + a.getElemento() + " | 💰 " + a.getValore());
        }
    }

    public void compraArma(Eroe eroe, int indice) {
        if (indice < 1 || indice > armiInVendita.size()) {
            System.out.println("❌ Scelta non valida.");
            return;
        }

        Arma arma = armiInVendita.get(indice - 1);
        if (eroe.spendiMonete(arma.getValore())) {
            // USIAMO IL NUOVO INVENTARIO
            eroe.getInventario().aggiungi(arma);
            System.out.println("🛒 Acquistata con successo: " + arma.getNome() + "!");
            armiInVendita.remove(arma); // Rimuove l'arma dal negozio
        } else {
            System.out.println("❌ Non hai abbastanza monete!");
        }
    }

    public void mostraRepartoPozioni(Eroe eroe) {
        System.out.println("\n🧪 --- REPARTO POZIONI --- 🧪");
        for (int i = 0; i < pozioniInVendita.size(); i++) {
            Pozione p = pozioniInVendita.get(i);
            System.out.println("[" + (i + 1) + "] " + p.getNome() + " | 💰 " + p.getValore());
        }
    }

    public void compraPozione(Eroe eroe, int indice) {
        if (indice < 1 || indice > pozioniInVendita.size()) {
            System.out.println("❌ Scelta non valida.");
            return;
        }

        Pozione pozioneTemplate = pozioniInVendita.get(indice - 1);
        if (eroe.spendiMonete(pozioneTemplate.getValore())) {
            // Creiamo una nuova istanza della pozione e la mettiamo nel NUOVO INVENTARIO
            Pozione nuovaPozione = new Pozione(pozioneTemplate.getNome(), pozioneTemplate.getPercentualeCura(), pozioneTemplate.getValore());
            eroe.getInventario().aggiungi(nuovaPozione);
            System.out.println("🛒 Acquistata con successo: " + nuovaPozione.getNome() + "!");
        } else {
            System.out.println("❌ Non hai abbastanza monete!");
        }
    }
}