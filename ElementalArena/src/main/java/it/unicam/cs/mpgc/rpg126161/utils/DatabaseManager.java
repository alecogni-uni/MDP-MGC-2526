package it.unicam.cs.mpgc.rpg126161.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Gestisce la connessione al Database tramite JPA (Pattern Singleton).
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private final EntityManagerFactory entityManagerFactory;

    // Costruttore privato (Singleton)
    private DatabaseManager() {
        // "ElementalArenaPU" deve corrispondere esattamente al nome nel persistence.xml
        this.entityManagerFactory = Persistence.createEntityManagerFactory("ElementalArenaPU");
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Fornisce un EntityManager per eseguire le operazioni sul DB.
     */
    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}