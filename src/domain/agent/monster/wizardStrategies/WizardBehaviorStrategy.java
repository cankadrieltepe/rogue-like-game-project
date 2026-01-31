package domain.agent.monster.wizardStrategies;

import domain.agent.monster.Wizard;

import java.io.Serializable;

/**
 * Requires: The game must have a valid dungeon with an active timer and a valid current hall.
 * Modifies: This method modifies the current behavior of the wizard and makes it such that the wizard may  teleport the player or move the rune or stay idle and disappear.
 * Effects:
 * - Chooses the behavior based on the remaining current game time:
 *   - If time remaining is less than 30%, it teleports the player and makes the wizard disappear.
 *   - If time remaining is more than 70%, it moves the rune every 3 seconds.
 *   - If time remaining is between 30% and 70%, it makes the wizard stay idle and disappear after 2 seconds.
 */
public interface WizardBehaviorStrategy {
    void execute(Wizard wizard);

}
