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

import org.terasology.utilities.Assets;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.layers.ingame.inventory.ItemCell;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.family.BlockFamily;
import org.terasology.world.block.items.BlockItemFactory;
import org.terasology.world.block.loader.BlockFamilyDefinition;

import java.util.Optional;

public class ItemWidget extends ItemCell {
    EntityRef item;
    String itemUrn;

    public ItemWidget(String itemUrn) {
        this.itemUrn = itemUrn;
    }

    @Override
    public EntityRef getTargetItem() {
        if (item == null) {
            EntityManager entityManager = CoreRegistry.get(EntityManager.class);
            BlockManager blockManager = CoreRegistry.get(BlockManager.class);
            BlockItemFactory blockFactory = new BlockItemFactory(entityManager);

            Optional<BlockFamilyDefinition> blockFamilyDefinition = Assets.get(itemUrn, BlockFamilyDefinition.class);
            if (blockFamilyDefinition.isPresent()) {
                BlockFamily blockFamily = blockManager.getBlockFamily(itemUrn);
                item = blockFactory.newInstance(blockFamily);
            } else {
                item = CoreRegistry.get(EntityManager.class).create(itemUrn);
            }
        }
        return item;
    }
}
