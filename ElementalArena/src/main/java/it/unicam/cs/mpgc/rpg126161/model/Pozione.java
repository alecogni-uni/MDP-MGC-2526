package it.unicam.cs.mpgc.rpg126161.model;

import lombok.Getter;

@Getter
public class Pozione extends Oggetto {
    private int percentualeCura;

    public Pozione(String nome, int percentualeCura, int valore) {
        super(nome, valore);
        this.percentualeCura = percentualeCura;
    }

    @Override
    public void usa(Eroe eroe) {
        int hpDaCurare = (eroe.getPuntiVitaMax() * percentualeCura) / 100;
        eroe.curati(hpDaCurare);
        System.out.println("🧪 " + eroe.getNome() + " ha usato " + getNome());
        // La logica di rimozione dall'inventario la gestirà l'Eroe
    }
}