package de.soup.vmchat.mixin;

import de.soup.vmchat.VMChat;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class MixinGuiChat {
    @Inject(method = "removed", at = @At("HEAD"))
    public void onGuiClosed(CallbackInfo _ci) {
        VMChat.getVars().getSecondChat().resetScroll();
    }
}
