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

import org.terasology.math.geom.Rect2i;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.widgets.browser.data.basic.flow.FlowRenderable;
import org.terasology.rendering.nui.widgets.browser.ui.style.TextRenderStyle;

public class WidgetFlowRenderable implements FlowRenderable<WidgetFlowRenderable> {
    UIWidget widget;
    private int width;
    private int height;
    private String hyperlink;

    public WidgetFlowRenderable(UIWidget widget,
                                int width, int height, String hyperlink) {
        this.widget = widget;
        this.width = width;
        this.height = height;
        this.hyperlink = hyperlink;
    }

    @Override
    public void render(Canvas canvas, Rect2i bounds, TextRenderStyle defaultRenderStyle) {
        canvas.drawWidget(widget, bounds);
    }

    @Override
    public int getMinWidth(TextRenderStyle defaultRenderStyle) {
        return width;
    }

    @Override
    public int getWidth(TextRenderStyle defaultRenderStyle) {
        return width;
    }

    @Override
    public int getHeight(TextRenderStyle defaultRenderStyle) {
        return height;
    }

    @Override
    public String getAction() {
        return hyperlink;
    }

    @Override
    public SplitResult<WidgetFlowRenderable> splitAt(TextRenderStyle defaultRenderStyle, int splitResultWidth) {
        if (splitResultWidth < width) {
            return new SplitResult<>(null, this);
        } else {
            return new SplitResult<>(this, null);
        }
    }
}
