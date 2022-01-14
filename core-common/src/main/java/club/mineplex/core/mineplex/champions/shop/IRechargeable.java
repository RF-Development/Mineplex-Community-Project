package club.mineplex.core.mineplex.champions.shop;

public interface IRechargeable {

    /**
     * @param level The level to query
     * @return The cooldown, in seconds, at the level specified
     */
    double getRecharge(final int level);

}
