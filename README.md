# Password Cracker Factory

Ce projet est une application en ligne de commande permettant de réaliser des attaques de cassage de mot de passe. Il a été conçu en mettant l'accent sur une architecture logicielle modulaire et extensible, en s'appuyant sur des patrons de conception fondamentaux.

L'application permet de lancer deux types d'attaques :
1.  **Attaque par Force Brute :** Teste systématiquement toutes les combinaisons de caractères possibles. Cette implémentation est hautement optimisée grâce au multithreading pour utiliser tous les cœurs du processeur.
2.  **Attaque par Dictionnaire :** Teste une liste de mots de passe potentiels à partir d'un fichier.

Ces attaques peuvent être dirigées contre deux types de cibles :
1.  **Cible Locale :** Une simulation d'authentification locale ultra-rapide, entièrement réalisée en mémoire.
2.  **Cible en Ligne :** Une véritable cible qui effectue des requêtes HTTP POST vers un formulaire de connexion web.

## Architecture et Patrons de Conception

L'architecture du projet a été conçue pour offrir une flexibilité maximale en découplant totalement la création des stratégies d'attaque de la création des cibles. Pour ce faire, elle s'appuie sur **deux hiérarchies de Fabriques Abstraites (Abstract Factory)**.

### 1. Hiérarchie des Fabriques de Stratégies
*   **Fabrique Abstraite :** `FabriqueStrategieAttaque` (Interface)
*   **Fabriques Concrètes :**
    *   `FabriqueBruteForce` : Crée un objet `AttaqueBruteForce`.
    *   `FabriqueDictionnaire` : Crée un objet `AttaqueDictionnaire`.

### 2. Hiérarchie des Fabriques de Cibles
*   **Fabrique Abstraite :** `FabriqueCible` (Interface)
*   **Fabriques Concrètes :**
    *   `FabriqueCibleLocale` : Crée un objet `CibleLocale`.
    *   `FabriqueCibleEnLigne` : Crée un objet `CibleEnLigne`.

### Rôle des Patrons de Conception

*   **Abstract Factory :** Ce patron est utilisé deux fois. Il permet à l'application principale de décider au moment de l'exécution quelle fabrique de stratégie et quelle fabrique de cible instancier en fonction des arguments de l'utilisateur. Le code client est ainsi complètement indépendant des classes concrètes des stratégies et des cibles.

*   **Strategy :** L'interface `StrategieAttaque` et ses implémentations (`AttaqueBruteForce`, `AttaqueDictionnaire`) permettent de changer l'algorithme d'attaque à la volée.

*   **Bridge :** Ce patron relie les stratégies d'attaque aux cibles. Une stratégie contient une référence à une `Cible` et peut opérer sur n'importe quelle implémentation de cette interface, ce qui permet de combiner n'importe quelle attaque avec n'importe quelle cible.

## Description Détaillée des Fichiers

Le code source est entièrement contenu dans le package `passwordcracker`.

*   `ApplicationCraqueur.java`
    *   **Rôle :** Point d'entrée principal de l'application. C'est la classe qui orchestre l'ensemble du processus de cassage de mot de passe.
    *   **Fonctionnalités :**
        *   **Analyse des arguments de la ligne de commande :** Interprète les paramètres `--type` (bruteforce/dictionnaire), `--target` (local/en_ligne), `--login` (nom d'utilisateur), `--url` (pour les cibles en ligne), `--dict` (chemin du dictionnaire), et `--length` (longueur des mots de passe pour la force brute).
        *   **Sélection des Fabriques :** En fonction des arguments `--type` et `--target`, elle instancie dynamiquement la `FabriqueStrategieAttaque` et la `FabriqueCible` appropriées.
        *   **Création des Objets :** Utilise les fabriques sélectionnées pour créer une instance de `StrategieAttaque` et une instance de `Cible`.
        *   **Lancement de l'Attaque :** Appelle la méthode `craquer()` de la stratégie d'attaque, en lui passant l'objet `Cible` et le nom d'utilisateur.
        *   **Affichage de la Configuration :** Affiche un résumé des paramètres d'attaque avant de commencer.

*   `FabriqueStrategieAttaque.java`
    *   **Rôle :** Interface de la Fabrique Abstraite pour la création de stratégies d'attaque.
    *   **Fonctionnalités :** Définit une seule méthode, `creerStrategieAttaque()`, qui doit être implémentée par toutes les fabriques concrètes de stratégies pour retourner une instance de `StrategieAttaque`.

*   `FabriqueBruteForce.java`
    *   **Rôle :** Fabrique concrète pour la création d'une stratégie d'attaque par force brute.
    *   **Fonctionnalités :** Implémente `FabriqueStrategieAttaque`. Sa méthode `creerStrategieAttaque()` retourne une nouvelle instance de `AttaqueBruteForce`, en lui passant l'alphabet de caractères à utiliser et les longueurs minimale et maximale des mots de passe à tester. Elle inclut une méthode utilitaire `genererAlphabet()` pour construire l'alphabet en fonction de critères (minuscules, majuscules, chiffres, symboles).

*   `FabriqueDictionnaire.java`
    *   **Rôle :** Fabrique concrète pour la création d'une stratégie d'attaque par dictionnaire.
    *   **Fonctionnalités :** Implémente `FabriqueStrategieAttaque`. Sa méthode `creerStrategieAttaque()` retourne une nouvelle instance de `AttaqueDictionnaire`, en lui passant le chemin du fichier dictionnaire à utiliser.

*   `FabriqueCible.java`
    *   **Rôle :** Interface de la Fabrique Abstraite pour la création de cibles d'authentification.
    *   **Fonctionnalités :** Définit une seule méthode, `creerCible()`, qui doit être implémentée par toutes les fabriques concrètes de cibles pour retourner une instance de `Cible`.

*   `FabriqueCibleLocale.java`
    *   **Rôle :** Fabrique concrète pour la création d'une cible d'authentification locale.
    *   **Fonctionnalités :** Implémente `FabriqueCible`. Sa méthode `creerCible()` retourne une nouvelle instance de `CibleLocale`.

*   `FabriqueCibleEnLigne.java`
    *   **Rôle :** Fabrique concrète pour la création d'une cible d'authentification en ligne.
    *   **Fonctionnalités :** Implémente `FabriqueCible`. Sa méthode `creerCible()` retourne une nouvelle instance de `CibleEnLigne`, en lui passant l'URL de la cible en ligne.

*   `StrategieAttaque.java`
    *   **Rôle :** Interface du patron Stratégie.
    *   **Fonctionnalités :** Définit le contrat pour toutes les stratégies d'attaque. Elle déclare la méthode `craquer(Cible cible, String nomUtilisateur)`, qui prend en paramètre l'objet `Cible` à attaquer et le nom d'utilisateur cible.

*   `AttaqueBruteForce.java`
    *   **Rôle :** Implémentation concrète de la stratégie d'attaque par force brute.
    *   **Fonctionnalités :**
        *   **Génération de Combinaisons :** Utilise un algorithme récursif pour générer systématiquement toutes les combinaisons de mots de passe possibles en fonction de l'alphabet et des longueurs spécifiées.
        *   **Multithreading :** Emploie un `ExecutorService` pour paralléliser la génération et le test des mots de passe sur plusieurs threads, exploitant ainsi pleinement les cœurs du processeur pour une performance maximale.
        *   **Rapport de Progression :** Affiche en temps réel le nombre d'essais, la vitesse de l'attaque (mots de passe par seconde) et le mot de passe actuellement testé. Le rapport est mis à jour soit après un certain nombre d'essais, soit après un certain intervalle de temps (pour les attaques lentes comme celles en ligne).
        *   **Arrêt Précoce :** S'arrête immédiatement dès que le mot de passe est trouvé.

*   `AttaqueDictionnaire.java`
    *   **Rôle :** Implémentation concrète de la stratégie d'attaque par dictionnaire.
    *   **Fonctionnalités :**
        *   **Chargement du Dictionnaire :** Lit les mots de passe à partir d'un fichier texte spécifié (`dictionnaire.txt` par défaut), un mot de passe par ligne.
        *   **Test Séquentiel :** Itère sur chaque mot de passe du dictionnaire et tente de s'authentifier auprès de la cible.
        *   **Rapport Simple :** Affiche chaque mot de passe testé et s'arrête dès que le mot de passe est trouvé.

*   `Cible.java`
    *   **Rôle :** Interface du patron Pont (côté implémentation).
    *   **Fonctionnalités :** Définit le contrat pour toutes les cibles d'authentification. Elle déclare la méthode `authentifier(String nomUtilisateur, String motDePasse)`, qui prend en paramètre le nom d'utilisateur et le mot de passe à tester, et retourne `true` si l'authentification réussit, `false` sinon.

*   `CibleLocale.java`
    *   **Rôle :** Implémentation concrète d'une cible d'authentification locale.
    *   **Fonctionnalités :** Simule une authentification contre un système local. Pour des raisons de performance et de simplicité, la vérification du mot de passe est effectuée directement en mémoire contre des identifiants codés en dur (`admin` / `pass123`). Cela la rend extrêmement rapide et idéale pour les tests de performance de l'algorithme de force brute.

*   `CibleEnLigne.java`
    *   **Rôle :** Implémentation concrète d'une cible d'authentification en ligne.
    *   **Fonctionnalités :** Effectue de véritables requêtes HTTP POST vers une URL de connexion spécifiée. Elle encode le nom d'utilisateur et le mot de passe, envoie la requête, et analyse la réponse du serveur pour déterminer si l'authentification a réussi (par exemple, en cherchant une chaîne de caractères spécifique comme "Connexion réussie" dans la réponse).

*   `dictionnaire.txt`
    *   **Rôle :** Fichier de données.
    *   **Contenu :** Un simple fichier texte où chaque ligne représente un mot de passe potentiel à tester par l'`AttaqueDictionnaire`.

## Explication des Imports Java

Voici le détail des packages importés dans les classes clés du projet.

#### Dans `AttaqueBruteForce.java`
*   `java.util.concurrent.*`: Ce groupe d'imports est crucial pour l'optimisation multithread.
    *   `ExecutorService`: Pour gérer un pool de threads et y soumettre des tâches.
    *   `Executors`: Une classe utilitaire pour créer facilement des `ExecutorService`.
    *   `TimeUnit`: Pour spécifier les unités de temps (utilisé lors de l'attente de la fin des threads).
    *   `AtomicBoolean`, `AtomicLong`: Des classes atomiques qui garantissent que les opérations (comme vérifier si le mot de passe est trouvé ou incrémenter un compteur) sont sûres dans un environnement multithread, évitant les erreurs de concurrence.

#### Dans `AttaqueDictionnaire.java`
*   `java.io.BufferedReader`, `java.io.FileReader`, `java.io.IOException`: Utilisés pour lire efficacement le fichier `dictionnaire.txt` ligne par ligne.
*   `java.util.ArrayList`, `java.util.List`: Pour stocker en mémoire la liste des mots de passe lus depuis le dictionnaire.

#### Dans `CibleEnLigne.java`
*   `java.io.*`: `BufferedReader`, `InputStreamReader`, `OutputStream` sont utilisés pour lire la réponse du serveur et envoyer les données du formulaire.
*   `java.net.*`: C'est le cœur de la fonctionnalité de cette classe.
    *   `HttpURLConnection`: Pour établir et gérer la connexion HTTP.
    *   `URL`: Pour représenter l'URL de la cible.
    *   `URLEncoder`: Essentiel pour formater correctement les données (`login` et `password`) afin qu'elles puissent être envoyées dans le corps d'une requête POST.
*   `java.nio.charset.StandardCharsets`: Pour garantir que l'encodage des caractères (UTF-8) est cohérent lors de l'envoi et de la réception des données HTTP.

## Compilation et Exécution

Assurez-vous d'être à la racine du projet.

#### 1. Compiler le projet
```bash
javac -d src -cp src src/passwordcracker/*.java
```

#### 2. Exécuter les 4 variantes

*   **1. Attaque par Force Brute sur Cible Locale**
    *(Cherche un mot de passe de 7 caractères pour l'utilisateur 'admin')*
    ```bash
    java -cp src passwordcracker.ApplicationCraqueur --type bruteforce --target local --login admin --length 7
    ```

*   **2. Attaque par Force Brute sur Cible en Ligne**
    ```bash
    java -cp src passwordcracker.ApplicationCraqueur --type bruteforce --target en_ligne --login admin --url "http://localhost/ProjetPC/login.php" --length 4
    ```

*   **3. Attaque par Dictionnaire sur Cible Locale**
    ```bash
    java -cp src passwordcracker.ApplicationCraqueur --type dictionnaire --target local --login admin
    ```

*   **4. Attaque par Dictionnaire sur Cible en Ligne**
    ```bash
    java -cp src passwordcracker.ApplicationCraqueur --type dictionnaire --target en_ligne --login admin --url "http://localhost/ProjetPC/login.php"
    ```

## Pistes d'amélioration

Le projet actuel est une base solide, mais plusieurs améliorations pourraient être envisagées pour le rendre encore plus puissant et réaliste.

*   **Attaques Hybrides :** Créer une nouvelle stratégie `AttaqueHybride` qui utilise d'abord un dictionnaire, puis enchaîne avec une attaque par force brute sur les mots de passe du dictionnaire en y ajoutant des chiffres ou des symboles (ex: `password` -> `password123`, `password!`).

*   **Gestion Avancée du Multithreading :** Permettre à l'utilisateur de spécifier le nombre de threads à utiliser via un argument (`--threads 8`). Pour l'attaque par dictionnaire, on pourrait également la paralléliser en divisant le fichier dictionnaire en plusieurs segments, chaque thread testant une partie de la liste.

*   **Support de Cibles Supplémentaires :** Ajouter de nouvelles implémentations de `Cible` pour d'autres protocoles, comme `CibleFTP`, `CibleSSH` ou `CibleBaseDeDonnees`. Grâce à l'architecture actuelle, cela ne nécessiterait aucune modification des stratégies d'attaque existantes.

*   **Rapports et Sauvegarde de Session :** Ajouter une option pour sauvegarder la progression d'une attaque par force brute dans un fichier, afin de pouvoir la reprendre plus tard. On pourrait également générer un rapport à la fin de l'attaque (temps écoulé, mot de passe trouvé, vitesse moyenne).

*   **Interface Graphique (GUI) :** Envelopper la logique du projet dans une interface graphique simple (avec Swing ou JavaFX) pour une utilisation plus conviviale.

## Diagramme d'Architecture

L'architecture suit le patron de **Fabrique Abstraite**. Le diagramme ci-dessous illustre les relations entre les composants clés.

```
+---------------------------------------------------------+           +--------------------------------------------+
|           <<interface>>                                 |           |           <<interface>>                    |
|         StrategieAttaque                                |<----------|             Cible                          |
|---------------------------------------------------------|           |--------------------------------------------|
| + craquerMotDePasse(nomUtilisateur, motDePasseACraquer) |           | + authentifier(nomUtilisateur, motDePasse) |
+---------------------------------------------------------+           +--------------------------------------------+
                 ^                                                                  ^
                 |                                                                  |
                 | (implémente)                                                     | (implémente)
                 |                                                                  |
+---------------------------------+                                     +---------------------------------+
|         AttaqueBruteForce       |                                     |           CibleLocale           |
|---------------------------------|                                     |---------------------------------|
| + craquerMotDePasse()           |                                     | + authentifier()                |
+---------------------------------+                                     +---------------------------------+
                 ^                                                                  ^
                 |                                                                  |
                 | (implémente)                                                     | (implémente)
                 |                                                                  |
+---------------------------------+                                     +---------------------------------+
|         AttaqueDictionnaire     |                                     |           CibleEnLigne          |
|---------------------------------|                                     |---------------------------------|
| + craquerMotDePasse()           |                                     | + authentifier()                |
+---------------------------------+                                     +---------------------------------+


+---------------------------------------------------------------------------------+
|                           <<interface>>                                         |
|                             FabriqueCraqueur                                    |
|---------------------------------------------------------------------------------|
| + creerStrategieAttaque(): StrategieAttaque                                     |
| + creerCible(): Cible                                                           |
+---------------------------------------------------------------------------------+
                 ^
                 |
                 | (implémente)
                 |
+---------------------------------------------------------------------------------+
|                       FabriqueBruteForceLocale                                  |
|---------------------------------------------------------------------------------|
| + creerStrategieAttaque(): AttaqueBruteForce                                    |
| + creerCible(): CibleLocale                                                     |
+---------------------------------------------------------------------------------+
                 ^
                 |
                 | (implémente)
                 |
+---------------------------------------------------------------------------------+
|                       FabriqueDictionnaireEnLigne                               |
|---------------------------------------------------------------------------------|
| + creerStrategieAttaque(): AttaqueDictionnaire                                  |
| + creerCible(): CibleEnLigne                                                    |
+---------------------------------------------------------------------------------+


+---------------------------------------+
|         ApplicationCraqueur           |
|---------------------------------------|
| - main(args: String[])                |
|---------------------------------------|
| + utilise(fabrique: FabriqueCraqueur) |
+---------------------------------------+
                 |
                 | (utilise/dépend de)
                 | 
                 +---------------------------------+
                 |         FabriqueCraqueur        | (via l'interface)
                 +---------------------------------+
```