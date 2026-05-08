package com.slimeeystudios.orbitalrod.mixin;

import com.slimeeystudios.orbitalrod.config.ModConfig;
import com.slimeeystudios.orbitalrod.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {
    @Inject(method = "updateResult", at = @At("TAIL"))
    private static void orbitalrod$clearOrbitalCraftingResultWhenDisabled(ScreenHandler handler, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory, CallbackInfo ci) {
        if (world.isClient || ModConfig.isCraftingRecipeEnabled()) {
            return;
        }

        ItemStack output = resultInventory.getStack(0);
        if (output.isOf(ModItems.ORBITAL_TNT_ROD)) {
            resultInventory.setStack(0, ItemStack.EMPTY);
            handler.sendContentUpdates();
        }
    }
}
