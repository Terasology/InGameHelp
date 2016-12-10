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

import org.terasology.utilities.Assets;
import org.terasology.rendering.assets.font.Font;
import org.terasology.rendering.nui.widgets.browser.ui.style.ContainerInteger;
import org.terasology.rendering.nui.widgets.browser.ui.style.FixedContainerInteger;
import org.terasology.rendering.nui.widgets.browser.ui.style.ParagraphRenderStyle;

/**
 * The text rendering style for titles.
 */
public class DefaultTitleParagraphStyle implements ParagraphRenderStyle {

    /**
     * Gets the title font
     *
     * @param hyperlink this parameter is not used.
     * @return the title font.
     */
    @Override
    public Font getFont(boolean hyperlink) {
        return Assets.getFont("engine:title").get();
    }

    /**
     * Gets the amount of padding on top of the paragraph.
     *
     * @return a container integer of value 10.
     */
    @Override
    public ContainerInteger getParagraphPaddingTop() {
        return new FixedContainerInteger(10);
    }
}
