package passwordcracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AttaqueDictionnaire implements StrategieAttaque {
    private final String cheminDictionnaire;

    public AttaqueDictionnaire(String cheminDictionnaire) {
        this.cheminDictionnaire = cheminDictionnaire;
    }

    @Override
    public void craquer(Cible cible, String nomUtilisateur) {
        System.out.println("Lancement de l'attaque par dictionnaire...");
        List<String> dictionnaire = chargerDictionnaire();

        if (dictionnaire.isEmpty()) {
            System.out.println("Le fichier dictionnaire est vide ou n'a pas pu être lu.");
            return;
        }

        for (String motDePasse : dictionnaire) {
            System.out.println("Tentative avec : " + motDePasse);
            if (cible.authentifier(nomUtilisateur, motDePasse)) {
                System.out.println("\n*****************************************");
                System.out.println("*** Mot de passe trouvé : " + motDePasse + " ***");
                System.out.println("*****************************************\n");
                return; // Termine après avoir trouvé le mot de passe
            }
        }
        System.out.println("L'attaque par dictionnaire n'a pas réussi à trouver le mot de passe.");
    }

    private List<String> chargerDictionnaire() {
        List<String> dictionnaire = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminDictionnaire))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionnaire.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier dictionnaire : " + e.getMessage());
        }
        return dictionnaire;
    }
}
