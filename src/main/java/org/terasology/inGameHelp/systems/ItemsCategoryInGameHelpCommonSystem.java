// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.inGameHelp.systems;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.Share;
import org.terasology.engine.utilities.Assets;
import org.terasology.inGameHelpAPI.ItemsCategoryInGameHelpRegistry;
import org.terasology.inGameHelpAPI.components.HelpItem;
import org.terasology.inGameHelpAPI.components.ItemHelpComponent;

/**
 * System that handles the prefabs and HelpItems that are associated with the prefab.
 */
@RegisterSystem
@Share(ItemsCategoryInGameHelpRegistry.class)
public class ItemsCategoryInGameHelpCommonSystem extends BaseComponentSystem implements ItemsCategoryInGameHelpRegistry {
    /**
     * Contains all known prefabs that have the item help component and maps all and the help items associated with the
     * prefab.
     */
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
