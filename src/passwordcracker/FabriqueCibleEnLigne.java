package passwordcracker;

public class FabriqueCibleEnLigne implements FabriqueCible {
    private final String url;

    public FabriqueCibleEnLigne(String url) {
        this.url = url;
    }

    @Override
    public Cible creerCible() {
        return new CibleEnLigne(url);
    }
}
