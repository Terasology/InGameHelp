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

import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.inGameHelp.components.HelpItem;

/**
 * Interface for dealing with the prefabs for the items. Contains all known items with the help item component.
 */
public interface ItemsCategoryInGameHelpRegistry {

    /**
     * Adds a Prefab and a variable amount of {@link org.terasology.inGameHelp.components.HelpItem}s that are associated with the prefab.
     *
     * @param prefab the prefab to add.
     * @param helpItems the help items that are associated with the prefab.
     */
    void addKnownPrefab(Prefab prefab, HelpItem... helpItems);

    /**
     * @return an Iterable containing the Prefabs.
     */
    Iterable<Prefab> getKnownPrefabs();

    /**
     * Gets all the help items for the prefab.
     *
     * @param prefab the prefab to get the help items for.
     * @return an Iterable containing the help items for the prefab.
     */
    Iterable<HelpItem> getHelpItems(Prefab prefab);
}
