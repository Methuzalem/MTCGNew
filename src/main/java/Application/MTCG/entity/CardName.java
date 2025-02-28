package Application.MTCG.entity;

public enum CardName {
    WaterGoblin("WaterGoblin"),
    FireGoblin("FireGoblin"),
    RegularGoblin("RegularGoblin"),
    WaterDragon("WaterDragon"),
    FireDragon("FireDragon"),
    RegularDragon("RegularDragon"),
    WaterWizzard("WaterWizzard"),
    FireWizzard("FireWizzard"),
    RegularWizzard("RegularWizzard"),
    WaterOrk("WaterOrk"),
    FireOrk("FireOrk"),
    RegularOrk("RegularOrk"),
    WaterKnight("WaterKnight"),
    FireKnight("FireKnight"),
    RegularKnight("RegularKnight"),
    WaterKraken("WaterKraken"),
    FireKraken("FireKraken"),
    RegularKraken("RegularKraken"),
    WaterElf("WaterElf"),
    FireElf("FireElf"),
    RegularElf("RegularElf"),
    WaterSpell("WaterSpell"),
    FireSpell("FireSpell"),
    RegularSpell("RegularSpell"),
    Dragon("Dragon"),
    Ork("Ork"),
    Knight("Knight"),
    Kraken("Kraken"),
    Wizzard("Wizzard");

    private final String name;

    CardName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
