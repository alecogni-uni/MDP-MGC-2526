package it.unicam.cs.mpgc.rpg126161.model;

import lombok.Getter;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce l'inventario degli oggetti in vendita e la logica di acquisto.
 */
@Getter
@Entity
public class Negozio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeShop;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Oggetto> articoliInVendita;

    protected Negozio() {}

    public Negozio(String nomeShop) {
        this.nomeShop = nomeShop;
        this.articoliInVendita = new ArrayList<>();
    }

    /**
     * Aggiunge un singolo articolo alla vetrina.
     */
    public void aggiungiArticolo(Oggetto o) {
        this.articoliInVendita.add(o);
    }

    /**
     * Metodo di utilità per popolare il negozio con una lista (es. caricata da JSON).
     */
    public void popolaNegozio(List<Arma> armi) {
        for (Arma a : armi) {
            this.aggiungiArticolo(a);
        }
    }

    /**
     * Gestisce la transazione di acquisto tra Eroe e Negozio.
     * @param eroe L'eroe che effettua l'acquisto.
     * @param indice L'indice dell'oggetto nella lista.
     */
    public void compraArticolo(Eroe eroe, int indice) {
        // Indice passato come 1-based (dal menu) convertito in 0-based
        int indiceReale = indice - 1;

        if (indiceReale < 0 || indiceReale >= articoliInVendita.size()) {
            return;
        }

        Oggetto oggettoScelto = articoliInVendita.get(indiceReale);

        if (eroe.spendiMonete(oggettoScelto.getValore())) {
            // Se è una pozione, ne aggiungiamo una copia (per permettere acquisti multipli)
            if (oggettoScelto instanceof Pozione) {
                Pozione p = (Pozione) oggettoScelto;
                eroe.getInventario().aggiungi(new Pozione(p.getNome(), p.getPercentualeCura(), p.getValore()));
            }
            // Se è un'arma, è un pezzo unico: la spostiamo dal negozio all'inventario
            else if (oggettoScelto instanceof Arma) {
                eroe.getInventario().aggiungi(oggettoScelto);
                articoliInVendita.remove(oggettoScelto);
            }
        }
    }
}