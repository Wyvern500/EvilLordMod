package com.jg.evilord.entities.block;

import java.util.function.Consumer;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IJgMenuProvider<T extends BlockEntity, L extends AbstractContainerMenu> extends MenuProvider {

	@Override
	default AbstractContainerMenu createMenu(int id, Inventory inv, 
			Player player) {
		return create(id, inv, buf -> buf.writeBlockPos(getParent()
				.getBlockPos()));
	}
	
	default AbstractContainerMenu create(int id, Inventory inv, 
			Consumer<FriendlyByteBuf> extraDataWriter) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		extraDataWriter.accept(buf);
		return getContainerType().create(id, inv, buf);
	}
	
	public T getParent();
	
	public MenuType<L> getContainerType();
	
}
