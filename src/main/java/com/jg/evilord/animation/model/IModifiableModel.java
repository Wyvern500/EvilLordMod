package com.jg.evilord.animation.model;

import com.jg.evilord.client.model.item.WrapperModel;

import net.minecraft.client.resources.model.BakedModel;

public abstract class IModifiableModel {

	protected WrapperModel model;
	
	public IModifiableModel() {
		
	}
	
	public abstract WrapperModel getModifiedModel(BakedModel origin);
	
	public WrapperModel getModel() {
		return model;
	}

	public void setModel(WrapperModel model) {
		this.model = model;
	}
	
}
