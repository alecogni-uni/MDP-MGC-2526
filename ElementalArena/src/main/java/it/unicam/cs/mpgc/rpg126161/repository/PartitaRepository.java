package it.unicam.cs.mpgc.rpg126161.repository;

import it.unicam.cs.mpgc.rpg126161.model.Partita;
import it.unicam.cs.mpgc.rpg126161.utils.DatabaseManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class PartitaRepository {

    public void salvaPartita(Partita partita) {
        EntityManager em = DatabaseManager.getInstance().getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            // Cerca se esiste già la partita per questo eroe
            List<Partita> esistenti = em.createQuery("SELECT p FROM Partita p WHERE p.eroe.nome = :nome", Partita.class)
                    .setParameter("nome", partita.getEroe().getNome()).getResultList();

            if (!esistenti.isEmpty()) {
                partita.setId(esistenti.get(0).getId()); // Mantieni l'ID esistente
                em.merge(partita);
            } else {
                em.persist(partita);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Partita> getTutteLePartite() {
        EntityManager em = DatabaseManager.getInstance().getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Partita p", Partita.class).getResultList();
        } finally {
            em.close();
        }
    }
    public void eliminaPartita(Partita partita) {
        EntityManager em = DatabaseManager.getInstance().getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Partita daEliminare = em.merge(partita);
            em.remove(daEliminare);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}