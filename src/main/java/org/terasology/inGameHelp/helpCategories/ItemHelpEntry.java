// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.inGameHelp.helpCategories;

import org.terasology.engine.rendering.nui.widgets.browser.data.DocumentData;

class ItemHelpEntry {
    private final String hyperlink;
    private final String displayName;
    private final DocumentData documentData;

    ItemHelpEntry(String hyperlink, String displayName, DocumentData documentData) {
        this.hyperlink = hyperlink;
        this.displayName = displayName;
        this.documentData = documentData;
    }

    String getHyperlink() {
        return hyperlink;
    }

    String getDisplayName() {
        return displayName;
    }

    DocumentData getDocumentData() {
        return documentData;
    }
}
