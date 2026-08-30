package net.kimblazter.woskyblockutilities.item;

import net.kimblazter.woskyblockutilities.WOSkyblockUtilities;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.TraderLlamaEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class CaravanHorn extends Item {

    private final Random random = Random.create();

    private final int COOLDOWN_TICKS = 30; // 5 min cooldown

    public CaravanHorn(Settings settings) {
        super(settings);
    }

    private static void playSound(World world, PlayerEntity player, Instrument instrument) {
        SoundEvent soundEvent = instrument
                .soundEvent()
                .value();
        float f = instrument.range() / 16.0F;
        world.playSoundFromEntity(player, player, soundEvent, SoundCategory.RECORDS, f, 1.0F);
        world.emitGameEvent(GameEvent.INSTRUMENT_PLAY, player.getPos(), GameEvent.Emitter.of(player));
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return Registries.INSTRUMENT
                .get(Instruments.CALL_GOAT_HORN)
                .useDuration();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.TOOT_HORN;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        user.setCurrentHand(hand);
        playSound(world, user, Registries.INSTRUMENT.get(Instruments.CALL_GOAT_HORN));
        user
                .getItemCooldownManager()
                .set(this, COOLDOWN_TICKS);
        user.incrementStat(Stats.USED.getOrCreateStat(this));



        if (!world.isClient()) {
            ServerWorld serverWorld = (ServerWorld) world;

            BlockPos randomSpawnPos = getNearbySpawnPos(world, user.getBlockPos(), 10);
            if (randomSpawnPos == null) {
                return TypedActionResult.fail(itemStack);
            }

            WanderingTraderEntity wanderingTraderToSpawn = EntityType.WANDERING_TRADER.spawn(
                    serverWorld, randomSpawnPos, SpawnReason.EVENT
            );

            if (wanderingTraderToSpawn != null) {
                wanderingTraderToSpawn.setDespawnDelay(48000);
                for (int i = 0; i < 2; i++) {
                    spawnLlama(serverWorld, wanderingTraderToSpawn, 5);
                }
            }
        }


        itemStack.damage(1, user, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        return TypedActionResult.consume(itemStack);
    }

    private void spawnLlama(ServerWorld world, WanderingTraderEntity wanderingTrader, int range) {
        BlockPos blockPos = this.getNearbySpawnPos(world, wanderingTrader.getBlockPos(), range);
        if (blockPos != null) {
            TraderLlamaEntity traderLlamaEntity = EntityType.TRADER_LLAMA.spawn(world, blockPos, SpawnReason.MOB_SUMMONED);
            if (traderLlamaEntity != null) {
                traderLlamaEntity.attachLeash(wanderingTrader, true);
            } else {
                WOSkyblockUtilities.LOGGER.error("Could not spawn traderLlamaEntity");
            }
        } else {
            WOSkyblockUtilities.LOGGER.error("Could not spawn traderLlamaEntity");
        }
    }

    @Nullable
    private BlockPos getNearbySpawnPos(WorldView world, BlockPos pos, int range) {
        BlockPos blockPos = null;
        SpawnLocation spawnLocation = SpawnRestriction.getLocation(EntityType.WANDERING_TRADER);

        for (int i = 0; i < 10; i++) {
            int j = pos.getX() + this.random.nextInt(range * 2) - range;
            int k = pos.getZ() + this.random.nextInt(range * 2) - range;
            int l = world.getTopY(Heightmap.Type.WORLD_SURFACE, j, k);
            BlockPos blockPos2 = new BlockPos(j, l, k);
            if (spawnLocation.isSpawnPositionOk(world, blockPos2, EntityType.WANDERING_TRADER)) {
                blockPos = blockPos2;
                break;
            }
        }

        return blockPos;
    }
}
