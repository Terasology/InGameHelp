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

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.terasology.inGameHelpAPI.ItemsCategoryInGameHelpRegistry;
import org.terasology.inGameHelpAPI.components.HelpItem;
import org.terasology.inGameHelpAPI.systems.HelpCategory;
import org.terasology.utilities.Assets;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.inGameHelpAPI.components.ItemHelpComponent;
import org.terasology.inGameHelp.ui.ItemWidget;
import org.terasology.inGameHelp.ui.WidgetFlowRenderable;
import org.terasology.logic.common.DisplayNameComponent;
import org.terasology.rendering.assets.font.Font;
import org.terasology.rendering.nui.widgets.browser.data.DocumentData;
import org.terasology.rendering.nui.widgets.browser.data.basic.FlowParagraphData;
import org.terasology.rendering.nui.widgets.browser.data.basic.flow.TextFlowRenderable;
import org.terasology.rendering.nui.widgets.browser.data.html.HTMLDocument;
import org.terasology.rendering.nui.widgets.browser.ui.style.ContainerInteger;
import org.terasology.rendering.nui.widgets.browser.ui.style.FixedContainerInteger;
import org.terasology.rendering.nui.widgets.browser.ui.style.ParagraphRenderStyle;
import org.terasology.rendering.nui.widgets.browser.ui.style.TextRenderStyle;

import java.util.Map;

/**
 * Help category that manages the Items tab.
 */
public class ItemsCategory implements HelpCategory {
    
    /** Name of this category */
    private final String name = "Items";

    /** Reference to the {@link org.terasology.inGameHelpAPI.ItemsCategoryInGameHelpRegistry} that determines which prefabs are associated with this. */
    private final ItemsCategoryInGameHelpRegistry itemsCategoryInGameHelpRegistry;

    /** Map of prefab names to help documents that are associated with the prefabs. */
    Map<String, DocumentData> items = Maps.newHashMap();

    /** The root HTML document. */
    HTMLDocument rootDocument;

    /** The current document data. */
    DocumentData currentDocument;

    /**
     * Constructor for this help category.
     *
     * @param itemsCategoryInGameHelpRegistry the items category help registry.
     */
    public ItemsCategory(ItemsCategoryInGameHelpRegistry itemsCategoryInGameHelpRegistry) {
        this.itemsCategoryInGameHelpRegistry = itemsCategoryInGameHelpRegistry;
    }

    public void setRegistry(ItemsCategoryInGameHelpRegistry registry) {

    }

    /**
     * Initialises the help category. Sets the font and padding. Creates the documents then adds help item information to the documents.
     */
    private void initialise() {
        TextRenderStyle titleRenderStyle = new TextRenderStyle() {
            /**
             * @return the title font
             */
            @Override
            public Font getFont(boolean hyperlink) {
                return Assets.getFont("engine:title").get();
            }
        };
        ParagraphRenderStyle titleParagraphStyle = new ParagraphRenderStyle() {
            /**
             * @return a fixed container integer of size 5
             */
            @Override
            public ContainerInteger getParagraphPaddingTop() {
                return new FixedContainerInteger(5);
            }
        };

        //Create the root document and add the item list paragraph that contains the items
        rootDocument = new HTMLDocument(null);
        FlowParagraphData itemListParagraph = new FlowParagraphData(null);
        rootDocument.addParagraph(itemListParagraph);
        
        //add all applicable prefabs to the document
        for (Prefab itemPrefab : itemsCategoryInGameHelpRegistry.getKnownPrefabs()) {
            HTMLDocument documentData = new HTMLDocument(null);
            ItemHelpComponent helpComponent = itemPrefab.getComponent(ItemHelpComponent.class);
            if (helpComponent == null) {
                helpComponent = new ItemHelpComponent();
                helpComponent.paragraphText.add("An unknown item.");
            }

            if (getCategoryName().equalsIgnoreCase(helpComponent.getCategory())) {
                FlowParagraphData imageNameParagraph = new FlowParagraphData(null);
                documentData.addParagraph(imageNameParagraph);
                imageNameParagraph.append(new WidgetFlowRenderable(new ItemWidget(itemPrefab.getName()), 48, 48, itemPrefab.getName()));
                DisplayNameComponent displayNameComponent = itemPrefab.getComponent(DisplayNameComponent.class);
                if (displayNameComponent != null) {
                    imageNameParagraph.append(new TextFlowRenderable(displayNameComponent.name, titleRenderStyle, null));
                } else {
                    imageNameParagraph.append(new TextFlowRenderable(itemPrefab.getName(), titleRenderStyle, null));
                }

                helpComponent.addHelpItemSection(documentData);

                // add all the other ones from components
                for (HelpItem helpItem : Iterables.filter(itemPrefab.iterateComponents(), HelpItem.class)) {
                    if (helpItem != helpComponent) {
                        helpItem.addHelpItemSection(documentData);
                    }
                }

                // add all the other ones from code registered HelpItems
                for (HelpItem helpItem : itemsCategoryInGameHelpRegistry.getHelpItems(itemPrefab)) {
                    helpItem.addHelpItemSection(documentData);
                }

                items.put(itemPrefab.getName(), documentData);

                // add this to the root document
                itemListParagraph.append(new WidgetFlowRenderable(new ItemWidget(itemPrefab.getName()), 48, 48, itemPrefab.getName()));
            }
        }
    }

    /**
     * Gets the name for this category.
     *
     * @return the name of this category.
     */
    @Override
    public String getCategoryName() {
        return name;
    }

    /**
     * Gets the document data.
     *
     * @return the current document data or the root document if the current document is null.
     */
    @Override
    public DocumentData getDocumentData() {
        initialise();
        if (currentDocument == null) {
            return rootDocument;
        } else {
            return currentDocument;
        }
    }

    /**
     * Goes to the root document.
     */
    @Override
    public void resetNavigation() {
        currentDocument = null;
    }

    /**
     * Goes to the document referenced by hyperlink.
     *
     * @param hyperlink the link to the document.
     * @return true if the link is found. false if otherwise.
     */
    @Override
    public boolean handleNavigate(String hyperlink) {
        if (items.size() == 0) {
            // handle the case where we navigate before we have shown the screen.  There is probably a better way to do this.
            initialise();
        }

        //goes to document referenced by hyperlink if it is found
        if (items.containsKey(hyperlink)) {
            currentDocument = items.get(hyperlink);
            return true;
        } else {
            return false;
        }
    }
}
