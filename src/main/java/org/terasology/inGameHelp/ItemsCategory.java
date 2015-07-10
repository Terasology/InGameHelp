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
package org.terasology.inGameHelp;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.terasology.asset.Assets;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.inGameHelp.components.HelpItem;
import org.terasology.inGameHelp.components.ItemHelpComponent;
import org.terasology.inGameHelp.systems.HelpCategory;
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
import java.util.stream.Collectors;

public class ItemsCategory implements HelpCategory {
    Map<String, DocumentData> items = Maps.newHashMap();
    HTMLDocument rootDocument;
    DocumentData currentDocument;

    private void initialise() {
        TextRenderStyle titleRenderStyle = new TextRenderStyle() {
            @Override
            public Font getFont(boolean hyperlink) {
                return Assets.getFont("engine:title").get();
            }
        };
        ParagraphRenderStyle titleParagraphStyle = new ParagraphRenderStyle() {
            @Override
            public ContainerInteger getParagraphPaddingTop() {
                return new FixedContainerInteger(5);
            }
        };


        rootDocument = new HTMLDocument(null);
        FlowParagraphData itemListParagraph = new FlowParagraphData(null);
        rootDocument.addParagraph(itemListParagraph);

        for (Prefab itemPrefab : Assets.list(Prefab.class).stream()
                .map(x -> Assets.get(x, Prefab.class).get())
                .filter(x -> x.hasComponent(ItemHelpComponent.class))
                .collect(Collectors.toList())) {
            HTMLDocument documentData = new HTMLDocument(null);
            HelpItem helpComponent = itemPrefab.getComponent(ItemHelpComponent.class);

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

            // add all the other ones
            for (HelpItem helpItem : Iterables.filter(itemPrefab.iterateComponents(), HelpItem.class)) {
                if (helpItem != helpComponent) {
                    helpItem.addHelpItemSection(documentData);
                }
            }

            items.put(itemPrefab.getName(), documentData);

            // add this to the root document
            itemListParagraph.append(new WidgetFlowRenderable(new ItemWidget(itemPrefab.getName()), 48, 48, itemPrefab.getName()));
        }

        currentDocument = rootDocument;
    }

    @Override
    public String getCategoryName() {
        return "Items";
    }

    @Override
    public DocumentData getDocumentData() {
        if (currentDocument == null) {
            initialise();
        }
        return currentDocument;
    }

    @Override
    public void resetNavigation() {
        currentDocument = rootDocument;
    }

    @Override
    public boolean handleNavigate(String hyperlink) {
        if (items.containsKey(hyperlink)) {
            currentDocument = items.get(hyperlink);
            return true;
        } else {
            return false;
        }
    }
}
