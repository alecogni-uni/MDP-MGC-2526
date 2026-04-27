package it.unicam.cs.mpgc.rpg126161.model;

import lombok.Getter;

@Getter
public class Mostro extends Entita {

    private int dannoBase;
    private int expFornita;
    private int moneteRilasciate; // Bottino

    public Mostro(String nome, int puntiVitaMax, Elemento elemento, int dannoBase, int expFornita, int moneteRilasciate) {
        super(nome, puntiVitaMax, elemento);
        this.dannoBase = dannoBase;
        this.expFornita = expFornita;
        this.moneteRilasciate = moneteRilasciate;
    }

    @Override
    public Elemento getElementoAttacco() {
        return this.getElemento();
    }

    @Override
    public void attacca(Combattente bersaglio) {
        double moltiplicatore = this.getElementoAttacco().getMoltiplicatore(bersaglio.getElementoDifesa());
        int dannoFinale = (int) (this.dannoBase * moltiplicatore);

        System.out.println("👹 Il " + this.getNome() + " attacca e infligge " + dannoFinale + " danni! (x" + moltiplicatore + ")");
        bersaglio.riceviDanno(dannoFinale);
    }
}