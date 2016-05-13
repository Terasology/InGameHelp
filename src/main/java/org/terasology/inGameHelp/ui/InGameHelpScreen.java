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

import org.terasology.inGameHelp.InGameHelpCategoryRegistry;
import org.terasology.inGameHelp.systems.HelpCategory;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.layouts.FlowLayout;
import org.terasology.rendering.nui.widgets.UIButton;
import org.terasology.rendering.nui.widgets.browser.ui.BrowserHyperlinkListener;
import org.terasology.rendering.nui.widgets.browser.ui.BrowserWidget;

public class InGameHelpScreen extends CoreScreenLayer {
    FlowLayout categoryButtons;
    BrowserWidget browser;

    Iterable<HelpCategory> categories;


    @Override
    public void initialise() {
        InGameHelpCategoryRegistry categoryRegistry = CoreRegistry.get(InGameHelpCategoryRegistry.class);
        categories = categoryRegistry.getCategories();

        categoryButtons = find("categoryButtons", FlowLayout.class);
        if (categoryButtons != null) {
            for (final HelpCategory category : categories) {
                UIButton newButton = new UIButton();
                newButton.setText(category.getCategoryName());
                newButton.subscribe(widget -> navigateTo(category));
                categoryButtons.addWidget(newButton, null);
            }
        }

        browser = find("browser", BrowserWidget.class);
        if (browser != null) {
            browser.addBrowserHyperlinkListener(new BrowserHyperlinkListener() {
                @Override
                public void hyperlinkClicked(String hyperlink) {
                    navigateTo(hyperlink);
                }
            });
        }

        for (HelpCategory helpCategory : categories) {
            navigateTo(helpCategory);
            break;
        }

    }

    public void navigateTo(String hyperlink) {
        for (HelpCategory helpCategory : categories) {
            if (helpCategory.handleNavigate(hyperlink)) {
                browser.navigateTo(helpCategory.getDocumentData());
                break;
            }
        }
    }

    private void navigateTo(HelpCategory category) {
        category.resetNavigation();
        browser.navigateTo(category.getDocumentData());
    }
}
