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

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.inGameHelp.InGameHelpCategoryRegistry;
import org.terasology.inGameHelp.ItemsCategoryInGameHelpRegistry;
import org.terasology.inGameHelp.event.OnAddNewCategoryEvent;
import org.terasology.inGameHelp.helpCategories.GeneralHelpCategory;
import org.terasology.inGameHelp.helpCategories.ItemsCategory;
import org.terasology.registry.In;
import org.terasology.registry.Share;

import java.util.ArrayList;
import java.util.List;

/**
 * System that handles the different categories.
 */
@RegisterSystem
@Share(InGameHelpCategoryRegistry.class)
public class InGameHelpCommonSystem extends BaseComponentSystem implements InGameHelpCategoryRegistry {
    /** Reference to the {@link org.terasology.inGameHelp.ItemsCategoryInGameHelpRegistry}. This contains all known items with an InGameHelp component. */
    @In
    ItemsCategoryInGameHelpRegistry itemsCategoryInGameHelpRegistry;

    /** List of HelpCategories. */
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
     * @param event             The event which contains the new category to be added.
     * @param entity            Reference to the entity used for passing this event.
     *
     */
    @ReceiveEvent
    public void addCategory(OnAddNewCategoryEvent event, EntityRef entity) {
        registerCategory(event.getCategory());
        event.getCategory().setRegistry(itemsCategoryInGameHelpRegistry);
    }

    /**
     * Initialises the system and registers a {@link org.terasology.inGameHelp.helpCategories.GeneralHelpCategory} and a {@link org.terasology.inGameHelp.helpCategories.ItemsCategory}.
     */
    @Override
    public void initialise() {
        super.initialise();

        // Register the two base categories.
        registerCategory(new GeneralHelpCategory());
        registerCategory(new ItemsCategory(itemsCategoryInGameHelpRegistry));
    }
}
