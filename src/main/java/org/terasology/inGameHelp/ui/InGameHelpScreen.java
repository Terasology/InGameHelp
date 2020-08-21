// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.inGameHelp.ui;

import org.terasology.inGameHelp.InGameHelpCategoryRegistry;
import org.terasology.inGameHelpAPI.systems.HelpCategory;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.nui.layouts.FlowLayout;
import org.terasology.nui.widgets.UIButton;
import org.terasology.rendering.nui.widgets.browser.ui.BrowserHyperlinkListener;
import org.terasology.rendering.nui.widgets.browser.ui.BrowserWidget;

/**
 * Implementation for the help screen.
 */
public class InGameHelpScreen extends CoreScreenLayer {
    /** Layout that contains buttons for the help category tabs. */
    FlowLayout categoryButtons;

    /** Browser that contains a listener for navigating to the different help categories. */
    BrowserWidget browser;
    
    /** Iterable that contains all the help categories. */
    Iterable<HelpCategory> categories;


    /**
     * Initialises the screen with buttons containing the names of the help categories that navigate to documents containing information for each of the help categories. 
     */
    @Override
    public void initialise() {
        InGameHelpCategoryRegistry categoryRegistry = CoreRegistry.get(InGameHelpCategoryRegistry.class);
        categories = categoryRegistry.getCategories();

        //populate categorybuttons with buttons referencing information from the different HelpCategory tabs
        categoryButtons = find("categoryButtons", FlowLayout.class);
        if (categoryButtons != null) {
            for (final HelpCategory category : categories) {
                UIButton newButton = new UIButton();
                newButton.setText(category.getCategoryName());
                newButton.subscribe(widget -> navigateTo(category));
                categoryButtons.addWidget(newButton, null);
            }
        }

        //add listener to browser
        browser = find("browser", BrowserWidget.class);
        if (browser != null) {
            browser.addBrowserHyperlinkListener(new BrowserHyperlinkListener() {
                /**
                 * Goes to the document referenced by the hyperlink.
                 *
                 * @param hyperlink the link to the document
                 */
                @Override
                public void hyperlinkClicked(String hyperlink) {
                    navigateTo(hyperlink);
                }
            });
        }

        //navigates to all of the help categories
        for (HelpCategory helpCategory : categories) {
            navigateTo(helpCategory);
            break;
        }

    }

    /**
     * Navigates to the document referenced by hyperlink.
     *
     * @param hyperlink the link to the document.
     */
    public void navigateTo(String hyperlink) {
        for (HelpCategory helpCategory : categories) {
            if (helpCategory.handleNavigate(hyperlink)) {
                browser.navigateTo(helpCategory.getDocumentData());
                break;
            }
        }
    }

    /**
     * Navigates to the base document.
     *
     * @param category the help tab that the user is on.
     */
    private void navigateTo(HelpCategory category) {
        category.resetNavigation();
        browser.navigateTo(category.getDocumentData());
    }
}
