package it.unicam.cs.mpgc.rpg126161.model;

/**
 * Gestisce il flusso dello scontro tra due Combattenti.
 */
public class GestoreCombattimento {

    private Combattente sfidante1;
    private Combattente sfidante2;

    public GestoreCombattimento(Combattente sfidante1, Combattente sfidante2) {
        this.sfidante1 = sfidante1;
        this.sfidante2 = sfidante2;
    }

    public Combattente avviaScontroAutomatizzato() {
        System.out.println("\n⚔️ --- INIZIO SCONTRO: " + sfidante1.getNome() + " VS " + sfidante2.getNome() + " --- ⚔️\n");
        int turno = 1;

        while (sfidante1.isVivo() && sfidante2.isVivo()) {
            System.out.println("--- TURNO " + turno + " ---");

            sfidante1.attacca(sfidante2);
            if (!sfidante2.isVivo()) break;

            sfidante2.attacca(sfidante1);
            turno++;
        }

        Combattente vincitore = sfidante1.isVivo() ? sfidante1 : sfidante2;
        Combattente sconfitto = sfidante1.isVivo() ? sfidante2 : sfidante1;

        System.out.println("🏆 IL COMBATTIMENTO È TERMINATO!");
        System.out.println("💀 " + sconfitto.getNome() + " è stato sconfitto.");
        System.out.println("👑 Il vincitore è " + vincitore.getNome() + "!\n");

        return vincitore;
    }
}