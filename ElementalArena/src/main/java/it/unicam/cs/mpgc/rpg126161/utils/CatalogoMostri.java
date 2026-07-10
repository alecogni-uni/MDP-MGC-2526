package it.unicam.cs.mpgc.rpg126161.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg126161.model.Mostro;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Carica il catalogo dei mostri dal file JSON di configurazione.
 */
public class CatalogoMostri {

    private static final String PERCORSO_FILE = "data/mostri.json";

    /**
     * Legge i mostri dal file JSON e ne ripristina la vita al massimo.
     * @return la lista dei mostri caricati, o una lista vuota in caso di errore di lettura
     */
    public static List<Mostro> caricaMostriDaJson() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(PERCORSO_FILE)) {
            Type listType = new TypeToken<ArrayList<Mostro>>(){}.getType();
            List<Mostro> mostriCaricati = gson.fromJson(reader, listType);
            mostriCaricati.forEach(Mostro::ripristinaVitaCompletamente);
            return mostriCaricati;
        } catch (IOException e) {
            System.err.println("❌ Errore nel caricamento del file " + PERCORSO_FILE);
            return new ArrayList<>();
        }
    }
}