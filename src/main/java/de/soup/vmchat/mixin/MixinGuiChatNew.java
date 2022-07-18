package de.soup.vmchat.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.soup.vmchat.VMChat;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NewChatGui.class)
public abstract class MixinGuiChatNew {

    @Inject(method = "render",at = @At("HEAD"))
    public void drawSecondChat(MatrixStack matrix, int updates, CallbackInfo _c){VMChat.getVars().getSecondChat().draw(matrix,updates);}

    @Inject(method = "scrollChat", at = @At("HEAD"))
    public void scroll(double amount, CallbackInfo _c) { VMChat.getVars().getSecondChat().scroll((int)amount); }

    @Inject(method = "clearMessages", at = @At("HEAD"))
    public void clearChatMessages(CallbackInfo _c) {
        VMChat.getVars().getSecondChat().clear();
    }

    @ModifyVariable(method = "addMessage(Lnet/minecraft/util/text/ITextComponent;)V", at = @At("HEAD"), argsOnly = true, index = 1)
    public ITextComponent setChatLine(ITextComponent chatComponent) {
        return chatComponent;
    }

    @ModifyConstant(method = "addMessage(Lnet/minecraft/util/text/ITextComponent;IIZ)V")
    public int maxLines(int maxIn) {
        if(maxIn == 100) {
            return VMChat.getGeneralConfig().getSettings().maxChatLines;
        }
        return maxIn;
    }
}
