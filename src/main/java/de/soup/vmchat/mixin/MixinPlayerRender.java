package de.soup.vmchat.mixin;

import de.soup.vmchat.VMChat;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingRenderer.class)
public class MixinPlayerRender {

    @Inject(method = "shouldShowName(Lnet/minecraft/entity/Entity;)Z", at= @At("HEAD"), cancellable = true)
    public void canRenderName(Entity entity, CallbackInfoReturnable<Boolean> c) {
        if(entity.is(VMChat.getVars().getClientPlayer()) && VMChat.getGeneralConfig().getSettings().renderOwnName) {
            c.setReturnValue(true);
        }
    }
}
