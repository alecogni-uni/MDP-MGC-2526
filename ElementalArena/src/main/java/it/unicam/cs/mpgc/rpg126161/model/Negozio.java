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

    public void aggiungiArticolo(Oggetto o) {
        this.articoliInVendita.add(o);
    }

    /**
     * Metodo di utilità per popolare il negozio tramite una lista
     * accetta una lista di una qualsiasi sottoclasse di Oggetto (Arma, Pozione, ecc.)
     */
    public void popolaNegozio(List<? extends Oggetto> oggettiNuovi) {
        for (Oggetto o : oggettiNuovi) {
            this.aggiungiArticolo(o);
        }
    }
    /**
     * Gestisce la transazione di acquisto tra Eroe e Negozio.
     */
    public void compraArticolo(Eroe eroe, int indice) {
        int indiceReale = indice - 1;

        if (indiceReale < 0 || indiceReale >= articoliInVendita.size()) {
            return;
        }

        Oggetto oggettoScelto = articoliInVendita.get(indiceReale);

        if (eroe.spendiMonete(oggettoScelto.getValore())) {
            // 1. Chiediamo all'oggetto di fornirci la sua istanza per l'acquisto
            eroe.getInventario().aggiungi(oggettoScelto.copia());

            // 2. Chiediamo all'oggetto se deve sparire dalla vetrina dopo l'acquisto
            if (oggettoScelto.isPezzoUnico()) {
                articoliInVendita.remove(oggettoScelto);
            }
        }
    }
}