package club.mineplex.core.mineplex.champions.shop;

public interface IChargeable {

    /**
     * @param level The level to query
     * @return The charge percentage at the level specified
     */
    double getChargeAt(final int level);

}
