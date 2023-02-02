package de.soup.vmchat.gui.chat.settings;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.soup.vmchat.VMChat;
import de.soup.vmchat.config.configs.general.elements.GeneralSettings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.Slider;

public class ChatSettingsGUI extends Screen {

    private final GeneralSettings config = VMChat.getGeneralConfig().getSettings();

    public ChatSettingsGUI(ITextComponent title) {
        super(title);
    }

    @Override
    protected void init()
    {
        super.init();

        addButton(new Button(width/2-100,height-30,200,20,new StringTextComponent("Done"),(b)-> VMChat.getVars().getClientPlayer().closeContainer()));
        addButton(new Button(width/2-135,50,130,20,new StringTextComponent("Visible: "+((config.isSecondChatEnabled) ? "On" : "Off")),(b)-> {
            config.isSecondChatEnabled = !config.isSecondChatEnabled;
            VMChat.getGeneralConfig().save();
            b.setMessage(new StringTextComponent("Visible: "+((config.isSecondChatEnabled) ? "On" : "Off")));
        }));

        this.children.add(new Slider(width/2+5,50,130,20,new StringTextComponent("Opacity: "),new StringTextComponent("%"),10,100,config.secondChatOpacity*100,false,true,(p)->{}));
        this.children.add(new Slider(width/2-135,80,130,20,new StringTextComponent("Scale: "),new StringTextComponent("%"),1,100,config.secondChatScale*100,false,true,(p)->{}));
        this.children.add(new Slider(width/2+5,80,130,20,new StringTextComponent("Focused height: "),new StringTextComponent("px"),20,180,config.secondChatFocusedHeight,false,true,(p)->{}));
        this.children.add(new Slider(width/2-135,110,130,20,new StringTextComponent("Unfocused height: "),new StringTextComponent("px"),20,180,config.secondChatUnFocusedHeight,false,true,(p)->{}));
        this.children.add(new Slider(width/2+5,110,130,20,new StringTextComponent("Width: "),new StringTextComponent("px"),40,320,config.secondChatWidth,false,true,(p)->{}));
        this.children.add(new Slider(width/2-65,140,130,20,new StringTextComponent("Chat Limit: "),new StringTextComponent(" Lines"),50,1000,config.maxChatLines,false,true,(p)->{}));
    }

    @Override
    public void render(MatrixStack matrix, int x, int y, float tick)
    {
        renderBackground(matrix);
        super.render(matrix, x, y, tick);

        buttons.forEach(b->b.visible = true);
        children.stream().filter(w->w instanceof Slider).forEach(slider->((Slider) slider).render(matrix,x,y,tick));

    }

    @Override
    public boolean mouseReleased(double x, double y, int p_231048_5_)
    {
        if(children.stream().filter(c->c instanceof Slider).anyMatch(slider->slider.isMouseOver(x,y)))
        {
            Slider clickedSlider = (Slider) children.stream()
                    .filter(c->c instanceof Slider)
                    .filter(slider->slider.isMouseOver(x,y)
                    ).findFirst().orElse(null);

            super.mouseReleased(x, y, p_231048_5_);

            saveNewConfigValues(clickedSlider);
            return false;
        }
        return super.mouseReleased(x, y, p_231048_5_);
    }

    @Override
    public boolean mouseDragged(double x, double y, int p_231045_5_, double p_231045_6_, double p_231045_8_)
    {
        if(children.stream().filter(c -> c instanceof Slider).anyMatch(slider -> (((Slider) slider).dragging)))
        {
            Slider draggedSlider = (Slider) children.stream()
                    .filter(c -> c instanceof Slider)
                    .filter(slider -> (((Slider) slider).dragging)
                    ).findFirst().orElse(null);

            if(children.stream().filter(c->c instanceof Slider).anyMatch(slider->slider.isMouseOver(x,y))) saveNewConfigValues(draggedSlider);
            else draggedSlider.mouseReleased(x,y,p_231045_5_);
        }
        return super.mouseDragged(x, y, p_231045_5_, p_231045_6_, p_231045_8_);
    }

    private void saveNewConfigValues(Slider tempSlider)
    {
        double newValue = tempSlider.getValue();
        switch (tempSlider.dispString.getString())
        {
            case "Opacity: ": config.secondChatOpacity = (float) (newValue/100.0);break;
            case "Scale: ": config.secondChatScale = (float) (newValue/100.0);break;
            case "Focused height: ": config.secondChatFocusedHeight = ((int)newValue);break;
            case "Unfocused height: ": config.secondChatUnFocusedHeight = ((int)newValue);break;
            case "Width: ": config.secondChatWidth = ((int)newValue);
            case "Chat Limit: ": config.maxChatLines = ((int) newValue);break;
        }
        VMChat.getGeneralConfig().save();
    }
}

