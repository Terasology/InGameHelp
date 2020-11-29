// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.inGameHelp.helpCategories;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.inGameHelpAPI.ItemsCategoryInGameHelpRegistry;
import org.terasology.inGameHelpAPI.components.ItemHelpComponent;
import org.terasology.inGameHelpAPI.systems.HelpCategory;
import org.terasology.inGameHelpAPI.ui.ItemWidget;
import org.terasology.inGameHelpAPI.ui.WidgetFlowRenderable;
import org.terasology.logic.common.DisplayNameComponent;
import org.terasology.rendering.assets.font.Font;
import org.terasology.rendering.nui.widgets.browser.data.DocumentData;
import org.terasology.rendering.nui.widgets.browser.data.ParagraphData;
import org.terasology.rendering.nui.widgets.browser.data.basic.FlowParagraphData;
import org.terasology.rendering.nui.widgets.browser.data.basic.flow.TextFlowRenderable;
import org.terasology.rendering.nui.widgets.browser.data.html.HTMLDocument;
import org.terasology.rendering.nui.widgets.browser.ui.style.TextRenderStyle;
import org.terasology.utilities.Assets;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Help category that manages the Items tab.
 */
public class ItemsCategory implements HelpCategory {

    /**
     * Name of this category
     */
    private final String name = "Items";

    /**
     * Reference to the {@link org.terasology.inGameHelpAPI.ItemsCategoryInGameHelpRegistry} that determines which
     * prefabs are associated with this.
     */
    private final ItemsCategoryInGameHelpRegistry itemsCategoryInGameHelpRegistry;

    /**
     * Sorted list of item help entries with hyperlink and document data attached.
     */
    private final List<ItemHelpEntry> items = Lists.newArrayList();

    /**
     * The root HTML document.
     */
    private HTMLDocument rootDocument;

    /**
     * The current document data.
     */
    private DocumentData currentDocument;

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
     * Initialises the help category. Sets the font and padding. Creates the documents then adds help item information
     * to the documents.
     */
    private void initialise() {
        TextRenderStyle titleRenderStyle = new TextRenderStyle() {
            /**
             * @return the title font
             */
            @Override
            public Font getFont(boolean hyperlink) {
                return Assets.getFont("engine:NotoSans-Regular-Title").get();
            }
        };

        //Create the root document and add the item list paragraph that contains the items
        rootDocument = new HTMLDocument(null);
        FlowParagraphData itemListParagraph = new FlowParagraphData(null);
        rootDocument.addParagraph(itemListParagraph);

        final Map<String, String> displayNames = new HashMap<>();

        //add all applicable prefabs to the document
        for (Prefab itemPrefab : itemsCategoryInGameHelpRegistry.getKnownPrefabs()) {

            ItemHelpComponent helpComponent = itemPrefab.getComponent(ItemHelpComponent.class);
            if (helpComponent == null) {
                helpComponent = new ItemHelpComponent();
                helpComponent.paragraphText.add("An unknown item.");
            }

            if (getCategoryName().equalsIgnoreCase(helpComponent.getCategory())) {

                String displayName =
                        Optional.ofNullable(itemPrefab.getComponent(DisplayNameComponent.class))
                                .map(c -> c.name)
                                .orElse(itemPrefab.getName());
                displayNames.put(displayName, itemPrefab.getName());

                HTMLDocument documentData = new HTMLDocument(null);

                documentData.addParagraph(getImageNameParagraph(itemPrefab.getName(), displayName, titleRenderStyle));
                documentData.addParagraphs(helpComponent.getHelpSection());

                // add all the other ones from components
                for (org.terasology.inGameHelpAPI.components.HelpItem helpItem :
                        Iterables.filter(itemPrefab.iterateComponents(),
                                org.terasology.inGameHelpAPI.components.HelpItem.class)) {
                    if (helpItem != helpComponent) {
                        documentData.addParagraphs(helpItem.getHelpSection());
                    }
                }

                // add all the other ones from code registered HelpItems
                for (org.terasology.inGameHelpAPI.components.HelpItem helpItem :
                        itemsCategoryInGameHelpRegistry.getHelpItems(itemPrefab)) {
                    documentData.addParagraphs(helpItem.getHelpSection());
                }

                items.add(new ItemHelpEntry(itemPrefab.getName(), displayName, documentData));
            }
        }
        items.stream()
                .sorted(Comparator.comparing(ItemHelpEntry::getDisplayName))
                .map(ItemHelpEntry::getHyperlink)
                .forEach(hyperlink ->
                        itemListParagraph.append(
                                new WidgetFlowRenderable(new ItemWidget(hyperlink), 48, 48, hyperlink)));
    }

    private ParagraphData getImageNameParagraph(String prefabName, String displayName, TextRenderStyle renderStyle) {
        FlowParagraphData paragraph = new FlowParagraphData(null);
        paragraph.append(new WidgetFlowRenderable(new ItemWidget(prefabName), 48, 48, prefabName));
        paragraph.append(new TextFlowRenderable(displayName, renderStyle, null));
        return paragraph;
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
            // handle the case where we navigate before we have shown the screen.  There is probably a better way to
            // do this.
            initialise();
        }

        Optional<DocumentData> target =
                items.stream().filter(item -> item.getHyperlink().equals(hyperlink)).findFirst().map(ItemHelpEntry::getDocumentData);

        //goes to document referenced by hyperlink if it is found
        if (target.isPresent()) {
            currentDocument = target.get();
            return true;
        } else {
            return false;
        }
    }
}
