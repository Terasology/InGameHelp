// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.inGameHelp.systems;

import org.terasology.engine.core.SimpleUri;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.input.InputSystem;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.logic.players.event.LocalPlayerInitializedEvent;
import org.terasology.engine.network.ClientComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.registry.Share;
import org.terasology.engine.rendering.nui.NUIManager;
import org.terasology.engine.unicode.EnclosedAlphanumerics;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;
import org.terasology.inGameHelp.InGameHelpClient;
import org.terasology.inGameHelp.components.HasBeenHelpedComponent;
import org.terasology.inGameHelp.ui.InGameHelpButton;
import org.terasology.inGameHelp.ui.InGameHelpScreen;
import org.terasology.input.ButtonState;
import org.terasology.input.Input;
import org.terasology.notifications.events.ExpireNotificationEvent;
import org.terasology.notifications.events.ShowNotificationEvent;
import org.terasology.notifications.model.Notification;
import org.terasology.nui.Color;
import org.terasology.nui.FontColor;

/**
 * Class that handles button events and displays the help screen to the client.
 */
@RegisterSystem(RegisterMode.CLIENT)
@Share(InGameHelpClient.class)
public class InGameHelpClientSystem extends BaseComponentSystem implements InGameHelpClient {

    private static final String NOTIFICATION_ID = "InGameHelp:firstTime";

    /**
     * Reference to the {@link org.terasology.engine.rendering.nui.NUIManager}, used for adding user interace elements and
     * displaying the help screen.
     */
    @In
    NUIManager nuiManager;
    @In
    InputSystem inputSystem;
    @In
    LocalPlayer localPlayer;

    /**
     * Initialises the system. Adds an UnHelpedNagWidget to the heads up display.
     */
    @Override
    public void initialise() {
        super.initialise();
        nuiManager.getHUD().addHUDElement("InGameHelp:UnHelpedNagWidget");
    }

    /**
     * Get a formatted representation of the primary {@link Input} associated with the given button binding.
     *
     * If the display name of the primary bound key is a single character this representation will be the encircled
     * character. Otherwise the full display name is used. The bound key will be printed in yellow.
     *
     * If no key binding was found the text "n/a" in red color is returned.
     *
     * @param button the URI of a bindable button
     * @return a formatted text to be used as representation for the player
     */
    //TODO: put this in a common place? Duplicated in Dialogs and EventualSkills
    private String getActivationKey(SimpleUri button) {
        return inputSystem.getInputsForBindButton(button).stream()
                .findFirst()
                .map(Input::getDisplayName)
                .map(key -> {
                    if (key.length() == 1) {
                        // print the key in yellow within a circle
                        int off = key.charAt(0) - 'A';
                        char code = (char) (EnclosedAlphanumerics.CIRCLED_LATIN_CAPITAL_LETTER_A + off);
                        return String.valueOf(code);
                    } else {
                        return key;
                    }
                })
                .map(key -> FontColor.getColored(key, Color.yellow))
                .orElse(FontColor.getColored("n/a", Color.red));
    }

    @ReceiveEvent
    public void onLocalPlayerInitialized(LocalPlayerInitializedEvent event, EntityRef entity) {
        if (!localPlayer.getCharacterEntity().hasComponent(HasBeenHelpedComponent.class)) {
            Notification notification = new Notification(NOTIFICATION_ID,
                    "Where's the Manual?",
                    "Press " + getActivationKey(new SimpleUri("InGameHelp:inGameHelp")) + " for in-game help",
                    "engine:items#blueBook");
            localPlayer.getClientEntity().send(new ShowNotificationEvent(notification));
        }
    }

    /**
     * Handles the button event and displays the InGameHelpScreen when the button has been clicked.
     *
     * @param event the help button event.
     * @param entity the entity to display the help screen to.
     */
    @ReceiveEvent(components = ClientComponent.class)
    public void onInGameHelpButton(InGameHelpButton event, EntityRef entity) {
        if (event.getState() == ButtonState.DOWN) {
            entity.send(new ExpireNotificationEvent(NOTIFICATION_ID));

            EntityRef targetEntity = localPlayer.getCharacterEntity();
            if (!targetEntity.hasComponent(HasBeenHelpedComponent.class)) {
                targetEntity.addComponent(new HasBeenHelpedComponent());
            }

            nuiManager.toggleScreen("InGameHelp:InGameHelpScreen");
            event.consume();
        }
    }

    /**
     * Displays the help screen for the document referenced by hyperlink.
     *
     * @param hyperlink the link to the document.
     */
    @Override
    public void showHelpForHyperlink(String hyperlink) {
        InGameHelpScreen screen = (InGameHelpScreen) nuiManager.getScreen("InGameHelp:InGameHelpScreen");
        if (screen == null) {
            screen = (InGameHelpScreen) nuiManager.pushScreen("InGameHelp:InGameHelpScreen");
        }
        screen.navigateTo(hyperlink);
        if (!nuiManager.isOpen("InGameHelp:InGameHelpScreen")) {
            nuiManager.toggleScreen("InGameHelp:InGameHelpScreen");
        }
    }
}
