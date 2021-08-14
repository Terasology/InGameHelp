// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.inGameHelp.components;

import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Component for when the user has been helped.
 * This component is added to the entity whenever the user presses the button that displays the InGameHelpScreen, so that another screen doesn't display if that particular user is currently being helped.
 */
public class HasBeenHelpedComponent implements Component<HasBeenHelpedComponent> {
    @Override
    public void copyFrom(HasBeenHelpedComponent other) {

    }
}
