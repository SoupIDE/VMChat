package de.soup.vmchat.util;

import com.google.common.collect.Lists;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class Keyboard {
    private static List<Integer> PRESSED_KEYS = Lists.newArrayList();

    public static boolean isKeyPressed(Key key){ return PRESSED_KEYS.contains(key.getKey()); }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event)
    {
        int key = event.getKey();
        if(key != -1)
        {
            if(event.getAction() == 0) PRESSED_KEYS.remove((Integer) key);
            else if(event.getAction() == 1) PRESSED_KEYS.add(key);
        }
    }

    public static class Key{

        private final String name;
        private final int keyCode;

        public Key(InputMappings.Input key)
        {
            this.name = key.getDisplayName().getString();
            this.keyCode = key.getValue();
        }

        public String getName(){ return this.name; }
        public int getKey(){ return this.keyCode; }

        @Override
        public String toString() {
            return "Key{"+this.name+" -> "+this.keyCode+"}";
        }
    }
}

