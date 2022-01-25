package club.mineplex.core.mineplex.champions.shop;

/**
 * Saved for skills that are prepared before use, like Disengage or Riposte
 */
public interface IPreparable {

    /**
     * @param level The level to query
     * @return The time, in seconds, that a skill has to prepare before it fails. Returns -1 for no timeframe.
     */
    double getPrepareExpire(final int level);

    /**
     * @param level The level to query
     * @return The cooldown, in seconds, that a skill applies upon failing to use an ability after preparing. Returns -1
     * for no cooldown.
     */
    default double getFailRecharge(final int level) {
        return -1D;
    }

}
