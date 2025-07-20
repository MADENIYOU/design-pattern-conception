package passwordcracker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class AttaqueBruteForce implements StrategieAttaque {
    private final char[] alphabet;
    private final int minLength;
    private final int maxLength;
    private final AtomicBoolean passwordFound = new AtomicBoolean(false);
    private final AtomicLong attemptCounter = new AtomicLong(0);
    private static final int REPORT_INTERVAL = 500_000; // Afficher le statut tous les N essais
    private static final long TIME_REPORT_INTERVAL_MS = 1000; // Afficher le statut toutes les 5 secondes
    private volatile long lastReportTime = System.currentTimeMillis();

    public AttaqueBruteForce(char[] alphabet, int minLength, int maxLength) {
        this.alphabet = alphabet;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public void craquer(Cible cible, String nomUtilisateur) {
        int coreCount = Runtime.getRuntime().availableProcessors();
        System.out.println("Lancement de l'attaque par force brute optimisée avec " + coreCount + " threads...");
        ExecutorService executor = Executors.newFixedThreadPool(coreCount);
        long startTime = System.currentTimeMillis();

        for (char firstChar : alphabet) {
            executor.submit(() -> {
                if (passwordFound.get()) return;
                for (int length = minLength; length <= maxLength; length++) {
                    if (passwordFound.get()) return;
                    char[] motDePasse = new char[length];
                    motDePasse[0] = firstChar;
                    if (length == 1) {
                        testerMotDePasse(new String(motDePasse), cible, nomUtilisateur, startTime);
                    } else {
                        genererCombinaisons(motDePasse, 1, cible, nomUtilisateur, startTime);
                    }
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!passwordFound.get()) {
            System.out.println("\nAttaque par force brute terminée sans succès.");
        }
    }

    private void genererCombinaisons(char[] motDePasse, int position, Cible cible, String nomUtilisateur, long startTime) {
        if (passwordFound.get()) {
            return;
        }

        if (position == motDePasse.length) {
            testerMotDePasse(new String(motDePasse), cible, nomUtilisateur, startTime);
            return;
        }

        for (char c : alphabet) {
            if (passwordFound.get()) {
                return;
            }
            motDePasse[position] = c;
            genererCombinaisons(motDePasse, position + 1, cible, nomUtilisateur, startTime);
        }
    }

    private void testerMotDePasse(String pass, Cible cible, String nomUtilisateur, long startTime) {
        if (passwordFound.get()) {
            return;
        }
        long attempts = attemptCounter.incrementAndGet();
        long currentTime = System.currentTimeMillis();

        // Rapport basé sur le nombre d'essais OU sur le temps
        if (attempts % REPORT_INTERVAL == 0 || (currentTime - lastReportTime) >= TIME_REPORT_INTERVAL_MS) {
            long elapsedTime = (currentTime - startTime) / 1000;
            long speed = elapsedTime > 0 ? attempts / elapsedTime : 0;
            System.out.printf("Essais: %d | Vitesse: %d mdp/s | Test en cours: %s\r", attempts, speed, pass);
            lastReportTime = currentTime; // Mettre à jour le temps du dernier rapport
        }

        if (cible.authentifier(nomUtilisateur, pass)) {
            if (passwordFound.compareAndSet(false, true)) {
                System.out.println("\n***********************************");
                System.out.println("*** Mot de passe trouvé : " + pass + " ***");
                System.out.println("***********************************\n");
                System.exit(0);
            }
        }
    }
}
