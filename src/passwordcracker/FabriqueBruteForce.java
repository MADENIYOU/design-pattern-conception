package passwordcracker;

public class FabriqueBruteForce implements FabriqueStrategieAttaque {
    private final int minLength;
    private final int maxLength;

    public FabriqueBruteForce(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public StrategieAttaque creerStrategieAttaque() {
        char[] alphabet = genererAlphabet(true, true, true, true);
        return new AttaqueBruteForce(alphabet, minLength, maxLength);
    }

    private static char[] genererAlphabet(boolean minuscules, boolean majuscules, boolean chiffres, boolean symboles) {
        StringBuilder sb = new StringBuilder();
        if (minuscules) for (char c = 'a'; c <= 'z'; c++) sb.append(c);
        if (majuscules) for (char c = 'A'; c <= 'Z'; c++) sb.append(c);
        if (chiffres) for (char c = '0'; c <= '9'; c++) sb.append(c);
        if (symboles) sb.append("!@#$%^&*()-_=+[]{}|;:,.<>?/");
        return sb.toString().toCharArray();
    }
}
