package club.mineplex.core.mineplex.clans.runes.armor;

import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.runes.RuneFactory;

public class ArmorRuneFactory extends RuneFactory {

    private static final ArmorRuneFactory INSTANCE = new ArmorRuneFactory();

    private ArmorRuneFactory() {

    }

    public static ArmorRuneFactory getInstance() {
        return INSTANCE;
    }

    @Override
    protected ClansRune[] createRunes() {
        return new ClansRune[]{
                new ConqueringArmor(),
                new LavaForged(),
                new Padded(),
                new Reinforced(),
                new Slanted()
        };
    }

}
