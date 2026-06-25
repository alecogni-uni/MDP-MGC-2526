package it.unicam.cs.mpgc.rpg126161.model;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce la collezione di oggetti.
 * Ora espone metodi per l'uso e la rimozione automatica.
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

    // L'inventario ora sa che se un oggetto è una pozione, va rimosso dopo l'uso
    public void consumaOggetto(int indice, Eroe eroe) {
        Oggetto o = getOggetto(indice);
        if (o != null) {
            o.usa(eroe);
            if (o instanceof Pozione) {
                this.oggetti.remove(o);
            }
        }
    }

    public Oggetto getOggetto(int indice) {
        return (indice >= 0 && indice < oggetti.size()) ? oggetti.get(indice) : null;
    }

    public int getDimensione() { return oggetti.size(); }
    public boolean isVuoto() { return oggetti.isEmpty(); }
}