package it.unicam.cs.mpgc.rpg126161.model;

import lombok.Getter;
import jakarta.persistence.*;

/**
 * Rappresenta una pozione utilizzabile nel gioco.
 * Estende la classe base Oggetto.
 */
@Getter
@Entity
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

    // L'inventario usa questo metodo polimorfico per decidere se rimuovere l'oggetto dopo l'uso.
    @Override
    public boolean isConsumabile() {
        return true;
    }

    @Override
    public Oggetto copia() {
        // Restituisce un nuovo oggetto clonato (così il negozio tiene il suo, e l'eroe prende questo).
        return new Pozione(this.getNome(), this.percentualeCura, this.getValore());
    }

    @Override
    public boolean isPezzoUnico() {
        return false; // Non scompare dal negozio dopo l'acquisto.
    }

    @Override
    public String getIconPath() {
        return "/images/potion.png";
    }

    @Override
    public String getDescrizione() {
        return "Consumabile";
    }

    // getEtichettaAzione() eredita "Usa" dalla classe base.
    // isEquipaggiabile() e isEquipaggiato() ereditano false: una pozione non si equipaggia.
}