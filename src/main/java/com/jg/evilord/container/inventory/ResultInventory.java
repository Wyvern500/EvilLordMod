package com.jg.evilord.container.inventory;

import org.apache.logging.log4j.LogManager;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class ResultInventory implements Container {

	private final NonNullList<ItemStack> items;
	private final AbstractContainerMenu menu;

	public ResultInventory(AbstractContainerMenu p_39325_, int size) {
		this.items = NonNullList.withSize(size, ItemStack.EMPTY);
		this.menu = p_39325_;
	}

	public int getContainerSize() {
		return this.items.size();
	}

	public boolean isEmpty() {
		for (ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	public ItemStack getItem(int p_39332_) {
		return p_39332_ >= this.getContainerSize() ? ItemStack.EMPTY : this.items.get(p_39332_);
	}

	public ItemStack removeItemNoUpdate(int p_39344_) {
		return ContainerHelper.takeItem(this.items, p_39344_);
	}

	public ItemStack removeItem(int p_39334_, int p_39335_) {
		return ContainerHelper.removeItem(this.items, p_39334_, p_39335_);
	}

	public void setItem(int index, ItemStack stack) {
		LogManager.getLogger().info("Index: " + index);
		this.items.set(index, stack);
	}

	public void setChanged() {
	}

	public boolean stillValid(Player p_39340_) {
		return true;
	}

	public void clearContent() {
		this.items.clear();
	}

	public int getSize() {
		return items.size();
	}

	public void fillStackedContents(StackedContents p_39342_) {
		for (ItemStack itemstack : this.items) {
			p_39342_.accountSimpleStack(itemstack);
		}
	}
}