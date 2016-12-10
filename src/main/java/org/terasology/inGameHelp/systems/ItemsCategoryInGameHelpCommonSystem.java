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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import org.terasology.utilities.Assets;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.inGameHelp.ItemsCategoryInGameHelpRegistry;
import org.terasology.inGameHelp.components.HelpItem;
import org.terasology.inGameHelp.components.ItemHelpComponent;
import org.terasology.registry.Share;

/**
 * System that handles the prefabs and HelpItems that are associated with the prefab.
 */
@RegisterSystem
@Share(ItemsCategoryInGameHelpRegistry.class)
public class ItemsCategoryInGameHelpCommonSystem extends BaseComponentSystem implements ItemsCategoryInGameHelpRegistry {
    /** Contains all known prefabs that have the item help component and maps all and the help items associated with the prefab. */
    Multimap<Prefab, HelpItem> knownPrefabs = HashMultimap.create();

    /**
     * Initialises the system.
     */
    @Override
    public void initialise() {
        super.initialise();
        
        //add the prefabs and HelpItems to the knownPrefabs
        Assets.list(Prefab.class).stream()
                .map(x -> Assets.get(x, Prefab.class).get())
                .filter(x -> x.hasComponent(ItemHelpComponent.class))
                .forEach(x -> addKnownPrefab(x));
    }

    /**
     * Adds prefab and help items associated with the prefab to knownPrefabs.
     *
     * @param prefab the prefab to add.
     * @param helpItems the help items to add.
     */
    @Override
    public void addKnownPrefab(Prefab prefab, HelpItem... helpItems) {
        knownPrefabs.put(prefab, null);
        for (HelpItem helpItem : helpItems) {
            knownPrefabs.put(prefab, helpItem);
        }
    }

    /**
     * Gets the prefabs.
     *
     * @return the set of prefabs.
     */
    @Override
    public Iterable<Prefab> getKnownPrefabs() {
        return knownPrefabs.keySet();
    }

    /**
     * Gets an iterable of help items that are associated with the prefab.
     *
     * @param prefab the prefab with which to get the help items.
     * @return the set of help items associated with the prefab.
     */
    @Override
    public Iterable<HelpItem> getHelpItems(Prefab prefab) {
        return Iterables.filter(knownPrefabs.get(prefab), x -> x != null);
    }
}
