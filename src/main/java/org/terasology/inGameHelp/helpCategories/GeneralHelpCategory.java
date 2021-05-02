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
package org.terasology.inGameHelp.helpCategories;

import org.terasology.gestalt.assets.ResourceUrn;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.rendering.nui.widgets.browser.data.DocumentData;
import org.terasology.engine.rendering.nui.widgets.browser.data.basic.HTMLLikeParser;
import org.terasology.engine.rendering.nui.widgets.browser.data.html.HTMLDocument;
import org.terasology.engine.utilities.Assets;
import org.terasology.inGameHelp.components.GeneralHelpComponent;
import org.terasology.inGameHelpAPI.ItemsCategoryInGameHelpRegistry;
import org.terasology.inGameHelpAPI.systems.HelpCategory;
import org.terasology.inGameHelpAPI.ui.DefaultTitleParagraphStyle;

import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Help category for the general tab.
 */
public class GeneralHelpCategory implements HelpCategory {

    /**
     * Gets the name of this category.
     *
     * @return the name of this category.
     */
    @Override
    public String getCategoryName() {
        return "General";
    }

    public void setRegistry(ItemsCategoryInGameHelpRegistry registry) {

    }

    /**
     * Gets the general help information then adds it to a document data
     *
     * @return a document with general help information.
     */
    @Override
    public DocumentData getDocumentData() {
        HTMLDocument documentData = new HTMLDocument(null);

        for (GeneralHelpComponent helpComponent : Assets.list(Prefab.class).stream()
                // ensure that this module's help is first
                .sorted(Comparator.comparing(x -> !x.equals(new ResourceUrn("InGameHelp:GeneralHelp"))))
                .map(x -> Assets.get(x, Prefab.class))
                .map(x -> x.get().getComponent(GeneralHelpComponent.class))
                .filter(x -> x != null)
                .collect(Collectors.toList())) {
            documentData.addParagraph(HTMLLikeParser.parseHTMLLikeParagraph(new DefaultTitleParagraphStyle(), helpComponent.title));
            for (String paragraph : helpComponent.paragraphText) {
                documentData.addParagraph(HTMLLikeParser.parseHTMLLikeParagraph(null, paragraph));
            }
        }

        return documentData;
    }

    /**
     * @return false
     */ 
    @Override
    public boolean handleNavigate(String hyperlink) {
        return false;
    }

    @Override
    public void resetNavigation() {
    }
}
