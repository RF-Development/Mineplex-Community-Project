package club.mineplex.core.mineplex.clans.runes.bow;

import club.mineplex.core.mineplex.clans.ClansRune;
import club.mineplex.core.mineplex.clans.runes.RuneFactory;

public class BowRuneFactory extends RuneFactory {

    private static final BowRuneFactory INSTANCE = new BowRuneFactory();

    private BowRuneFactory() {

    }

    public static BowRuneFactory getInstance() {
        return INSTANCE;
    }

    @Override
    protected ClansRune[] createRunes() {
        return new ClansRune[] {
                new Heavy(),
                new Hunting(),
                new Inverse(),
                new Leeching(),
                new Recursive(),
                new Scorching(),
                new Recursive()
        };
    }

}
