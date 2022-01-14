package club.mineplex.core.mineplex.champions.shop;

public interface IEnergyConsumer {

    /**
     * @param level The level to query
     * @return The energy to consume
     */
    double getEnergy(final int level);

    /**
     * Saved for abilities that consume energy per second
     */
    interface PerSecond extends IEnergyConsumer {

    }

}
