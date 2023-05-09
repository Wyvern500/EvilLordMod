package com.jg.evilord.client.screens.widgets;

import com.jg.evilord.animation.model.JgModelPart;
import com.jg.evilord.client.screens.widgets.JGSelectionList.Key;

import net.minecraft.client.gui.Font;

public class JgPartKey extends Key {

	protected JgModelPart part;
	
	public JgPartKey(Font font, JgModelPart part) {
		super(font, part.getName());
		this.part = part;
	}
	
	public JgModelPart getPart() {
		return part;
	}
	
}
