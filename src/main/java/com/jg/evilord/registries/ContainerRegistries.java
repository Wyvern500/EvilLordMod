package com.jg.evilord.registries;

import com.jg.evilord.Evilord;
import com.jg.evilord.containers.ArtifactCrafterContainer;
import com.jg.evilord.containers.SoulManipulatorContainer;
import com.jg.evilord.containers.SpellExplorerContainer;
import com.mojang.logging.LogUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerRegistries {

	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(
			ForgeRegistries.CONTAINERS, Evilord.MODID);

	public static final RegistryObject<MenuType<SoulManipulatorContainer>> soulContainerManipulator = 
			CONTAINERS.register("soul_container_manipulator", 
					() -> new MenuType<>(SoulManipulatorContainer::new));
	
	public static final RegistryObject<MenuType<SpellExplorerContainer>> spellExplorer = 
			CONTAINERS.register("spell_explorer", 
					() -> new MenuType<>(SpellExplorerContainer::new));
	
	public static final RegistryObject<MenuType<ArtifactCrafterContainer>> artifactCrafter = 
			CONTAINERS.register("artifact_crafter", 
					() -> 
			new MenuType<>((IContainerFactory<ArtifactCrafterContainer>)(windowId, inv, data) -> {
						if(data == null) {
							LogUtils.getLogger().info("Data is null");
						} else {
							LogUtils.getLogger().info("Data is not null: " + 
									data.toString());
						}
						return new ArtifactCrafterContainer(windowId, inv, data);
					}));
	
	
}
