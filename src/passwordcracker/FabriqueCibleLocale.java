package passwordcracker;

public class FabriqueCibleLocale implements FabriqueCible {
    @Override
    public Cible creerCible() {
        return new CibleLocale();
    }
}
