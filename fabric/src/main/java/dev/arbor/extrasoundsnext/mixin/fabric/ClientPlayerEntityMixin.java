package dev.arbor.extrasoundsnext.mixin.fabric;

import com.mojang.authlib.GameProfile;
import dev.arbor.extrasoundsnext.sounds.Mixers;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * For Hotbar drop action.
 */
@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin extends Player {
    public ClientPlayerEntityMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Inject(method = "drop", at = @At("RETURN"))
    private void extrasounds$hotbarItemDrop(boolean fullStack, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = this.getInventory().getSelected();
        if (!itemStack.isEmpty()) {
            SoundManager.playThrow(itemStack, Mixers.HOTBAR);
        }
    }
}
