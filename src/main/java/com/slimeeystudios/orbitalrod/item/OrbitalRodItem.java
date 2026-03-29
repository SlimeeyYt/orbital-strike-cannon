package com.slimeeystudios.orbitalrod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class OrbitalRodItem extends FishingRodItem {
    private static final int USE_COOLDOWN_TICKS = 108;
    private static final double MAX_TARGET_DISTANCE = 108.0D;
    private static final double BASE_SPAWN_HEIGHT = 108.0D;
    private static final int LAYER_COUNT = 9;
    private static final int CIRCUMFERENCE_COUNT = 9;
    private static final int TNT_PER_CIRCUMFERENCE = 48;
    private static final double BASE_CIRCUMFERENCE_RADIUS = 12.0D;
    private static final double CIRCUMFERENCE_RADIUS_STEP = 24.0D;
    private static final double LAYER_VERTICAL_STEP = 9.0D;
    private static final int TNT_FUSE_TICKS = 96;
    private static final int EXTRA_EXPLOSION_DELAY_TICKS = 108;

    public OrbitalRodItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        if (user.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.FAIL;
        }

        HitResult hitResult = user.pick(MAX_TARGET_DISTANCE, 0.0F, false);
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return super.use(level, user, hand);
        }

        if (!level.isClientSide()) {
            BlockPos targetPos = ((BlockHitResult) hitResult).getBlockPos();
            Vec3 targetCenter = Vec3.atCenterOf(targetPos);
            ServerLevel serverLevel = (ServerLevel) level;

            spawnActivationParticles(serverLevel, targetCenter);
            spawnOrbitalStrike(serverLevel, targetCenter, user);
            serverLevel.playSound(null, targetPos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.2F, 0.6F);

            stack.hurtAndBreak(1, user, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        }

        user.getCooldowns().addCooldown(stack, USE_COOLDOWN_TICKS);
        user.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.SUCCESS;
    }

    private void spawnActivationParticles(ServerLevel level, Vec3 targetCenter) {
        level.sendParticles(ParticleTypes.SMOKE, targetCenter.x, targetCenter.y + 0.1D, targetCenter.z, 24, 0.4D, 0.15D, 0.4D, 0.01D);
        level.sendParticles(ParticleTypes.FLAME, targetCenter.x, targetCenter.y + 0.1D, targetCenter.z, 16, 0.25D, 0.1D, 0.25D, 0.01D);
    }

    private void spawnOrbitalStrike(ServerLevel level, Vec3 targetCenter, Player owner) {
        for (int layer = 0; layer < LAYER_COUNT; layer++) {
            double spawnY = targetCenter.y + BASE_SPAWN_HEIGHT + (layer * LAYER_VERTICAL_STEP);
            double centerFallSpeed = -0.17D - (layer * 0.004D);

            PrimedTnt centerTnt = new PrimedTnt(level, targetCenter.x, spawnY, targetCenter.z, owner);
            centerTnt.setFuse(TNT_FUSE_TICKS + EXTRA_EXPLOSION_DELAY_TICKS);
            centerTnt.setDeltaMovement(0.0D, centerFallSpeed, 0.0D);
            level.addFreshEntity(centerTnt);

            for (int ring = 0; ring < CIRCUMFERENCE_COUNT; ring++) {
                double radius = BASE_CIRCUMFERENCE_RADIUS + (ring * CIRCUMFERENCE_RADIUS_STEP);

                for (int index = 0; index < TNT_PER_CIRCUMFERENCE; index++) {
                    double angle = (Math.PI * 2.0D * index) / TNT_PER_CIRCUMFERENCE;
                    double spawnX = targetCenter.x + Math.cos(angle) * radius;
                    double spawnZ = targetCenter.z + Math.sin(angle) * radius;
                    double orbAngle = angle + (layer * 0.35D) + (ring * 0.2D);
                    double tangentialX = -Math.sin(orbAngle) * 0.08D;
                    double tangentialZ = Math.cos(orbAngle) * 0.08D;
                    double inwardX = (targetCenter.x - spawnX) * 0.015D;
                    double inwardZ = (targetCenter.z - spawnZ) * 0.015D;
                    double fallSpeed = -0.17D - (layer * 0.004D);

                    PrimedTnt tnt = new PrimedTnt(level, spawnX, spawnY, spawnZ, owner);
                    tnt.setFuse(TNT_FUSE_TICKS + EXTRA_EXPLOSION_DELAY_TICKS);
                    tnt.setDeltaMovement(tangentialX + inwardX, fallSpeed, tangentialZ + inwardZ);
                    level.addFreshEntity(tnt);
                }
            }
        }
    }
}
