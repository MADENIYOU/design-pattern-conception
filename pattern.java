import java.util.Scanner;

public class pattern {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bienvenue dans Password Cracker Factory");

        int choixAttack = 0;
        while (choixAttack != 1 && choixAttack != 2) {
            System.out.println("Choisissez la méthode d'attaque :");
            System.out.println("1. Attaque par dictionnaire");
            System.out.println("2. Attaque brute force");
            if (scanner.hasNextInt()) {
                choixAttack = scanner.nextInt();
                if (choixAttack != 1 && choixAttack != 2) {
                    System.out.println("Erreur : Veuillez entrer 1 ou 2.");
                }
            } 
        }

        int choixTarget = 0;
        while (choixTarget != 1 && choixTarget != 2) {
            System.out.println("Choisissez la cible :");
            System.out.println("1. Locale");
            System.out.println("2. En ligne");
            if (scanner.hasNextInt()) {
                choixTarget = scanner.nextInt();
                if (choixTarget != 1 && choixTarget != 2) {
                    System.out.println("Erreur : Veuillez entrer 1 ou 2.");
                }
            }
        }

        System.out.print("Entrez le login : ");
        String login = scanner.next();

        String typeAttack = (choixAttack == 1) ? "dictionnaire" : "brute force";
        String target = (choixTarget == 1) ? "locale" : "en ligne";

        System.out.println("Lancement de l'attaque " + typeAttack + " sur la cible " + target + " avec le login : " + login);
        
        scanner.close();
}
}