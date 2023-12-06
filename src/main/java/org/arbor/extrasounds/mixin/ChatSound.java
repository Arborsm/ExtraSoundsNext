package org.arbor.extrasounds.mixin;

import org.arbor.extrasounds.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;

@Mixin(ChatComponent.class)
public class ChatSound
{
    @Inject(at = @At("RETURN"), method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V")
    private void messageSound(Component message, @Nullable MessageSignature signature, int ticks, @Nullable GuiMessageTag indicator, boolean refresh, CallbackInfo ci)
    {
        if (Minecraft.getInstance().player == null || refresh)
            return;
        String msg = message.getString();
        LocalPlayer p = Minecraft.getInstance().player;
        if (msg.contains("@" + p.getName().getString()) || msg.contains("@" + p.getDisplayName().getString()))
            SoundManager.playSound(Sounds.CHAT_MENTION, SoundType.CHAT_MENTION);
        else
            SoundManager.playSound(Sounds.CHAT, SoundType.CHAT);
    }
}
