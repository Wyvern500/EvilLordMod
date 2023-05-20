package com.jg.evilord.client.events;

import java.util.Map;

import com.jg.evilord.animation.model.JgModel;
import com.jg.evilord.animation.model.ModelsHandler;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Event;

public class RegisterModelEvent extends Event {
	
	public RegisterModelEvent() {
		
	}
	
	public void register(Item item, JgModel model) {
		model.setItem(item);
		ModelsHandler.register(item.getDescriptionId(), model);
	}
	
	public void get(Item item) {
		ModelsHandler.get(item.getDescriptionId());
	}
	
	public Map<String, JgModel> getModels() {
		return ModelsHandler.getMap();
	}
	
}
