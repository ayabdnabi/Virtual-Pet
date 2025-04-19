package src;


/**
 * The ParentalControl class manages restrictions and tracking features for a parent or guardian
 * in the virtual pet simulation game. It allows for time-based play limitations and logs gameplay duration.
 *
 * <p>Main responsibilities include:</p>
 * <ul>
 *   <li>Authenticating access with a preset parent password</li>
 *   <li>Defining allowed play hours within a 24-hour time window</li>
 *   <li>Tracking and resetting total play time and session counts</li>
 *   <li>Providing functionality to revive a pet from a dead state</li>
 * </ul>
 *
 * @author Kamaldeep Ghotra
 * @version 1.0
 */
public class ParentalControl {
    /** Parental control password (set to 1234) */
    private static final String PARENT_PASSWORD = "1234";

    /** Flag that determines if parental time restrictions are currently active */
    private boolean limitationsEnabled;

    /** The allowed start hour of the user (24 hr format) */
    private int allowedStartHour;
    /** The allowed end hour fo the user (24hr format) */
    private int allowedEndHour;
    /** Tracking the total play time for avg time */
    private long totalPlayTime = 0;
    /** Tracking the sessions count for avg time */
    private int sessionCount = 0;


    /**
     * Checks if the provided input matches the parent password.
     *
     * @param input The password entered by the user.
     * @return true if the input matches the stored parent password; false otherwise.
     */
    public boolean authenticate(String input) {
        return PARENT_PASSWORD.equals(input);
    }


    /**
     * Checks whether gameplay is currently allowed based on the parental time restrictions.
     *
     * @return true if play is allowed.
     */
    public boolean isPlayAllowedNow() {
        // If time based limitations are disabled, allow play at any time
        if (!limitationsEnabled) {
            return true;
        }

        // Get the current hour
        int currentHour = java.time.LocalTime.now().getHour();

        // Return true if the current hour is within the allowed play window.
        return currentHour >= allowedStartHour && currentHour < allowedEndHour;
    }


    /**
     * Sets the daily playtime window during which gameplay is allowed.
     *
     * @param startHour The starting hour (inclusive) in 24-hour format (e.g., 9 for 9 AM).
     * @param endHour   The ending hour (exclusive) in 24-hour format (e.g., 21 for 9 PM).
     */
    public void setPlayTimeWindow(int startHour, int endHour) {
        this.allowedStartHour = startHour;
        this.allowedEndHour = endHour;
    }


    /**
     * Enables or disables parental playtime limitations.
     *
     * @param enabled If true, playtime restrictions are enforced; if false, the player can play at any time.
     */
    public void setLimitationsEnabled(boolean enabled) {
        this.limitationsEnabled = enabled;
    }



    /**
     * Calculates and returns the average playtime per session.
     *
     * @return The average session duration in milliseconds, or 0 if no sessions have been recorded.
     */
    public long getAveragePlayTime() {
        return sessionCount == 0 ? 0 : totalPlayTime / sessionCount;
    }


    /**
     * Returns the total accumulated playtime across all sessions.
     *
     * @return The total playtime in milliseconds.
     */
    public long getTotalPlayTime() {
        return totalPlayTime;
    }


    /**
     * Updates the total playtime and session count after a game session ends.
     *
     * @param sessionDuration The length of the session in milliseconds.
     */
    public void updateAfterSession(long sessionDuration) {
        totalPlayTime += sessionDuration;
        sessionCount++;
    }


    /**
     * Resets the parental statistics by clearing total playtime
     * and setting the session count back to zero.
     */
    public void resetStats() {
        totalPlayTime = 0;
        sessionCount = 0;
    }


    /**
     * Attempts to revive a pet if it is currently dead.
     * If the pet is dead, it resets the pet's state and returns true.
     * Otherwise, the method returns false and does nothing.
     *
     * @param pet The pet to revive.
     * @return true if the pet was revived; false if the pet was not dead.
     */
    public boolean revivePet(Pet pet) {
        // If it is dead, reset all of its stats and flags
        if (pet.isDead()) {
            pet.resetState();
            return true;  // Return true
        } else {
            // If the pet is not dead, return false
            return false;
        }
    }


    /**
     * Resets all playtime limitations by disabling the restriction toggle
     * and setting the allowed play window to cover the full 24-hour day.
     * This allows the game to be played at any time.
     */
    public void resetPlayTimeRestrictions() {
        this.limitationsEnabled = false;
        this.allowedStartHour = 0;
        this.allowedEndHour = 24;
    }


    /**
     * Checks whether parental playtime limitations are currently enabled.
     *
     * @return true if playtime restrictions are active; false otherwise.
     */
    public boolean isLimitationsEnabled() {
        return limitationsEnabled;
    }


    /**
     * Gets the starting hour of the allowed playtime window.
     * This is used when parental limitations are enabled.
     *
     * @return the allowed starting hour in 24-hour format (e.g., 9 for 9 AM).
     */
    public int getAllowedStartHour() {
        return allowedStartHour;
    }


    /**
     * Gets the ending hour of the allowed playtime window.
     * This is used when parental limitations are enabled.
     *
     * @return the allowed ending hour in 24-hour format (e.g., 21 for 9 PM).
     */
    public int getAllowedEndHour() {
        return allowedEndHour;
    }


    /**
     * Returns the total number of game sessions tracked by the parental control system.
     * Each session is counted when a play session ends and its duration is recorded.
     *
     * @return the number of recorded play sessions.
     */
    public int getSessionCount() {
        return sessionCount;
    }
}
