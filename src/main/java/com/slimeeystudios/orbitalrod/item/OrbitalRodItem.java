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
    private static final double MAX_TARGET_DISTANCE = 108.0D;
    private static final double BASE_SPAWN_HEIGHT = 108.0D;
    private static final int LAYER_COUNT = 9;
    private static final int CIRCUMFERENCE_COUNT = 5;
    private static final int TNT_PER_CIRCUMFERENCE = 48;
    private static final double BASE_CIRCUMFERENCE_RADIUS = 12.0D;
    private static final double CIRCUMFERENCE_RADIUS_STEP = 24.0D;
    private static final double LAYER_VERTICAL_STEP = 9.0D;
    private static final int TNT_FUSE_TICKS = 96;
    private static final int EXTRA_EXPLOSION_DELAY_TICKS = 108;

    public OrbitalRodItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
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
            double centerFallSpeed = -0.17D - (layer * 0.004D);

            TntEntity centerTnt = new TntEntity(world, targetCenter.x, spawnY, targetCenter.z, owner);
            centerTnt.setFuse(TNT_FUSE_TICKS + EXTRA_EXPLOSION_DELAY_TICKS);
            centerTnt.setVelocity(0.0D, centerFallSpeed, 0.0D);
            world.spawnEntity(centerTnt);

            for (int ring = 0; ring < CIRCUMFERENCE_COUNT; ring++) {
                double radius = BASE_CIRCUMFERENCE_RADIUS + (ring * CIRCUMFERENCE_RADIUS_STEP);

                for (int index = 0; index < TNT_PER_CIRCUMFERENCE; index++) {
                    double angle = (Math.PI * 2.0D * index) / TNT_PER_CIRCUMFERENCE;
                    double spawnX = targetCenter.x + Math.cos(angle) * radius;
                    double spawnZ = targetCenter.z + Math.sin(angle) * radius;
                    double orbitalAngle = angle + (layer * 0.35D) + (ring * 0.2D);
                    double tangentialX = -Math.sin(orbitalAngle) * 0.08D;
                    double tangentialZ = Math.cos(orbitalAngle) * 0.08D;
                    double inwardX = (targetCenter.x - spawnX) * 0.015D;
                    double inwardZ = (targetCenter.z - spawnZ) * 0.015D;
                    double fallSpeed = -0.17D - (layer * 0.004D);

                    TntEntity tnt = new TntEntity(world, spawnX, spawnY, spawnZ, owner);
                    tnt.setFuse(TNT_FUSE_TICKS + EXTRA_EXPLOSION_DELAY_TICKS);
                    tnt.setVelocity(tangentialX + inwardX, fallSpeed, tangentialZ + inwardZ);
                    world.spawnEntity(tnt);
                }
            }
        }
    }
}
