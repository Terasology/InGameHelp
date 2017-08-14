/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.inGameHelp.systems;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.inGameHelp.InGameHelpClient;
import org.terasology.inGameHelp.components.HasBeenHelpedComponent;
import org.terasology.inGameHelp.ui.InGameHelpScreen;
import org.terasology.inGameHelp.ui.InGameHelpButton;
import org.terasology.input.ButtonState;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.network.ClientComponent;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.registry.Share;
import org.terasology.rendering.nui.NUIManager;

/**
 * Class that handles button events and displays the help screen to the client. 
 */
@RegisterSystem(RegisterMode.CLIENT)
@Share(InGameHelpClient.class)
public class InGameHelpClientSystem extends BaseComponentSystem implements InGameHelpClient {
    /** Reference to the {@link org.terasology.rendering.nui.NUIManager}, used for adding user interace elements and displaying the help screen. */ 
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
