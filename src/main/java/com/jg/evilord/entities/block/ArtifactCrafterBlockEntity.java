package com.jg.evilord.entities.block;

import com.jg.evilord.containers.ArtifactCrafterContainer;
import com.jg.evilord.registries.BlockEntityRegistries;
import com.jg.evilord.registries.ContainerRegistries;
import com.jg.evilord.soul.energy.ISoulEnergyStorage;
import com.jg.evilord.soul.energy.SoulEnergyStorage;
import com.mojang.logging.LogUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.network.NetworkHooks;

public class ArtifactCrafterBlockEntity extends BinderBlockEntity {

	private SoulEnergyStorage storage;
	
	public ArtifactCrafterBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityRegistries.artifactCrafter.get(), pos, state);
		this.storage = new SoulEnergyStorage(100);
	}

	@Override
	public int getContainerSize() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getItem(int p_18941_) {
		return null;
	}

	@Override
	public ItemStack removeItem(int p_18942_, int p_18943_) {
		return null;
	}

	@Override
	public ItemStack removeItemNoUpdate(int p_18951_) {
		return null;
	}

	@Override
	public void setItem(int p_18944_, ItemStack p_18945_) {
		
	}

	@Override
	public boolean stillValid(Player p_18946_) {
		return true;
	}

	@Override
	public void clearContent() {
		
	}

	@Override
	public void linkChanged(BlockState pState, Level pLevel, BlockPos pPos) {
		
	}

	@Override
	public void drops() {
		
	}

	@Override
	public void receiveMessageFromClient(CompoundTag nbt) {
		
	}

	@Override
	public void receiveMessageFromServer(CompoundTag nbt) {
		
	}

	@Override
	protected Component getDefaultName() {
		return new TranslatableComponent("com.jg.evilord.artifact_crafter_container");
	}
	
	@Override
	public ISoulEnergyStorage getSoulEnergyStorage() {
		return storage;
	}
	
	public ContainerData getData() {
		return data;
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory inv) {
		return new ArtifactCrafterContainer(id, inv, data, this);
	}

}
