package org.arbor.extrasounds.mixin.chat;

import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.arbor.extrasounds.sounds.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;
import org.jetbrains.annotations.Nullable;
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

    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V", at = @At("RETURN"))
    private void extrasounds$receiveMessage(Component message, @Nullable MessageSignature signature, int ticks, @Nullable GuiMessageTag indicator, boolean refresh, CallbackInfo ci) {
        final LocalPlayer player = this.minecraft.player;
        if (player == null || refresh) {
            return;
        }

        String msg = message.getString();
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
