public class CrackerApp {
    public static void main(String[] args) {
        String type = "";
        String target = "";
        String login = "";

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--type":
                    type = args[++i];
                    break;
                case "--target":
                    target = args[++i];
                    break;
                case "--login":
                    login = args[++i];
                    break;
            }
        }

        if (type.isEmpty() || target.isEmpty() || login.isEmpty()) {
            System.out.println("Arguments manquants. Veuillez utiliser le menu interactif.");
            pattern.main(new String[0]);
        } else {
            System.out.println("Attaque : " + type + " | Cible : " + target + " | Login : " + login);
            // Lancer la bonne fabrique ici
        }
    }
}
