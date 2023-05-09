package com.jg.evilord.client.model.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jg.evilord.utils.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class DynamicGunModel extends BaseModel {

	ItemStack stack;
	List<BakedQuad> quads;
	boolean init = false;
	boolean firstTime;
	
	public DynamicGunModel(BakedModel origin, Item item, ItemStack stack) {
		super(origin, item);
		this.stack = stack;
		this.quads = new ArrayList<BakedQuad>();
		this.firstTime = true;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction dir, Random random) {
		if (!init || firstTime) {
			Minecraft mc = Minecraft.getInstance();
			if (!firstTime) {
				this.stack = mc.player.getMainHandItem();
			}
			quads.clear();

			// Add quads

			if (firstTime) {
				firstTime = false;
			}

			init = true;
		}
		
		if (dir == null) {
			return quads;
		} else {
			return Utils.EMPTY;
		}
	}
	
	public void reconstruct() {
		init = false;
	}

	@Override
	public ItemOverrides getOverrides() {
		return null;
	}

}
