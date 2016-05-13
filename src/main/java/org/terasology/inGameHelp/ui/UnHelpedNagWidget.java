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
package org.terasology.inGameHelp.ui;

import com.google.common.base.Strings;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.inGameHelp.components.HasBeenHelpedComponent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.databinding.ReadOnlyBinding;
import org.terasology.rendering.nui.layers.hud.CoreHudWidget;
import org.terasology.rendering.nui.widgets.UILabel;

public class UnHelpedNagWidget extends CoreHudWidget {
    UILabel message;

    @Override
    public void initialise() {
        message = find("message", UILabel.class);
        if (message != null) {
            message.bindText(new ReadOnlyBinding<String>() {
                @Override
                public String get() {
                    EntityRef targetEntity = CoreRegistry.get(LocalPlayer.class).getCharacterEntity();
                    HasBeenHelpedComponent hasBeenHelpedComponent = targetEntity.getComponent(HasBeenHelpedComponent.class);
                    if (hasBeenHelpedComponent == null) {
                        return "Press 'P' for in game help";
                    }
                    return null;
                }

            });
        }
        bindVisible(new ReadOnlyBinding<Boolean>() {
            @Override
            public Boolean get() {
                if (message != null) {
                    return !Strings.isNullOrEmpty(message.getText());
                } else {
                    return false;
                }
            }
        });
    }
}
