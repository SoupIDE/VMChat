package de.soup.vmchat.gui.tools.macros;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.soup.vmchat.VMChat;
import de.soup.vmchat.config.configs.macro.elements.Macros;
import de.soup.vmchat.util.Keyboard;
import de.soup.vmchat.util.MessageBus;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class CreateMacroGUI extends Screen
{

    private boolean isChoosingKey = false;
    private String errorMessage = "";

    private Macros.Macro preMacro;
    private boolean isEdit;

    private TextFieldWidget nameField;
    private TextFieldWidget messageField;
    private Button macroButton;
    private Button addShiftButton;
    private Button addAltButton;
    private Button addCtrlButton;
    private Button autoSendButton;
    private Keyboard.Key chosenKey;

    public CreateMacroGUI(ITextComponent title) { super(title); }

    public CreateMacroGUI(ITextComponent title, Macros.Macro preMacro)
    {
        super(title);
        this.preMacro = preMacro;
        this.isEdit = true;
    }

    @Override
    protected void init()
    {
        super.init();
        VMChat.getVars().getClient().keyboardHandler.setSendRepeatsToGui(true);

        nameField = new TextFieldWidget(font, this.width/2-150, this.height/2-110, 300, 20, new StringTextComponent(""));
        messageField = new TextFieldWidget(font, this.width/2-150, this.height/2-70, 300, 20, new StringTextComponent(""));

        messageField.setMaxLength(230);
        nameField.setMaxLength(20);

        this.children.add(messageField);
        this.children.add(nameField);

        this.setInitialFocus(nameField);

        macroButton = new Button( this.width/2-150, this.height/2-30, 300, 20, new StringTextComponent("Click to enter key"),(b)->
        {
            b.changeFocus(true);
            b.setMessage(new StringTextComponent(""));
            isChoosingKey = true;
        });
        addShiftButton = new Button(this.width/2-60, this.height/2+10, 20, 20, new StringTextComponent(""),(b)->
        {
            b.changeFocus(true);
            b.setMessage(new StringTextComponent((b.isFocused()) ? "X" : ""));
        });
        addAltButton = new Button(this.width/2-60, this.height/2+40, 20, 20, new StringTextComponent(""),(b)->
        {
            b.changeFocus(true);
            b.setMessage(new StringTextComponent((b.isFocused()) ? "X" : ""));
        });
        addCtrlButton = new Button(this.width/2-60, this.height/2+70, 20, 20, new StringTextComponent(""),(b)->
        {
            b.changeFocus(true);
            b.setMessage(new StringTextComponent((b.isFocused()) ? "X" : ""));
        });
        autoSendButton = new Button(this.width/2+114, this.height/2+40, 20, 20, new TranslationTextComponent(""),(b)->
        {
            b.changeFocus(true);
            b.setMessage(new StringTextComponent((b.isFocused()) ? "X" : ""));
        });

        addButton(macroButton);
        addButton(addAltButton);
        addButton(addShiftButton);
        addButton(addCtrlButton);
        addButton(autoSendButton);

        if(isEdit)
        {
            nameField.setValue(preMacro.getName());
            messageField.setValue(preMacro.getMessage());
            macroButton.setMessage(new StringTextComponent(preMacro.getKeys().get(0).getName().toUpperCase()));
            chosenKey = preMacro.getKeys().get(0);

            if(preMacro.hasAddition(340)) addShiftButton.onPress();
            if(preMacro.hasAddition(341)) addCtrlButton.onPress();
            if(preMacro.hasAddition(342)) addAltButton.onPress();
            if(preMacro.isAutoSend()) autoSendButton.onPress();
        }

        addButton(new Button(this.width/2-160,this.height-32,150,20,new StringTextComponent("Cancel"),(b)->{VMChat.getVars().getClient().setScreen(new SelectMacroGUI(new StringTextComponent("select")));}));
        addButton(new Button(this.width/2+10,this.height-32,150,20,new StringTextComponent("Done"),(b)->{saveMacro();}));
    }

    @Override
    public void render(MatrixStack matrix, int x, int y, float ticks)
    {
        renderBackground(matrix);
        super.render(matrix, x, y, ticks);

        buttons.forEach(b->b.visible=true);
        nameField.render(matrix,x,y,ticks);
        messageField.render(matrix, x, y, ticks);

        drawString(matrix,font, MessageBus.parseTextFormattingString("&f&l&nName:"),this.width/2-14,this.height/2-125,1);
        drawString(matrix,font, MessageBus.parseTextFormattingString("&f&l&nMessage:"),this.width/2-23,this.height/2-84,1);

        drawString(matrix,font, TextFormatting.WHITE+"+ Shift",this.width/2-130,this.height/2+16,0);
        drawString(matrix,font,TextFormatting.WHITE+"+ Alt",this.width/2-130,this.height/2+46,0);
        drawString(matrix,font,TextFormatting.WHITE+"+ Ctrl",this.width/2-130,this.height/2+76,0);
        drawString(matrix,font,TextFormatting.WHITE+"AutoSend",this.width/2+30, this.height/2+46,0);

        if(!errorMessage.isEmpty()) drawString(matrix,font, TextFormatting.DARK_RED+errorMessage,2, font.lineHeight-5,1);
    }

    private void saveMacro()
    {
        if(messageField.getValue().isEmpty() || macroButton.getMessage().getString().equals("") || chosenKey == null)
        {
            errorMessage = "Please fill all fields before saving!";
            return;
        }

        Macros config = VMChat.getMacroConfig().getSettings();

        String name = nameField.getValue();
        String message = messageField.getValue();
        boolean isShiftButtonAdded = addShiftButton.isFocused();
        boolean isAltButtonAdded = addAltButton.isFocused();
        boolean isCtrlButtonAdded = addCtrlButton.isFocused();
        boolean isAutoSend = autoSendButton.isFocused();
        List<Keyboard.Key> keybinds = new ArrayList<>();

        keybinds.add(chosenKey);
        if(isAltButtonAdded) keybinds.add(new Keyboard.Key(InputMappings.getKey(342,56)));
        if(isShiftButtonAdded) keybinds.add(new Keyboard.Key(InputMappings.getKey(340,42)));
        if(isCtrlButtonAdded) keybinds.add(new Keyboard.Key(InputMappings.getKey(341,29)));

        if(!isEdit && config.isMacroKeySaved(keybinds) || !isEdit && config.isMacroNameSaved(name)
        || isEdit && !name.equalsIgnoreCase(preMacro.getName()) && config.isMacroNameSaved(name)
        || isEdit && !keybinds.toString().equals(preMacro.getKeys().toString()) && config.isMacroKeySaved(keybinds))
        {
            errorMessage = "Please don't use already taken keybinds or macro names!";
            return;
        }

        if(isEdit)
        {
            preMacro.setAutoSend(isAutoSend);
            preMacro.setMessage(message);
            preMacro.setName(name);

            List<Keyboard.Key> editKeys = new ArrayList<>();
            editKeys.add(chosenKey);
            if(isAltButtonAdded) editKeys.add(new Keyboard.Key(InputMappings.getKey(342,56)));
            if(isShiftButtonAdded) editKeys.add(new Keyboard.Key(InputMappings.getKey(340,42)));
            if(isCtrlButtonAdded) editKeys.add(new Keyboard.Key(InputMappings.getKey(341,29)));

            preMacro.setKeys(editKeys);

        }
        else config.getMacros().add(new Macros.Macro(name,message,keybinds,isAutoSend));

        VMChat.getMacroConfig().save();
        VMChat.getVars().getClient().setScreen(new SelectMacroGUI(new StringTextComponent("select")));
    }

    @Override
    public boolean charTyped(char p_231042_1_, int p_231042_2_)
    {
        if(messageField.isFocused()) return messageField.charTyped(p_231042_1_, p_231042_2_);
        return super.charTyped(p_231042_1_, p_231042_2_);
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_)
    {
        if(p_231046_1_ == -1 ) return false;
        if(p_231046_1_ == 256) VMChat.getVars().getClient().setScreen(new SelectMacroGUI(new StringTextComponent("select")));

        if(isChoosingKey)
        {
            chosenKey = new Keyboard.Key(InputMappings.getKey(p_231046_1_,p_231046_2_));
            isChoosingKey = false;
            macroButton.setMessage(new StringTextComponent(chosenKey.getName().toUpperCase()));
            macroButton.changeFocus(true);
            return false;
        }

        if(messageField.isFocused())
        {
            if(p_231046_1_ == 258)
            {
                messageField.setFocus(false);
                nameField.setFocus(true);
            }
            else return messageField.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
        }
        else if(nameField.isFocused())
        {
            if(p_231046_1_ == 258)
            {
                nameField.setFocus(false);
                messageField.setFocus(true);
            }
            else return nameField.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
        }

        return false;
    }

    @Override
    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_)
    {
        if(isChoosingKey && !macroButton.isMouseOver(p_231044_1_,p_231044_3_))
        {
            isChoosingKey = false;
            macroButton.setMessage(new StringTextComponent("Click to enter key"));
            macroButton.changeFocus(true);
            this.chosenKey = null;
        }

        nameField.setFocus(nameField.isMouseOver(p_231044_1_,p_231044_3_));
        messageField.setFocus(messageField.isMouseOver(p_231044_1_,p_231044_3_));

        return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
    }
}

