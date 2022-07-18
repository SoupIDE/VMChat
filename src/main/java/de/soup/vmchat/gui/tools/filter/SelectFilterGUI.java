package de.soup.vmchat.gui.tools.filter;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.soup.vmchat.VMChat;
import de.soup.vmchat.config.configs.filter.elements.ChatFilters;
import de.soup.vmchat.config.configs.filter.elements.FilterType;
import de.soup.vmchat.util.MessageBus;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.*;

public class SelectFilterGUI extends Screen {

    private FilterList filterList;
    private int filterRenderDelay;
    private boolean filterIsRendered;
    private Button removeButton;
    private Button editButton;

    public SelectFilterGUI(ITextComponent title) {
        super(title);
    }

    @Override
    protected void init()
    {
        super.init();

        filterIsRendered = false;
        filterRenderDelay = 5;
        filterList = new FilterList(new ArrayList<>());

        addButton(new Button(this.width/2-100,this.height-82,60,20,new StringTextComponent("Add"),(b)->{VMChat.getVars().getClient().setScreen(new CreateFilterGUI(new StringTextComponent("create")));}));
        removeButton = new Button(this.width/2-28,this.height-82,60,20,new StringTextComponent("Remove"),(b)->{ filterList.removeEntry(filterList.getSelected()); });
        addButton(removeButton);
        editButton = new Button(this.width/2+40,this.height-82,60,20,new StringTextComponent("Edit"),(b)->{VMChat.getVars().getClient().setScreen(new CreateFilterGUI(new StringTextComponent("create"),filterList.getSelected().filter));});
        addButton(editButton);
        addButton(new Button(this.width/2-100,this.height-32,200,20,new StringTextComponent("Close"),(b)->{VMChat.getVars().getClientPlayer().closeContainer();}));
    }

    @Override
    public void render(MatrixStack matrix, int x, int y, float ticks)
    {
        if(!filterIsRendered)filterRenderDelay--;
        if(!filterIsRendered && filterRenderDelay==0)
        {
            filterIsRendered = true;
            filterList = new FilterList(VMChat.getFilterConfig().getSettings().getChatFilters());
            children.add(filterList);
            if(!filterList.children().isEmpty()) filterList.setSelected(filterList.children().get(0));
            else
            {
                removeButton.active = false;
                editButton.active = false;
            }
        }
        renderBackground(matrix);
        super.render(matrix, x, y, ticks);
        filterList.render(matrix,x,y,ticks);
        buttons.forEach(b->b.visible = true);
    }

    class FilterList extends ExtendedList<SelectFilterGUI.FilterList.FilterEntry>
    {
        // 1. Minecraft 2. Width 3. Height 4. Top (y) 5. Bottom (y) 5. Entry width
        // * There are no left or right, substituted by "x0 = ... " and "x1 = ... "
        FilterList(List<ChatFilters.ChatFilter> filters)
        {
            super(SelectFilterGUI.this.getMinecraft(), SelectFilterGUI.this.width,SelectFilterGUI.this.height,40, SelectFilterGUI.this.height-110 ,22);
            setRenderBackground(false);
            setRenderTopAndBottom(false);

            filters.forEach(filter-> addEntry(new FilterEntry(filter)));
        }

        @Override
        protected int getScrollbarPosition() {
            return width-30;
        }

        @Override
        public void setSelected(@Nullable FilterEntry entry) {
            editButton.active = true;
            super.setSelected(entry);
        }

        @Override
        protected boolean removeEntry(FilterEntry entry) {
            VMChat.getFilterConfig().getSettings().getChatFilters().remove(getSelected().filter);
            VMChat.getFilterConfig().save();
            super.removeEntry(entry);
            if(filterList.children().isEmpty())
            {
                removeButton.active = false;
                editButton.active = false;
            }
            else filterList.setSelected(filterList.children().get(0));

            return false;
        }

        class FilterEntry extends ExtendedList.AbstractListEntry<FilterEntry>
        {
            private final ChatFilters.ChatFilter filter;

            FilterEntry(ChatFilters.ChatFilter filter)
            {
                this.filter = filter;
            }

            @Override
            public void render(MatrixStack matrix, int id, int top, int left, int entryWidth, int entryHeight, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_)
            {
                drawString(matrix,font, TextFormatting.WHITE+this.filter.getName(),left+4,top+5,1);
                drawString(matrix,font, TextFormatting.WHITE+" ->  "+ MessageBus.parseTextFormattingString((filter.getType() == FilterType.IGNORE ? "&c" : filter.getType() == FilterType.SECOND_CHAT ? "&e" : "&6")+this.filter.getType().toString()),left+entryWidth-95,top+5,1);
            }

            @Override
            public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_)
            {
                if(p_231044_5_ == 0) this.select();
                return false;
            }

            private void select(){FilterList.this.setSelected(this);}
        }
    }
}

