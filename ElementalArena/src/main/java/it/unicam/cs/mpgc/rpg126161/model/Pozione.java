package it.unicam.cs.mpgc.rpg126161.model;

import lombok.Getter;
import jakarta.persistence.*;

@Getter
@Entity
/**
 * Rappresenta una pozione utilizzabile nel gioco.
 * Estende la classe base Oggetto.
 */
public class Pozione extends Oggetto {

    private int percentualeCura;


    protected Pozione() {}

    public Pozione(String nome, int percentualeCura, int valore) {
        super(nome, valore);
        this.percentualeCura = percentualeCura;
    }

    @Override
    public void usa(Eroe eroe) {
        int hpDaCurare = (eroe.getPuntiVitaMax() * percentualeCura) / 100; // applica la cura all'eroe
        eroe.curati(hpDaCurare);
        System.out.println("🧪 " + eroe.getNome() + " ha usato " + getNome()); // stringa per GUI
    }

    // L'inventario userà questo metodo polimorfico
    @Override
    public boolean isConsumabile() {
        return true;
    }

    @Override
    public Oggetto copia() {
        // Restituisce un nuovo oggetto clonato (così il negozio tiene il suo, e l'eroe prende questo)
        return new Pozione(this.getNome(), this.percentualeCura, this.getValore());
    }

    @Override
    public boolean isPezzoUnico() {
        return false; // Non scompare dal negozio dopo l'acquisto
    }
}