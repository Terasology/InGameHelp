// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.inGameHelp;

import org.terasology.inGameHelpAPI.systems.HelpCategory;

/**
 * Interface for managing the different help categories.
 */
public interface InGameHelpCategoryRegistry {

    /**
     * Retrieves the {@link org.terasology.inGameHelpAPI.systems.HelpCategory}s previously registered to this registry.
     *
     * @return an iterable of the help categories.
     */
    Iterable<HelpCategory> getCategories();

    /**
     * Adds a {@link org.terasology.inGameHelpAPI.systems.HelpCategory} to this registry.
     *
     * @param category the {@link org.terasology.inGameHelpAPI.systems.HelpCategory} to add.
     */
    void registerCategory(HelpCategory category);
}
