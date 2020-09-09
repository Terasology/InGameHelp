// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.inGameHelp.systems;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.engine.registry.Share;
import org.terasology.inGameHelp.InGameHelpCategoryRegistry;
import org.terasology.inGameHelp.helpCategories.GeneralHelpCategory;
import org.terasology.inGameHelp.helpCategories.ItemsCategory;
import org.terasology.inGameHelpAPI.ItemsCategoryInGameHelpRegistry;
import org.terasology.inGameHelpAPI.event.OnAddNewCategoryEvent;
import org.terasology.inGameHelpAPI.systems.HelpCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * System that handles the different categories.
 */
@RegisterSystem
@Share(InGameHelpCategoryRegistry.class)
public class InGameHelpCommonSystem extends BaseComponentSystem implements InGameHelpCategoryRegistry {
    /**
     * Reference to the {@link org.terasology.inGameHelpAPI.ItemsCategoryInGameHelpRegistry}. This contains all known
     * items with an InGameHelp component.
     */
    @In
    ItemsCategoryInGameHelpRegistry itemsCategoryInGameHelpRegistry;

    /**
     * List of HelpCategories.
     */
    List<HelpCategory> categories = new ArrayList<>();

    /**
     * @return a list of help categories.
     */
    @Override
    public Iterable<HelpCategory> getCategories() {
        return categories;
    }

    /**
     * Adds a new help category.
     *
     * @param category the help category to add.
     */
    @Override
    public void registerCategory(HelpCategory category) {
        categories.add(category);
    }

    /**
     * Add a new category to the global list of help categories.
     *
     * @param event The event which contains the new category to be added.
     * @param entity Reference to the entity used for passing this event.
     */
    @ReceiveEvent
    public void addCategory(OnAddNewCategoryEvent event, EntityRef entity) {
        registerCategory(event.getCategory());
        event.getCategory().setRegistry(itemsCategoryInGameHelpRegistry);
    }

    /**
     * Initialises the system and registers a {@link org.terasology.inGameHelp.helpCategories.GeneralHelpCategory} and a
     * {@link org.terasology.inGameHelp.helpCategories.ItemsCategory}.
     */
    @Override
    public void initialise() {
        super.initialise();

        // Register the two base categories.
        registerCategory(new GeneralHelpCategory());
        registerCategory(new ItemsCategory(itemsCategoryInGameHelpRegistry));
    }
}
