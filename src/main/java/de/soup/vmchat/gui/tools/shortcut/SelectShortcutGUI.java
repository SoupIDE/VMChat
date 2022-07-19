package de.soup.vmchat.gui.tools.shortcut;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.soup.vmchat.VMChat;
import de.soup.vmchat.config.configs.shortcut.elements.Shortcuts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class SelectShortcutGUI extends Screen {

    private ShortcutList shortcutList;
    private boolean isListRendered;
    private int listRenderDelay;

    private Button removeButton;
    private Button editButton;

    public SelectShortcutGUI(ITextComponent title) {
        super(title);
    }

    @Override
    protected void init()
    {
        super.init();

        isListRendered = false;
        listRenderDelay = 5;
        shortcutList = new ShortcutList(new ArrayList<>());

        addButton(new Button(this.width/2-100,this.height-82,60,20,new StringTextComponent("Add"),(b)->{VMChat.getVars().getClient().setScreen(new CreateShortcutGUI(new StringTextComponent("create")));}));
        removeButton = new Button(this.width/2-28,this.height-82,60,20,new StringTextComponent("Remove"),(b)->{ shortcutList.removeEntry(shortcutList.getSelected()); });
        addButton(removeButton);
        editButton = new Button(this.width/2+40,this.height-82,60,20,new StringTextComponent("Edit"),(b)->{VMChat.getVars().getClient().setScreen(new CreateShortcutGUI(new StringTextComponent("create"),shortcutList.getSelected().shortcut));});
        addButton(editButton);
        addButton(new Button(this.width/2-100,this.height-32,200,20,new StringTextComponent("Close"),(b)->{VMChat.getVars().getClientPlayer().closeContainer();}));

    }

    @Override
    public void render(MatrixStack matrix, int x, int y, float ticks)
    {
        if(!isListRendered) listRenderDelay--;
        if(!isListRendered && listRenderDelay == 0)
        {
            isListRendered = true;
            shortcutList = new ShortcutList(VMChat.getShortcutConfig().getSettings().getShortcuts());
            this.children.add(shortcutList);
            if(!shortcutList.children().isEmpty()) shortcutList.setSelected(shortcutList.children().get(0));
            else {
                editButton.active = false;
                removeButton.active = false;
            }
        }
        renderBackground(matrix);
        super.render(matrix, x, y, ticks);

        shortcutList.render(matrix,x,y,ticks);
        buttons.forEach(b->b.visible=true);
    }

    class ShortcutList extends ExtendedList<ShortcutList.ShortcutEntry>
    {
         ShortcutList(List<Shortcuts.Shortcut> shortcuts)
         {
            super(SelectShortcutGUI.this.getMinecraft(), SelectShortcutGUI.this.width, SelectShortcutGUI.this.height, 40, SelectShortcutGUI.this.height-110 ,22);
            setRenderBackground(false);
            setRenderTopAndBottom(false);
            shortcuts.forEach(s->addEntry(new ShortcutEntry(s)));
         }

        @Override
        protected int getScrollbarPosition() {
            return this.width-30;
        }

        @Override
        protected boolean removeEntry(ShortcutEntry entry)
        {
            VMChat.getShortcutConfig().getSettings().getShortcuts().remove(entry.shortcut);
            VMChat.getShortcutConfig().save();
            super.removeEntry(entry);

            if(children().isEmpty())
            {
                editButton.active = false;
                removeButton.active = false;
            }
            else setSelected(children().get(0));

            return false;
        }

        class ShortcutEntry extends ExtendedList.AbstractListEntry<ShortcutEntry>
        {

            private final Shortcuts.Shortcut shortcut;

            ShortcutEntry(Shortcuts.Shortcut shortcut){ this.shortcut = shortcut; }

            @Override
            public void render(MatrixStack matrix, int id, int top, int left, int entryWidth, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_)
            {
                drawString(matrix,font, TextFormatting.WHITE+this.shortcut.getName(),left+4,top+5,1);
                drawString(matrix,font, (TextFormatting.WHITE+" ->  "+this.shortcut.getShortcut()),left+entryWidth-95,top+5,1);
            }

            @Override
            public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_)
            {
                if(p_231044_5_ == 0) select();
                return false;
            }

            private void select(){ setSelected(this); }
        }
    }
}
