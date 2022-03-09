package club.mineplex.core.mineplex.clans.runes;

import club.mineplex.core.mineplex.clans.ClansRune;

public abstract class RuneFactory {

    private final ClansRune[] skills;

    protected RuneFactory() {
        this.skills = this.createRunes();
    }

    protected abstract ClansRune[] createRunes();

    public ClansRune[] getRunes() {
        return this.skills;
    }

}
