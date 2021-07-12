// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.inGameHelp.components;

import com.google.common.collect.Lists;
import org.terasology.engine.rendering.nui.widgets.browser.data.ParagraphData;
import org.terasology.engine.rendering.nui.widgets.browser.data.basic.HTMLLikeParser;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.inGameHelpAPI.components.HelpItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Use this to add a subsection of help to the general category.
 */
public class GeneralHelpComponent implements Component<GeneralHelpComponent>, HelpItem {
    /** Title of this component. */
    public String title;

    /** Name of this help category. */
    public String category = "General";

    /** Description of this help item. */
    public List<String> paragraphText = new ArrayList<>();

    /**
     * @return the title of this help item.
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * @return the category of this help item.
     */
    @Override
    public String getCategory() {
        return category;
    }

    /**
     * Gets the description of this help item.
     *
     * @return an iterable of paragraph data that contains the description of this help item.
     */
    @Override
    public Iterable<ParagraphData> getParagraphs() {
        List<ParagraphData> result = Lists.newLinkedList();
        for (String paragraph : paragraphText) {
            result.add(HTMLLikeParser.parseHTMLLikeParagraph(null, paragraph));
        }
        return result;
    }

    @Override
    public void copy(GeneralHelpComponent other) {
        this.title = other.title;
        this.category = other.category;
        this.paragraphText.clear();
        this.paragraphText.addAll(other.paragraphText);
    }
}
