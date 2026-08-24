package net.kimblazter.woskyblockutilities.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.kimblazter.woskyblockutilities.item.ModItems;

public class WOSkyblockUtilitiesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			return 0xFF71C35C;
		}, ModItems.COMPRESSED_LILY_PAD);
	}
}