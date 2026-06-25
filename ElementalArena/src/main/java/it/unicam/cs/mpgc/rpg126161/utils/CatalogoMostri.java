package it.unicam.cs.mpgc.rpg126161.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg126161.model.Mostro;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CatalogoMostri {

    public static List<Mostro> caricaMostriDaJson() {
        Gson gson = new Gson();
        String percorsoFile = "data/mostri.json";
        try (FileReader reader = new FileReader(percorsoFile)) {
            Type listType = new TypeToken<ArrayList<Mostro>>(){}.getType();
            List<Mostro> mostriCaricati = gson.fromJson(reader, listType);

            // Ripristiniamo la vita al massimo per ogni mostro
            for (Mostro m : mostriCaricati) {
                m.ripristinaVitaCompletamente();
            }
            return mostriCaricati;
        } catch (IOException e) {
            System.err.println("❌ Errore nel caricamento del file " + percorsoFile);
            return new ArrayList<>();
        }
    }
}