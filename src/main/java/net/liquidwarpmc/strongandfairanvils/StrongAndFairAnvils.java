package net.liquidwarpmc.strongandfairanvils;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StrongAndFairAnvils implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("StrongAndFairAnvils");

	public static final Block STONE_ANVIL = new AnvilBlock(FabricBlockSettings.of(Material.REPAIR_STATION, MaterialColor.STONE).strength(5.0F, 1200.0F).sounds(BlockSoundGroup.ANVIL));

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, new Identifier("strongandfairanvils", "stone_anvil"), STONE_ANVIL);
		Registry.register(Registry.ITEM, new Identifier("strongandfairanvils", "stone_anvil"), new BlockItem(STONE_ANVIL, new Item.Settings().group(ItemGroup.MISC)));

		LOGGER.info("[Strong and Fair Anvils] initialized!");
	}
}
