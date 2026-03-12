package com.slimeeystudios.orbitalrod.item;

import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class OrbitalRodItem extends FishingRodItem {
    private static final double MAX_TARGET_DISTANCE = 100.0D;
    private static final double BASE_SPAWN_HEIGHT = 100.0D;
    private static final int LAYER_COUNT = 6;
    private static final int RINGS_PER_LAYER = 8;
    private static final int BASE_TNT_PER_RING = 8;
    private static final int TNT_PER_RING_STEP = 4;
    private static final double BASE_RING_RADIUS = 1.5D;
    private static final double RING_RADIUS_STEP = 1.75D;
    private static final double LAYER_VERTICAL_STEP = 4.0D;
    private static final int TNT_FUSE_TICKS = 80;
    private static final int EXTRA_EXPLOSION_DELAY_TICKS = 140;

    public OrbitalRodItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        HitResult hitResult = user.raycast(MAX_TARGET_DISTANCE, 0.0F, false);

        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return super.use(world, user, hand);
        }

        if (!world.isClient) {
            BlockPos targetPos = ((BlockHitResult) hitResult).getBlockPos();
            Vec3d targetCenter = Vec3d.ofCenter(targetPos);
            ServerWorld serverWorld = (ServerWorld) world;

            spawnActivationParticles(serverWorld, targetCenter);
            spawnOrbitalStrike(serverWorld, targetCenter, user);
            serverWorld.playSound(null, targetPos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1.2F, 0.6F);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(stack, world.isClient());
    }

    private void spawnActivationParticles(ServerWorld world, Vec3d targetCenter) {
        world.spawnParticles(ParticleTypes.SMOKE, targetCenter.x, targetCenter.y + 0.1D, targetCenter.z, 24, 0.4D, 0.15D, 0.4D, 0.01D);
        world.spawnParticles(ParticleTypes.FLAME, targetCenter.x, targetCenter.y + 0.1D, targetCenter.z, 16, 0.25D, 0.1D, 0.25D, 0.01D);
    }

    private void spawnOrbitalStrike(ServerWorld world, Vec3d targetCenter, PlayerEntity owner) {
        for (int layer = 0; layer < LAYER_COUNT; layer++) {
            double spawnY = targetCenter.y + BASE_SPAWN_HEIGHT + (layer * LAYER_VERTICAL_STEP);

            for (int ring = 0; ring < RINGS_PER_LAYER; ring++) {
                double radius = BASE_RING_RADIUS + (ring * RING_RADIUS_STEP);
                int tntPerRing = BASE_TNT_PER_RING + (ring * TNT_PER_RING_STEP);

                for (int index = 0; index < tntPerRing; index++) {
                    double angle = (Math.PI * 2.0D * index) / tntPerRing;
                    double spawnX = targetCenter.x + Math.cos(angle) * radius;
                    double spawnZ = targetCenter.z + Math.sin(angle) * radius;

                    TntEntity tnt = new TntEntity(world, spawnX, spawnY, spawnZ, owner);
                    tnt.setFuse(TNT_FUSE_TICKS + EXTRA_EXPLOSION_DELAY_TICKS);
                    tnt.setVelocity(
                            world.random.nextGaussian() * 0.012D,
                            -0.12D - (world.random.nextDouble() * 0.03D),
                            world.random.nextGaussian() * 0.012D
                    );
                    world.spawnEntity(tnt);
                }
            }
        }
    }
}
