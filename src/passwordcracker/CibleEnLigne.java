package passwordcracker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CibleEnLigne implements Cible {
    private final String urlCible;

    public CibleEnLigne(String urlCible) {
        this.urlCible = urlCible;
    }

    @Override
    public boolean authentifier(String nomUtilisateur, String motDePasse) {
        try {
            String postData = String.format("login=%s&password=%s",
                    URLEncoder.encode(nomUtilisateur, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(motDePasse, StandardCharsets.UTF_8.name()));

            URL url = new URI(urlCible).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = postData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String response = br.readLine();
                    return response != null && response.contains("Connexion réussie");
                }
            }

            return false;

        } catch (Exception e) {
            System.out.println("Erreur lors de l'attaque en ligne : " + e.getMessage());
            return false;
        }
    }
}
