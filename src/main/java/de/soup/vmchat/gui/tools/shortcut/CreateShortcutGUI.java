package de.soup.vmchat.gui.tools.shortcut;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.soup.vmchat.VMChat;
import de.soup.vmchat.config.configs.shortcut.elements.Shortcuts;
import de.soup.vmchat.util.MessageBus;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CreateShortcutGUI extends Screen {

    private Shortcuts.Shortcut preShortcut;
    private boolean isEdit = false;

    private TextFieldWidget nameField;
    private TextFieldWidget shortcutField;
    private TextFieldWidget replacementField;

    private Button ignoreCommandsButton;
    private Button autoSendButton;

    private String errorMessage = "";

    protected CreateShortcutGUI(ITextComponent p_i51108_1_) { super(p_i51108_1_); }
    protected CreateShortcutGUI(ITextComponent title, Shortcuts.Shortcut preShortcut)
    {
        super(title);
        this.preShortcut = preShortcut;
        this.isEdit = true;
    }

    @Override
    protected void init()
    {
        super.init();
        VMChat.getVars().getClient().keyboardHandler.setSendRepeatsToGui(true);

        nameField = new TextFieldWidget(font, this.width/2-150, this.height/2-70, 125, 20, new StringTextComponent(""));
        shortcutField = new TextFieldWidget(font, this.width/2-15, this.height/2-70, 165, 20, new StringTextComponent(""));
        replacementField = new TextFieldWidget(font, this.width/2-150, this.height/2-30, 300, 20, new StringTextComponent(""));

        replacementField.setMaxLength(230);
        nameField.setMaxLength(20);
        shortcutField.setMaxLength(30);

        this.children.add(nameField);
        this.children.add(shortcutField);
        this.children.add(replacementField);

        this.setInitialFocus(nameField);

        ignoreCommandsButton = new Button(this.width/2+50, this.height/2+6, 20, 20, new StringTextComponent(""),(b)->
        {
            b.changeFocus(true);
            b.setMessage(new StringTextComponent((b.isFocused()) ? "X" : ""));
        });
        autoSendButton = new Button(this.width/2+50, this.height/2+34, 20, 20, new StringTextComponent(""),(b)->
        {
            b.changeFocus(true);
            b.setMessage(new StringTextComponent((b.isFocused()) ? "X" : ""));
        });

        addButton(ignoreCommandsButton);
        addButton(autoSendButton);

        if(isEdit)
        {
            nameField.setValue(preShortcut.getName());
            shortcutField.setValue(preShortcut.getShortcut());
            replacementField.setValue(preShortcut.getReplacement());

            if(preShortcut.isIgnoreCommands()) ignoreCommandsButton.onPress();
            if(preShortcut.isAutoSend()) autoSendButton.onPress();
        }
        else
        {
            ignoreCommandsButton.onPress();
            autoSendButton.onPress();
        }

        addButton(new Button(this.width/2-160,this.height-32,150,20,new StringTextComponent("Cancel"),(b)->{VMChat.getVars().getClient().setScreen(new SelectShortcutGUI(new StringTextComponent("select")));}));
        addButton(new Button(this.width/2+10,this.height-32,150,20,new StringTextComponent("Done"),(b)->{saveShortcut();}));
    }

    @Override
    public void render(MatrixStack matrix, int x, int y, float ticks)
    {
        renderBackground(matrix);
        super.render(matrix, x, y, ticks);

        buttons.forEach(b->b.visible=true);
        nameField.render(matrix,x,y,ticks);
        shortcutField.render(matrix, x, y, ticks);
        replacementField.render(matrix, x, y, ticks);

        drawString(matrix,font, MessageBus.parseTextFormattingString("&f&l&nName:"),this.width/2-105,this.height/2-85,1);
        drawString(matrix,font, MessageBus.parseTextFormattingString("&f&l&nShortcut:"),this.width/2+40,this.height/2-85,1);
        drawString(matrix,font, MessageBus.parseTextFormattingString("&f&l&nReplacement:"),this.width/2-36,this.height/2-44,1);

        drawString(matrix,font, TextFormatting.WHITE+"IgnoreCommands: ",this.width/2-60,this.height/2+11,0);
        drawString(matrix,font,TextFormatting.WHITE+"AutoSend: ",this.width/2-60,this.height/2+40,0);

        if(!errorMessage.isEmpty()) drawString(matrix,font, TextFormatting.DARK_RED+errorMessage,2, font.lineHeight-5,1);
    }

    private void saveShortcut()
    {
        if(nameField.getValue().isEmpty() || shortcutField.getValue().isEmpty() || replacementField.getValue().isEmpty())
        {
            errorMessage = "Please fill all fields before saving!";
            return;
        }

        Shortcuts config = VMChat.getShortcutConfig().getSettings();

        String name = nameField.getValue();
        String shortcut = shortcutField.getValue();
        String replacement = replacementField.getValue();
        boolean isAutoSend = autoSendButton.isFocused();
        boolean isIgnoreCommands = ignoreCommandsButton.isFocused();

        if(!isEdit && config.isShortcutNameSaved(name) || !isEdit && config.isShortcutShortcut(shortcut)
        || isEdit && !name.equalsIgnoreCase(preShortcut.getName()) && config.isShortcutNameSaved(name)
        || isEdit && !shortcut.equalsIgnoreCase(preShortcut.getShortcut()) && config.isShortcutShortcut(shortcut))
        {
            errorMessage = "Please don't use already taken shortcuts or shortcut names";
            return;
        }

        if(isEdit)
        {
            preShortcut.setName(name);
            preShortcut.setShortcut(shortcut);
            preShortcut.setReplacement(replacement);

            preShortcut.setIgnoreCommands(isIgnoreCommands);
            preShortcut.setAutoSend(isAutoSend);
        }
        else config.getShortcuts().add(new Shortcuts.Shortcut(name,shortcut,replacement,isIgnoreCommands,isAutoSend));

        VMChat.getShortcutConfig().save();
        VMChat.getVars().getClient().setScreen(new SelectShortcutGUI(new StringTextComponent("select")));
    }

    @Override
    public boolean mouseClicked(double x, double y, int p_231044_5_) {
        nameField.setFocus(nameField.isMouseOver(x,y));
        shortcutField.setFocus(shortcutField.isMouseOver(x,y));
        replacementField.setFocus(replacementField.isMouseOver(x,y));
        return super.mouseClicked(x, y, p_231044_5_);
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_)
    {

        if(p_231046_1_ == 256) VMChat.getVars().getClient().setScreen(new SelectShortcutGUI(new StringTextComponent("select")));

        if(nameField.isFocused())
        {
            if(p_231046_1_ == 258)
            {
                nameField.setFocus(false);
                shortcutField.setFocus(true);
            }
            else return nameField.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
        }
        else if(shortcutField.isFocused())
        {
            if(p_231046_1_ == 258)
            {
                shortcutField.setFocus(false);
                replacementField.setFocus(true);
            }
            else return shortcutField.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
        }
        else if(replacementField.isFocused())
        {
            if(p_231046_1_ == 258)
            {
                replacementField.setFocus(false);
                nameField.setFocus(true);
            }
            else return replacementField.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
        }
        return false;
    }

    @Override
    public boolean charTyped(char p_231042_1_, int p_231042_2_) {
        if(nameField.isFocused()) return nameField.charTyped(p_231042_1_,p_231042_2_);
        else if(shortcutField.isFocused()) return shortcutField.charTyped(p_231042_1_,p_231042_2_);
        else return replacementField.charTyped(p_231042_1_,p_231042_2_);
    }
}
