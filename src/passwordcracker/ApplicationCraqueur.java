package passwordcracker;

public class ApplicationCraqueur {
    public static void main(String[] args) {
        String typeAttaque = null;
        String typeCible = null;
        String nomUtilisateur = null;
        String urlCible = null;
        String cheminDictionnaire = "dictionnaire.txt";
        int minLength = 1;
        int maxLength = 7; // Longueur par défaut

        // Analyse des arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--type":
                    if (i + 1 < args.length) typeAttaque = args[++i];
                    break;
                case "--target":
                    if (i + 1 < args.length) typeCible = args[++i];
                    break;
                case "--login":
                    if (i + 1 < args.length) nomUtilisateur = args[++i];
                    break;
                case "--url":
                    if (i + 1 < args.length) urlCible = args[++i];
                    break;
                case "--dict":
                    if (i + 1 < args.length) cheminDictionnaire = args[++i];
                    break;
                case "--length":
                     if (i + 1 < args.length) {
                        try {
                            maxLength = Integer.parseInt(args[++i]);
                            minLength = maxLength; // Pour tester une longueur spécifique
                        } catch (NumberFormatException e) {
                            System.err.println("Erreur: La longueur doit être un nombre.");
                            return;
                        }
                    }
                    break;
            }
        }

        if (typeAttaque == null || typeCible == null || nomUtilisateur == null) {
            System.out.println("Utilisation : java -cp src passwordcracker.ApplicationCraqueur --type <bruteforce|dictionnaire> --target <local|en_ligne> --login <nom_utilisateur> [--url <url_cible>] [--dict <chemin_fichier>] [--length <longueur>]");
            return;
        }

        // --- Création dynamique avec les fabriques ---

        // 1. Choisir la fabrique de stratégie
        FabriqueStrategieAttaque fabriqueStrategie;
        if ("bruteforce".equalsIgnoreCase(typeAttaque)) {
            fabriqueStrategie = new FabriqueBruteForce(minLength, maxLength);
        } else if ("dictionnaire".equalsIgnoreCase(typeAttaque)) {
            fabriqueStrategie = new FabriqueDictionnaire(cheminDictionnaire);
        } else {
            System.out.println("Type d'attaque non supporté.");
            return;
        }

        // 2. Choisir la fabrique de cible
        FabriqueCible fabriqueCible;
        if ("local".equalsIgnoreCase(typeCible)) {
            fabriqueCible = new FabriqueCibleLocale();
        } else if ("en_ligne".equalsIgnoreCase(typeCible)) {
            if (urlCible == null) {
                System.out.println("Erreur : --url est requis pour les cibles en ligne.");
                return;
            }
            fabriqueCible = new FabriqueCibleEnLigne(urlCible);
        } else {
            System.out.println("Type de cible non supporté.");
            return;
        }

        // 3. Créer les objets en utilisant les fabriques
        StrategieAttaque strategie = fabriqueStrategie.creerStrategieAttaque();
        Cible cible = fabriqueCible.creerCible();

        // --- Lancement de l'attaque ---
        System.out.println("\nConfiguration de l'attaque :");
        System.out.println("  - Type d'attaque : " + typeAttaque);
        System.out.println("  - Type de cible   : " + typeCible);
        System.out.println("  - Nom utilisateur: " + nomUtilisateur);
        if (urlCible != null) System.out.println("  - URL Cible       : " + urlCible);
        if ("dictionnaire".equalsIgnoreCase(typeAttaque)) System.out.println("  - Dictionnaire  : " + cheminDictionnaire);
        if ("bruteforce".equalsIgnoreCase(typeAttaque)) System.out.println("  - Longueur testée: " + maxLength);

        strategie.craquer(cible, nomUtilisateur);
    }
}