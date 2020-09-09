// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.inGameHelp.ui;

import org.terasology.engine.input.BindButtonEvent;
import org.terasology.engine.input.DefaultBinding;
import org.terasology.engine.input.RegisterBindButton;
import org.terasology.nui.input.InputType;
import org.terasology.nui.input.Keyboard;

/**
 * Button event for opening the InGameHelp window.
 */
@RegisterBindButton(id = "inGameHelp", description = "Open Help", category = "general")
@DefaultBinding(type = InputType.KEY, id = Keyboard.KeyId.P)
public class InGameHelpButton extends BindButtonEvent {
}
