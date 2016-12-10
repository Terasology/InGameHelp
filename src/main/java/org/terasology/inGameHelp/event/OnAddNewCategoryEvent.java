/*
 * Copyright 2016 MovingBlocks
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
package org.terasology.inGameHelp.event;

import org.terasology.entitySystem.event.Event;
import org.terasology.inGameHelp.systems.HelpCategory;

/**
 * Event that is sent to the entity whenever a new HelpCategory is added. Contains the specific help category that was added.
 */
public class OnAddNewCategoryEvent implements Event {
    /** Category to be stored in this event.*/
    private HelpCategory category;

    /**
     * Gets the category contained this event.
     *
     * @return              The category.
     *
     */
    public HelpCategory getCategory() {
        return category;
    }

    /**
     * Creates a new instance of this event with the given HelpCategory.
     *
     * @param category      The HelpCategory instance.
     *
     */
    public OnAddNewCategoryEvent(HelpCategory category) {
        this.category = category;
    }
}
