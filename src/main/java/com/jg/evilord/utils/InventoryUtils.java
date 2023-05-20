package com.jg.evilord.utils;

import java.util.List;

import com.jg.evilord.client.screens.AnimationScreen.Pair;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class InventoryUtils {

	public static void removeFromInventory(Player player, List<Pair<Integer, Integer>> data) {
		removeFromInventory(player.getInventory(), data);
	}
	
	public static void removeFromInventory(Inventory inv, List<Pair<Integer, Integer>> data) {
		for(Pair<Integer, Integer> pair : data) {
			inv.removeItem(pair.getLeft(), pair.getRight());
		}
	}
	
}
