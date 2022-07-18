package de.soup.vmchat.gui.chat.renderer;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.soup.vmchat.VMChat;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.RenderComponentsUtil;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.LogManager;

import java.util.List;

public class SecondChat extends AbstractGui {

    private final List<String> recentMessages = Lists.newArrayList();
    private final List<ChatLine<ITextComponent>> allMessages = Lists.newArrayList();
    private final List<ChatLine<IReorderingProcessor>> singleMessages = Lists.newArrayList();
    private int scrollPos;
    private boolean isScrolled;

    public void draw(MatrixStack matrix, int updates)
    {
        if(VMChat.getGeneralConfig().getSettings().isSecondChatEnabled)
        {
            int lineCount = this.getLineCount();
            double opacity = VMChat.getGeneralConfig().getSettings().secondChatOpacity;
            if(lineCount > 0)
            {
                boolean chatOpened = this.isChatOpened();

                double chatScale = VMChat.getGeneralConfig().getSettings().secondChatScale;
                int width = MathHelper.ceil((float) this.getWidth() / chatScale);
                RenderSystem.pushMatrix();
                RenderSystem.translatef((float) (VMChat.getVars().getClient().getWindow().getGuiScaledWidth() - getWidth() - 6.0f * chatScale), 0.0F, 0.0F);
                RenderSystem.scaled(chatScale,chatScale,1.0F);
                int lineIndex;
                int ticks;
                int alpha;

                for(lineIndex = 0;lineIndex+this.scrollPos < this.singleMessages.size() && lineIndex < lineCount;lineIndex++)
                {
                    ChatLine<IReorderingProcessor> message = this.singleMessages.get(lineIndex + this.scrollPos);
                    if(message != null)
                    {
                        ticks = updates - message.getUpdateCounter();
                        if(ticks < 200 || chatOpened)
                        {
                            double e = (double) ticks / 200.00;
                            e = 1.00 - e;
                            e *= 10.00;
                            e = MathHelper.clamp(e,0.0D,1.0D);
                            e *= e;
                            alpha = (chatOpened) ? 255 : (int) (255.0D*e);
                            alpha = (int) ((float) alpha * opacity);
                            if(alpha > 3)
                            {
                                int x = 0;
                                int y = (8 - (lineIndex * 9));

                                fill(matrix,x, y - 9, x + width + MathHelper.floor(4f * chatScale), y, alpha / 2 << 24);
                                RenderSystem.enableBlend();
                                VMChat.getVars().getClient().font.draw(matrix, message.getMessage(), x, y - 8, 16777215 + (alpha << 24));
                                RenderSystem.disableAlphaTest();
                                RenderSystem.disableBlend();
                            }
                        }
                    }
                }
                RenderSystem.popMatrix();
            }
        }
    }

    public void addMessage(ITextComponent message){ this.addMessage(message,0); }
    public void addMessage(ITextComponent message, int id)
    {
        LogManager.getLogger().info("[CHAT2] {}", message.getString());
        this.addMessage(message,id,VMChat.getVars().getClient().gui.getGuiTicks(),false);
    }

    public void addMessage(ITextComponent message, int id, int updates, boolean refresh)
    {
        if(id != 0) this.deleteMessage(id);

        int messageWidth = MathHelper.floor((float) this.getWidth() / VMChat.getGeneralConfig().getSettings().secondChatScale);
        List<IReorderingProcessor> messages = RenderComponentsUtil.wrapComponents(message,messageWidth,VMChat.getVars().getClient().font);

        for(IReorderingProcessor msg : messages)
        {
            if(this.isChatOpened() && this.scrollPos > 0)
            {
                this.isScrolled = true;
                this.scroll(1);
            }
            this.singleMessages.add(0,new ChatLine<>(updates,msg,id));
        }

        while (this.singleMessages.size() > VMChat.getGeneralConfig().getSettings().maxChatLines)
        {
            this.singleMessages.remove(this.singleMessages.size() -1);
        }
        if(!refresh)
        {
            this.allMessages.add(0,new ChatLine<>(updates,message,id));
            while (this.allMessages.size() > VMChat.getGeneralConfig().getSettings().maxChatLines)
            {
                this.allMessages.remove(this.allMessages.size()-1);
            }
        }
    }

    public void clear()
    {
        allMessages.clear();
        singleMessages.clear();
        recentMessages.clear();
        resetScroll();
    }

    public void resetScroll()
    {
        this.scrollPos = 0;
        this.isScrolled = false;
    }

    public void scroll(int scrollAmount)
    {
        this.scrollPos += scrollAmount;
        int messages = this.singleMessages.size();
        if(this.scrollPos > messages - this.getLineCount()) this.scrollPos = messages - this.getLineCount();

        if(this.scrollPos <= 0)
        {
            this.scrollPos = 0;
            this.isScrolled = false;
        }
    }

    public void deleteMessage(int id)
    {
        this.singleMessages.removeIf((line)->line.getId() == id);
        this.allMessages.removeIf((line)-> line.getId() == id);
    }

    public boolean isChatOpened(){ return VMChat.getVars().getClient().screen instanceof ChatScreen; }
    public int getHeight(){ return MathHelper.floor(this.isChatOpened() ? VMChat.getGeneralConfig().getSettings().secondChatFocusedHeight : VMChat.getGeneralConfig().getSettings().secondChatUnFocusedHeight); }
    public int getWidth(){ return MathHelper.floor(VMChat.getGeneralConfig().getSettings().secondChatWidth); }
    public int getLineCount(){ return this.getHeight() / 9; }
}
