package com.jg.evilord.entities.block;

import com.jg.evilord.containers.ArtifactCrafterContainer;
import com.jg.evilord.registries.ContainerRegistries;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class Test extends MenuBlockEntity<ArtifactCrafterContainer> {

	public Test(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public MenuType<ArtifactCrafterContainer> getContainerType() {
		return ContainerRegistries.artifactCrafter.get();
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("test");
	}

	@Override
	public MenuBlockEntity<ArtifactCrafterContainer> getParent() {
		return this;
	}

}
