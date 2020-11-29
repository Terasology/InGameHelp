// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.inGameHelp.helpCategories;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.inGameHelpAPI.ItemsCategoryInGameHelpRegistry;
import org.terasology.inGameHelpAPI.components.HelpItem;
import org.terasology.inGameHelpAPI.components.ItemHelpComponent;
import org.terasology.inGameHelpAPI.systems.HelpCategory;
import org.terasology.inGameHelpAPI.ui.ItemWidget;
import org.terasology.inGameHelpAPI.ui.WidgetFlowRenderable;
import org.terasology.logic.common.DisplayNameComponent;
import org.terasology.rendering.assets.font.Font;
import org.terasology.rendering.nui.widgets.browser.data.DocumentData;
import org.terasology.rendering.nui.widgets.browser.data.ParagraphData;
import org.terasology.rendering.nui.widgets.browser.data.basic.FlowParagraphData;
import org.terasology.rendering.nui.widgets.browser.data.basic.flow.FlowRenderable;
import org.terasology.rendering.nui.widgets.browser.data.basic.flow.TextFlowRenderable;
import org.terasology.rendering.nui.widgets.browser.data.html.HTMLDocument;
import org.terasology.rendering.nui.widgets.browser.ui.style.TextRenderStyle;
import org.terasology.utilities.Assets;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    private List<ItemHelpEntry> items = Lists.newArrayList();

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
        // initialize the rendering style
        TextRenderStyle titleRenderStyle = new TextRenderStyle() {
            /**
             * @return the title font
             */
            @Override
            public Font getFont(boolean hyperlink) {
                return Assets.getFont("engine:NotoSans-Regular-Title").get();
            }
        };

        // initialize item help entries for all applicable prefabs
        items = StreamSupport.stream(itemsCategoryInGameHelpRegistry.getKnownPrefabs().spliterator(), false)
                .map(itemPrefab -> helpEntryFor(itemPrefab, titleRenderStyle))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(ItemHelpEntry::getDisplayName))
                .collect(Collectors.toList());

        // add all item help entries sorted in alphabetical order to the document
        FlowParagraphData itemListParagraph = new FlowParagraphData(null);
        items.stream()
                .map(ItemHelpEntry::getHyperlink)
                .map(this::createItemWidget)
                .forEach(itemListParagraph::append);

        //Create the root document and add the item list paragraph that contains the items
        rootDocument = new HTMLDocument(null);
        rootDocument.addParagraph(itemListParagraph);
    }

    /**
     * Create a renderable widget for an item identified by the given URN.
     *
     * @param simpleUri the resource URN of the item, in form of a simple URI
     * @return a square widget of the item's icon or preview
     */
    private FlowRenderable createItemWidget(String simpleUri) {
        return new WidgetFlowRenderable(new ItemWidget(simpleUri), 48, 48, simpleUri);
    }

    /**
     * Create a help entry for the given prefab.
     * <p>
     * The help entry contains the detailed help rendered as {@link HTMLDocument} as well as the hyperlink for
     * identification and the display name (for sorting in the UI).
     * <p>
     * The detailed help document consists of a title paragraph with the item's image and display name, followed by
     * paragraphs derived from associated {@link HelpItem}s. These subsections are sorted alphabetically by their
     * title.
     *
     * @param itemPrefab the item prefab to create a help entry for
     * @param titleRenderStyle the render style to use for paragraph titles
     * @return an {@link ItemHelpEntry} for the prefab, or {@code null} if it does not match this category
     */
    private ItemHelpEntry helpEntryFor(Prefab itemPrefab, TextRenderStyle titleRenderStyle) {
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

            // add the ItemHelpComponent from the prefab (or the freshly created one if not present)
            List<HelpItem> helpItems = Lists.newArrayList(helpComponent);
            // add all HelpItem components from the prefab
            Iterables.filter(itemPrefab.iterateComponents(), HelpItem.class).forEach(helpItems::add);
            // add all HelpItems that have been registered
            itemsCategoryInGameHelpRegistry.getHelpItems(itemPrefab).forEach(helpItems::add);

            List<ParagraphData> allParagraphs = helpItems.stream()
                    .distinct()
                    .sorted(Comparator.comparing(HelpItem::getTitle))
                    .map(HelpItem::getHelpSection)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            HTMLDocument documentData = new HTMLDocument(null);
            documentData.addParagraph(getTitleParagraph(itemPrefab.getName(), displayName, titleRenderStyle));
            documentData.addParagraphs(allParagraphs);

            return new ItemHelpEntry(itemPrefab.getName(), displayName, documentData);
        }
        return null;
    }

    /**
     * Assemble the title paragraph for an item based on the given prefab name.
     *
     * @param prefabName the prefab name, i.e., resource urn for the item
     * @param displayName the human readable name to display
     * @param renderStyle the render style for the title text
     * @return a paragraph with the item's icon on the left followed by the display name
     */
    private ParagraphData getTitleParagraph(String prefabName, String displayName, TextRenderStyle renderStyle) {
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
        if (rootDocument == null) {
            initialise();
        }
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
