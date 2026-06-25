package it.unicam.cs.mpgc.rpg126161.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg126161.model.Arma;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CaricatoreArmi {
    public static List<Arma> caricaArmiDaJson() {
        try (FileReader reader = new FileReader("data/armi.json")) {
            return new Gson().fromJson(reader, new TypeToken<ArrayList<Arma>>(){}.getType());
        } catch (Exception e) {
            System.err.println("❌ Errore caricamento armi: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}