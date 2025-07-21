package passwordcracker;

public class CibleLocale implements Cible {

    // La logique d'authentification est maintenant directement dans cette classe
    // pour une performance maximale.
    private static final String VALID_LOGIN = "admin";
    private static final String VALID_PASSWORD = "pass123";

    @Override
    public boolean authentifier(String nomUtilisateur, String motDePasse) {
        // La vérification est maintenant instantanée et en mémoire.
        // Cela évite le coût énorme de créer un nouveau processus pour chaque tentative.
        return VALID_LOGIN.equals(nomUtilisateur) && VALID_PASSWORD.equals(motDePasse);
    }
}