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

import org.terasology.inGameHelp.ui.DefaultTitleParagraphStyle;
import org.terasology.rendering.nui.widgets.browser.data.ParagraphData;
import org.terasology.rendering.nui.widgets.browser.data.basic.HTMLLikeParser;
import org.terasology.rendering.nui.widgets.browser.data.html.HTMLDocument;

/**
 * Interface that defines the composition of a help item.
 * This is composed of a title, category, and paragraphs of the description of this help item.
 */
public interface HelpItem {

    /**
     * @return the title of this HelpItem.
     */
    String getTitle();

    /**
     * @return the category of this HelpItem.
     */
    String getCategory();

    /**
     * @return the description of this HelpItem.
     */
    Iterable<ParagraphData> getParagraphs();

    /**
     * Adds help information to the document.
     *
     * @param documentdata the document that is modified by adding help information.
     */
    default void addHelpItemSection(HTMLDocument documentData) {
        documentData.addParagraph(HTMLLikeParser.parseHTMLLikeParagraph(new DefaultTitleParagraphStyle(), getTitle()));
        for (ParagraphData paragraph : getParagraphs()) {
            documentData.addParagraph(paragraph);
        }
    }
}
