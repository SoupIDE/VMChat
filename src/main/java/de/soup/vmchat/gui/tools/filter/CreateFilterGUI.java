package de.soup.vmchat.gui.tools.filter;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.soup.vmchat.VMChat;
import de.soup.vmchat.config.configs.filter.elements.ChatFilters;
import de.soup.vmchat.config.configs.filter.elements.FilterType;
import de.soup.vmchat.gui.tools.macros.SelectMacroGUI;
import de.soup.vmchat.util.MessageBus;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.stream.Collectors;

public class CreateFilterGUI extends Screen {

    private ChatFilters.ChatFilter preFilter;

    private boolean isEdit = false;
    private String errorMessage = "";

    private TextFieldWidget filterNameField;
    private TextFieldWidget filterPatternField;
    private FilterType filterTypeButton;


    protected CreateFilterGUI(ITextComponent title) {
        super(title);
    }

    protected CreateFilterGUI(ITextComponent title, ChatFilters.ChatFilter preFilter)
    {
        super(title);
        this.preFilter = preFilter;
        this.isEdit = true;
    }

    @Override
    protected void init()
    {
        super.init();

        VMChat.getVars().getClient().keyboardHandler.setSendRepeatsToGui(true);
        filterNameField = new TextFieldWidget(font, this.width/2-150, this.height/2-70, 300, 20, new StringTextComponent(""));
        filterPatternField = new TextFieldWidget(font, this.width/2-150, this.height/2-30, 300, 20, new StringTextComponent(""));
        filterNameField.setMaxLength(20);
        filterPatternField.setMaxLength(120);

        this.children.add(filterNameField);
        this.children.add(filterPatternField);
        this.setInitialFocus(filterNameField);

        addButton(new Button(this.width/2-100,this.height/2,60,20,new StringTextComponent(FilterType.IGNORE.toString()),(b)->{toggleFilterTypeButtons(FilterType.IGNORE);}));
        addButton(new Button(this.width/2-28,this.height/2,60,20,new StringTextComponent(FilterType.SECOND_CHAT.toString()),(b)->{toggleFilterTypeButtons(FilterType.SECOND_CHAT);}));
        addButton(new Button(this.width/2+40,this.height/2,70,20,new StringTextComponent(FilterType.COPY_SECOND_CHAT.toString()),(b)->{toggleFilterTypeButtons(FilterType.COPY_SECOND_CHAT);}));
        addButton(new Button(this.width/2-150,this.height-32,150,20,new StringTextComponent("Cancel"),(b)->{VMChat.getVars().getClient().setScreen(new SelectFilterGUI(new StringTextComponent("Filter")));}));
        addButton(new Button(this.width/2+20,this.height-32,150,20,new StringTextComponent("Done"),(b)->{saveFilter();}));

        if(isEdit)
        {
            filterNameField.setValue(this.preFilter.getName());
            filterPatternField.setValue(this.preFilter.getPatternUnformatted());

            toggleFilterTypeButtons(this.preFilter.getType());
        }

    }

    @Override
    public void render(MatrixStack matrix, int x, int y, float ticks)
    {
        renderBackground(matrix);
        super.render(matrix, x, y, ticks);

        buttons.forEach(b->b.visible=true);

        drawString(matrix,font, MessageBus.parseTextFormattingString("&f&l&nName:"),this.width/2-20, this.height/2-70-font.lineHeight-4,1);
        filterNameField.render(matrix,x,y,ticks);
        drawString(matrix,font,MessageBus.parseTextFormattingString("&f&l&nPattern:"),this.width/2-25, this.height/2-30-font.lineHeight-4,1);
        filterPatternField.render(matrix,x,y,ticks);

        if(!errorMessage.isEmpty()) drawString(matrix,font, TextFormatting.DARK_RED+errorMessage,2, font.lineHeight-5,1);
    }

    private void toggleFilterTypeButtons(FilterType selected)
    {
        List<Widget> typeButtons = buttons.stream().filter(b->b.getWidth() < 100).collect(Collectors.toList());

        for(Widget typeButton : typeButtons)
        {
            if(typeButton.isFocused() && typeButton.getMessage().getString().equals(selected.toString()))
            {
                typeButton.changeFocus(true);
                filterTypeButton = null;
                return;
            }

            if(typeButton.getMessage().getString().equals(selected.toString()))
            {
                filterTypeButton = selected;
                typeButton.changeFocus(true);
            }else
            {
                if(typeButton.isFocused()) typeButton.changeFocus(true);
            }
        }
    }

    private void saveFilter()
    {
        if(filterNameField.getValue().isEmpty() || filterPatternField.getValue().isEmpty() || filterTypeButton == null)
        {
            errorMessage = "Please fill all fields before saving!";
            return;
        }

        String filterName = filterNameField.getValue();
        String filterPattern = filterPatternField.getValue();

        ChatFilters config = VMChat.getFilterConfig().getSettings();

        if(config.isFilterSaved(filterName) && !isEdit || isEdit && !filterName.equalsIgnoreCase(preFilter.getName()) && config.isFilterSaved(filterName))
        {
            errorMessage = "Please don't use already taken filter names!";
            return;
        }

        if(isEdit)
        {
            preFilter.setName(filterName);
            preFilter.setPattern(filterPattern);
            preFilter.setType(filterTypeButton);
        }
        else config.getChatFilters().add(new ChatFilters.ChatFilter(filterName,filterPattern,filterTypeButton));

        VMChat.getFilterConfig().save();
        VMChat.getVars().getClient().setScreen(new SelectFilterGUI(new StringTextComponent("select")));
    }

    @Override
    public boolean mouseClicked(double x, double y, int p_231044_5_)
    {
        filterNameField.setFocus(filterNameField.isMouseOver(x,y));
        filterPatternField.setFocus(filterPatternField.isMouseOver(x,y));

        return super.mouseClicked(x, y, p_231044_5_);
    }

    @Override
    public boolean charTyped(char p_231042_1_, int p_231042_2_)
    {
        if(filterNameField.isFocused()) filterNameField.charTyped(p_231042_1_, p_231042_2_);
        else if(filterPatternField.isFocused())filterPatternField.charTyped(p_231042_1_,p_231042_2_);

        return false;
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_)
    {
        if(p_231046_1_ == 256) VMChat.getVars().getClient().setScreen(new SelectFilterGUI(new StringTextComponent("select")));

        if(filterNameField.isFocused())
        {
            if(p_231046_1_ == 258)
            {
                filterNameField.setFocus(false);
                filterPatternField.setFocus(true);
            }
            else return filterNameField.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
        }
        else if(filterPatternField.isFocused())
        {
            if(p_231046_1_ == 258)
            {
                filterPatternField.setFocus(false);
                filterNameField.setFocus(true);
            }
            else return filterPatternField.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
        }

        return false;
    }
}
