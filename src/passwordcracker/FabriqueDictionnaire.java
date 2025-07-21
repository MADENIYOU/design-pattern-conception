package passwordcracker;

public class FabriqueDictionnaire implements FabriqueStrategieAttaque {
    private final String cheminDictionnaire;

    public FabriqueDictionnaire(String cheminDictionnaire) {
        this.cheminDictionnaire = cheminDictionnaire;
    }

    @Override
    public StrategieAttaque creerStrategieAttaque() {
        return new AttaqueDictionnaire(cheminDictionnaire);
    }
}
