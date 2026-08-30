package net.kimblazter.woskyblockutilities.item;

import net.kimblazter.woskyblockutilities.WOSkyblockUtilities;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;

public class ModItems {

    public static final Item PUTRID_SOUP = registerItem("putrid_soup",
                                                        new Item(
                                                                new Item.Settings().maxCount(1).food(new FoodComponent.Builder().nutrition(3).alwaysEdible().saturationModifier(0.1F).statusEffect(new StatusEffectInstance(
                                                                StatusEffects.UNLUCK, 12000, 100), 1.0F).statusEffect(new StatusEffectInstance(
                                                                StatusEffects.HUNGER, 600, 0), 1.0F).build())
                                                        ));

    public static final Item COMPRESSED_LILY_PAD = registerItem("compressed_lily_pad",
                                                                new Item(new Item.Settings().rarity(Rarity.EPIC).component(
                                                                        DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE,
                                                                        true)));

    public static final Item CARAVAN_HORN = registerItem("caravan_horn",
                                                         new CaravanHorn(new Item.Settings().rarity(Rarity.RARE).maxCount(1).maxDamage(5)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, WOSkyblockUtilities.id(name), item);
    }


    public static void registerModItems() {
        WOSkyblockUtilities.LOGGER.info("Registering items for " + WOSkyblockUtilities.MOD_ID);
    }
}
