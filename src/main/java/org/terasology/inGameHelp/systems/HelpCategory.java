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

import org.terasology.inGameHelp.ItemsCategoryInGameHelpRegistry;
import org.terasology.rendering.nui.widgets.browser.data.DocumentData;

/**
 * Interface that manages how a specific help tab will function.
 */
public interface HelpCategory {

    /**
     * Sets the registry.
     *
     * @param registry references an items category in the in game help registry.
     */
    void setRegistry(ItemsCategoryInGameHelpRegistry registry);

    /**
     *  @return the name of the category.
     */
    String getCategoryName();

    /**
     * @return the document data for the particular category.
     */
    DocumentData getDocumentData();

    /**
      * Handles navigation between documents.
      *
      * @param hyperlink the link to the document.
      * @return True if the document that was linked exists. False if otherwise.
      */
    boolean handleNavigate(String hyperlink);

    /**
     * Resets the document, which takes the user back to the main help page.
     */
    void resetNavigation();
}
