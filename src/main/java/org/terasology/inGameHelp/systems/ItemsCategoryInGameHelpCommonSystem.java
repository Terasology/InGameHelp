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

@RegisterSystem
@Share(ItemsCategoryInGameHelpRegistry.class)
public class ItemsCategoryInGameHelpCommonSystem extends BaseComponentSystem implements ItemsCategoryInGameHelpRegistry {
    Multimap<Prefab, HelpItem> knownPrefabs = HashMultimap.create();

    @Override
    public void initialise() {
        super.initialise();

        Assets.list(Prefab.class).stream()
                .map(x -> Assets.get(x, Prefab.class).get())
                .filter(x -> x.hasComponent(ItemHelpComponent.class))
                .forEach(x -> addKnownPrefab(x));
    }

    @Override
    public void addKnownPrefab(Prefab prefab, HelpItem... helpItems) {
        knownPrefabs.put(prefab, null);
        for (HelpItem helpItem : helpItems) {
            knownPrefabs.put(prefab, helpItem);
        }
    }

    @Override
    public Iterable<Prefab> getKnownPrefabs() {
        return knownPrefabs.keySet();
    }

    @Override
    public Iterable<HelpItem> getHelpItems(Prefab prefab) {
        return Iterables.filter(knownPrefabs.get(prefab), x -> x != null);
    }
}
