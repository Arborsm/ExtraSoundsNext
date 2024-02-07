package org.arbor.extrasounds.mixin.chat;

import org.arbor.extrasounds.misc.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public abstract class ChatHudMixin {
    @Shadow
    private int chatScrollbarPos;
    @Shadow
    private @Final Minecraft minecraft;

    @Unique
    private int extra_sounds$currentLines;

    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;IIZ)V", at = @At("RETURN"))
    private void extrasounds$receiveMessage(Component p_93791_, int p_93792_, int p_93793_, boolean p_93794_, CallbackInfo ci) {
        final LocalPlayer player = this.minecraft.player;
        if (player == null || p_93794_) {
            return;
        }

        String msg = p_93791_.getString();
        if (msg.contains("@" + player.getName().getString()) || msg.contains("@" + player.getDisplayName().getString())) {
            SoundManager.playSound(Sounds.CHAT_MENTION, SoundType.CHAT_MENTION);
        } else {
            SoundManager.playSound(Sounds.CHAT, SoundType.CHAT);
        }
    }

    @Inject(method = "scrollChat", at = @At("RETURN"))
    private void extrasounds$onScroll(int amount, CallbackInfo ci) {
        if (this.chatScrollbarPos != this.extra_sounds$currentLines) {
            SoundManager.playSound(Sounds.INVENTORY_SCROLL, SoundType.CHAT);
            this.extra_sounds$currentLines = this.chatScrollbarPos;
        }
    }
}
