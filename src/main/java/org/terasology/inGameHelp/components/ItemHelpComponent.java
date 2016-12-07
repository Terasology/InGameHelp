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
package org.terasology.inGameHelp.components;

import com.google.common.collect.Lists;
import org.terasology.entitySystem.Component;
import org.terasology.rendering.nui.widgets.browser.data.ParagraphData;
import org.terasology.rendering.nui.widgets.browser.data.basic.HTMLLikeParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Use this to add a subsection of help to an item.
 */
public class ItemHelpComponent implements Component, HelpItem {
    public String title = "About this item";                // Change this to display something else as the title.
    public String category = "Items";                       // What help category to place this item in.
    public List<String> paragraphText = new ArrayList<>();  // The description lines of this item.

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
     * @return an iterable of paragraph data about this help item that contains the description.
     */
    @Override
    public Iterable<ParagraphData> getParagraphs() {
        List<ParagraphData> result = Lists.newLinkedList();
        for (String paragraph : paragraphText) {
            result.add(HTMLLikeParser.parseHTMLLikeParagraph(null, paragraph));
        }
        return result;
    }
}
