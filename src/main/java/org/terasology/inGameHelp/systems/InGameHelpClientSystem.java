// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.inGameHelp.systems;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.network.ClientComponent;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.registry.In;
import org.terasology.engine.registry.Share;
import org.terasology.engine.rendering.nui.NUIManager;
import org.terasology.inGameHelp.InGameHelpClient;
import org.terasology.inGameHelp.components.HasBeenHelpedComponent;
import org.terasology.inGameHelp.ui.InGameHelpButton;
import org.terasology.inGameHelp.ui.InGameHelpScreen;
import org.terasology.nui.input.ButtonState;

/**
 * Class that handles button events and displays the help screen to the client.
 */
@RegisterSystem(RegisterMode.CLIENT)
@Share(InGameHelpClient.class)
public class InGameHelpClientSystem extends BaseComponentSystem implements InGameHelpClient {
    /**
     * Reference to the {@link org.terasology.rendering.nui.NUIManager}, used for adding user interace elements and
     * displaying the help screen.
     */
    @In
    NUIManager nuiManager;

    /**
     * Initialises the system. Adds an UnHelpedNagWidget to the heads up display.
     */
    @Override
    public void initialise() {
        super.initialise();
        nuiManager.getHUD().addHUDElement("InGameHelp:UnHelpedNagWidget");
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

            EntityRef targetEntity = CoreRegistry.get(LocalPlayer.class).getCharacterEntity();
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
