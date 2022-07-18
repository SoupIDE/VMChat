package de.soup.vmchat.gui.tools.macros;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.soup.vmchat.VMChat;
import de.soup.vmchat.config.configs.macro.elements.Macros;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SelectMacroGUI extends Screen {

    private MacroList macroList;

    private Button removeButton;
    private Button editButton;

    private int renderDelay;
    private boolean isRendered;

    public SelectMacroGUI(ITextComponent title) { super(title); }

    @Override
    protected void init()
    {
        super.init();

        this.renderDelay = 5;
        this.isRendered = false;
        macroList = new MacroList(new ArrayList<>());

        addButton(new Button(this.width/2-100,this.height-82,60,20,new StringTextComponent("Add"),(b)->{VMChat.getVars().getClient().setScreen(new CreateMacroGUI(new StringTextComponent("create")));}));
        removeButton = new Button(this.width/2-28,this.height-82,60,20,new StringTextComponent("Remove"),(b)->{ macroList.removeEntry(macroList.getSelected()); });
        addButton(removeButton);
        editButton = new Button(this.width/2+40,this.height-82,60,20,new StringTextComponent("Edit"),(b)->{VMChat.getVars().getClient().setScreen(new CreateMacroGUI(new StringTextComponent("create"),macroList.getSelected().macro));});
        addButton(editButton);
        addButton(new Button(this.width/2-100,this.height-32,200,20,new StringTextComponent("Close"),(b)->{VMChat.getVars().getClientPlayer().closeContainer();}));
    }

    @Override
    public void render(MatrixStack matrix, int x, int y, float ticks)
    {
        if(!isRendered) renderDelay--;
        if(!isRendered && renderDelay==0)
        {
            this.isRendered = true;
            macroList = new MacroList(VMChat.getMacroConfig().getSettings().getMacros());
            children.add(macroList);
            if(!macroList.children().isEmpty()) macroList.setSelected(macroList.children().get(0));
            else
            {
                removeButton.active = false;
                editButton.active = false;
            }
        }
        renderBackground(matrix);
        super.render(matrix, x, y, ticks);
        macroList.render(matrix,x,y,ticks);
        buttons.forEach(b->b.visible = true);
    }

    class MacroList extends ExtendedList<MacroList.MacroEntry>
    {
         MacroList(List<Macros.Macro> macros)
         {
            super(SelectMacroGUI.this.getMinecraft(), SelectMacroGUI.this.width, SelectMacroGUI.this.height, 40, SelectMacroGUI.this.height-110 ,22);
            setRenderBackground(false);
            setRenderTopAndBottom(false);
            macros.forEach(macro-> addEntry(new MacroEntry(macro)));
        }

        @Override
        protected int getScrollbarPosition() { return width-30; }

        @Override
        public void setSelected(@Nullable MacroEntry p_241215_1_)
        {
             editButton.active = true;
             super.setSelected(p_241215_1_);
        }

        @Override
        protected boolean removeEntry(MacroEntry entry)
        {
             VMChat.getMacroConfig().getSettings().getMacros().remove(entry.macro);
             VMChat.getMacroConfig().save();
             super.removeEntry(entry);

             if(macroList.children().isEmpty())
             {
                 editButton.active = false;
                 removeButton.active = false;
             }
             else setSelected(macroList.children().get(0));

             return false;
        }

        class MacroEntry extends ExtendedList.AbstractListEntry<MacroEntry>
        {
            private final Macros.Macro macro;

            private MacroEntry(Macros.Macro macro){ this.macro = macro; }

            @Override
            public void render(MatrixStack matrix, int id, int top, int left, int entryWidth, int entryHeight, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_)
            {
                drawString(matrix,font, TextFormatting.WHITE+this.macro.getName(),left+4,top+5,1);
                drawString(matrix,font, (TextFormatting.WHITE+" ->  [ "+this.macro.getKeys().stream().map(m->TextFormatting.GREEN+m.getName().toUpperCase()).collect(Collectors.joining(TextFormatting.WHITE+" + "))+TextFormatting.WHITE+" ]"),left+entryWidth-95,top+5,1);
            }

            @Override
            public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_)
            {
                if(p_231044_5_ == 0) this.select();
                return false;
            }

            private void select() { MacroList.this.setSelected(this); }
        }
    }
}
