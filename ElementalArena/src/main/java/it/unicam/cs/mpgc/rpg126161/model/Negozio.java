package it.unicam.cs.mpgc.rpg126161.model;

import lombok.Getter;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce gli oggetti in vendita e la transazione economica di acquisto.
 * Non conosce i tipi concreti degli oggetti: il comportamento specifico
 * (cosa consegnare, se rimuovere l'articolo) è delegato all'oggetto stesso.
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
     * Popola il negozio con una lista di oggetti di qualsiasi sottoclasse
     */
    public void popolaNegozio(List<? extends Oggetto> oggettiNuovi) {
        oggettiNuovi.forEach(this::aggiungiArticolo);
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
            // L'oggetto fornisce l'istanza da consegnare all'eroe (clone o sé stesso).
            eroe.getInventario().aggiungi(oggettoScelto.copia());

            // L'oggetto decide se deve sparire dalla vetrina dopo l'acquisto.
            if (oggettoScelto.isPezzoUnico()) {
                articoliInVendita.remove(oggettoScelto);
            }
        }
    }
}