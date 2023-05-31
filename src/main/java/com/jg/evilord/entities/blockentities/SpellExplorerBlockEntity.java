package com.jg.evilord.entities.blockentities;

import javax.annotation.Nonnull;

import com.jg.evilord.containers.SpellExplorerContainer;
import com.jg.evilord.entities.block.InventoryBinderBlockEntity;
import com.jg.evilord.entities.block.inventory.SimpleBlockEntityInventory;
import com.jg.evilord.registries.BlockEntityRegistries;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class SpellExplorerBlockEntity extends InventoryBinderBlockEntity {

	private static final Component CONTAINER_TITLE = new TranslatableComponent(
			"com.jg.container.spell_explorer_container");

	public SpellExplorerBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityRegistries.spellExplorer.get(), pos, state);
	}
	
	protected SpellExplorerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state, 100, 4, 1, 1);
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		// Will get tag from #getUpdateTag
		return ClientboundBlockEntityDataPacket.create(this, be -> {
			CompoundTag nbt = new CompoundTag();
			//this.writeCustomNBT(nbttagcompound, true);
			nbt.putString("test", "Test Update packet");
			return nbt;
		});
	}
	
	@Override
	public void handleUpdateTag(CompoundTag tag) {
		super.handleUpdateTag(tag);
		LogUtils.getLogger().info("test: " + tag.getString("test"));
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		super.onDataPacket(net, pkt);
		CompoundTag nonNullTag = pkt.getTag() != null ? pkt.getTag() : new CompoundTag();
		LogUtils.getLogger().info("test: " + nonNullTag.getString("test"));
	}
	
	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = new CompoundTag();
		tag.putString("test", "Test update tag");
		return tag;
	}
	
	@Override
	public CompoundTag serializeNBT() {
		return super.serializeNBT();
	}
	
	@Override
	public void deserializeNBT(CompoundTag nbt) {
		super.deserializeNBT(nbt);
		LogUtils.getLogger().info("Vinculate: " + nbt.getString(LINK));
	}

	@Override
	public Component getDisplayName() {
		return CONTAINER_TITLE;
	}

	@Override
	public void drops() {
		Containers.dropContents(this.level, this.worldPosition, inv);
	}

	@Override
	public void linkChanged(BlockState state, Level level, BlockPos pos) {
		/*String[] sData = serializeNBT().getString(LINK).split(",");
		if(sData.length == 0) return;
		if(sData[0].equals("")) return;
		BlockPos bePos = new BlockPos(Integer.parseInt(sData[0]), Integer.parseInt(sData[1]),
				Integer.parseInt(sData[2]));
		BlockEntity be = level.getBlockEntity(bePos);
		if(be != null) {
			((BinderBlockEntity)be).unbind();
		}*/
	}

	@Override
	public void receiveMessageFromClient(CompoundTag nbt) {
		LogUtils.getLogger().info("from Client");
	}

	@Override
	public void receiveMessageFromServer(CompoundTag nbt) {
		LogUtils.getLogger().info("from Server");
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
		return new SpellExplorerContainer(id, inv, this);
	}

	@Override
	public void onEnergyChanged(int energy, boolean extracted) {
		// TODO Auto-generated method stub
		
	}

}
