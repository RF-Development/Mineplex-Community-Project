package club.mineplex.core.mineplex.champions.shop;

public interface IActivatable extends IRechargeable {

    /**
     * @param level The level to query
     * @return The delay, in seconds, that the cooldown should be applied after
     */
    double applyRechargeAfter(final int level);

}
