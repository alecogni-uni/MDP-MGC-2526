package it.unicam.cs.mpgc.rpg126161.repository;

import it.unicam.cs.mpgc.rpg126161.model.Partita;
import it.unicam.cs.mpgc.rpg126161.utils.DatabaseManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.function.Consumer;

/**
 * Gestisce la persistenza delle partite su database tramite JPA.
 * L'identità di un salvataggio è data dal nome dell'eroe: un nuovo salvataggio
 * con lo stesso nome sovrascrive quello esistente.
 */
public class PartitaRepository {

    /**
     * Esegue un'operazione all'interno di una transazione, gestendo commit,
     * rollback in caso di errore e chiusura dell'EntityManager.
     * @param operazione l'operazione da eseguire sull'EntityManager
     */
    private void inTransazione(Consumer<EntityManager> operazione) {
        EntityManager em = DatabaseManager.getInstance().getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            operazione.accept(em);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Salva una partita: se ne esiste già una per lo stesso nome eroe la aggiorna,
     * altrimenti la crea.
     * @param partita la partita da salvare
     */
    public void salvaPartita(Partita partita) {
        inTransazione(em -> {
            List<Partita> esistenti = em.createQuery(
                            "SELECT p FROM Partita p WHERE p.eroe.nome = :nome", Partita.class)
                    .setParameter("nome", partita.getEroe().getNome())
                    .getResultList();

            if (!esistenti.isEmpty()) {
                partita.setId(esistenti.get(0).getId()); // riusa l'ID del record esistente per aggiornarlo
                em.merge(partita);
            } else {
                em.persist(partita);
            }
        });
    }

    /**
     * Restituisce tutte le partite salvate.
     * @return la lista di tutte le partite presenti nel database
     */
    public List<Partita> getTutteLePartite() {
        EntityManager em = DatabaseManager.getInstance().getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Partita p", Partita.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Elimina una partita dal database.
     * @param partita la partita da eliminare
     */
    public void eliminaPartita(Partita partita) {
        // merge riaggancia l'entità al contesto di persistenza prima della rimozione.
        inTransazione(em -> em.remove(em.merge(partita)));
    }

    /**
     * Verifica in modo efficiente (query COUNT, senza caricare le entità) se esiste
     * almeno un salvataggio.
     * @return true se è presente almeno una partita salvata
     */
    public boolean hasSalvataggi() {
        EntityManager em = DatabaseManager.getInstance().getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(p) FROM Partita p", Long.class).getSingleResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
}