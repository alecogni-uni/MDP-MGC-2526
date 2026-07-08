package it.unicam.cs.mpgc.rpg126161.model;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce la collezione di oggetti dell'Eroe.
 */
@Getter
@Entity
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Oggetto> oggetti;

    public Inventario() {
        this.oggetti = new ArrayList<>();
    }

    public void aggiungi(Oggetto o) {
        this.oggetti.add(o);
    }

    /**
     * Usa l'oggetto e lo rimuove dall'inventario se necessario.
     */
    public void consumaOggetto(int indice, Eroe eroe) {
        Oggetto o = getOggetto(indice);
        if (o != null) {
            o.usa(eroe);

            // Se l'oggetto è consumabile, lo rimuoviamo dall'inventario
            if (o.isConsumabile()) {
                this.oggetti.remove(o);
            }
        }
    }

    public Oggetto getOggetto(int indice) {
        return (indice >= 0 && indice < oggetti.size()) ? oggetti.get(indice) : null;
    }

    public int getDimensione() {
        return oggetti.size();
    }

    public boolean isVuoto() {
        return oggetti.isEmpty();
    }
}