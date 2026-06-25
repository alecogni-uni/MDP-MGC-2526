package it.unicam.cs.mpgc.rpg126161.model;

import lombok.Getter;
import jakarta.persistence.*;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Oggetto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int valore;

    protected Oggetto() {} // Costruttore vuoto per JPA

    public Oggetto(String nome, int valore) {
        this.nome = nome;
        this.valore = valore;
    }

    public abstract void usa(Eroe eroe);
}