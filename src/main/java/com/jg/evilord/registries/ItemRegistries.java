package com.jg.evilord.registries;

import com.jg.evilord.Evilord;
import com.jg.evilord.item.GrimoireOfEvilItem;
import com.jg.evilord.item.SoulContainerItem;
import com.jg.evilord.item.SoulContainerItem.ContainerTier;
import com.jg.evilord.staff.StaffItem;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistries {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister
			.create(ForgeRegistries.ITEMS, Evilord.MODID);
	
	public static final RegistryObject<Item> deathStaff = ITEMS.register("death_staff",
			() -> new StaffItem(new Item.Properties().tab(Evilord.getTab()).stacksTo(1)));
	
	public static final RegistryObject<Item> grimoireOfEvil = ITEMS.register("grimoire_of_evil",
			() -> new GrimoireOfEvilItem(new Item.Properties().tab(Evilord.getTab()).stacksTo(1)));
	
	public static final RegistryObject<Item> connectionManipulatorWand = 
			ITEMS.register("connection_manipulator_wand",
			() -> new Item(new Item.Properties().tab(Evilord.getTab()).stacksTo(1)));
	
	public static final RegistryObject<Item> soulContainer = ITEMS.register("soul_container",
			() -> new SoulContainerItem(new Item.Properties().tab(Evilord.getTab())
					.stacksTo(1), ContainerTier.ZERO));
	
	public static final RegistryObject<Item> soulContainerTier1 = ITEMS.register("soul_container_1",
			() -> new SoulContainerItem(new Item.Properties().tab(Evilord.getTab())
					.stacksTo(1), ContainerTier.ONE));
	
	public static final RegistryObject<Item> soulContainerTier2 = ITEMS.register("soul_container_2",
			() -> new SoulContainerItem(new Item.Properties().tab(Evilord.getTab())
					.stacksTo(1), ContainerTier.TWO));
	
	public static final RegistryObject<Item> soulContainerTier3 = ITEMS.register("soul_container_3",
			() -> new SoulContainerItem(new Item.Properties().tab(Evilord.getTab())
					.stacksTo(1), ContainerTier.THREE));
	
	public static final RegistryObject<Item> soulContainerTier4 = ITEMS.register("soul_container_4",
			() -> new SoulContainerItem(new Item.Properties().tab(Evilord.getTab())
					.stacksTo(1), ContainerTier.FOUR));
	
	public static final RegistryObject<Item> soulContainerTier5 = ITEMS.register("soul_container_5",
			() -> new SoulContainerItem(new Item.Properties().tab(Evilord.getTab())
					.stacksTo(1), ContainerTier.FIVE));
	
	public static final RegistryObject<BlockItem> soulManipulator = ITEMS.register("soul_manipulator",
			() -> new BlockItem(BlockRegistries.soulManipulator.get(),
					new Item.Properties().tab(Evilord.getTab()).stacksTo(64)));
	
	public static final RegistryObject<BlockItem> spellExplorer = ITEMS.register("spell_explorer",
			() -> new BlockItem(BlockRegistries.spellExplorer.get(),
					new Item.Properties().tab(Evilord.getTab()).stacksTo(64)));
	
}
