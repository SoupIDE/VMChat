package de.soup.vmchat.config.configs.macro.elements;

import de.soup.vmchat.util.Keyboard;

import java.util.*;

public class Macros {
    private List<Macro> macros = new ArrayList<>();

    public List<Macro> getMacros(){ return this.macros; }
    public boolean isMacroKeySaved(List<Keyboard.Key> keys){ return this.macros.stream().anyMatch(m-> keys.toString().equals(m.getKeys().toString())); }
    public boolean isMacroNameSaved(String name) { return this.macros.stream().anyMatch(m->m.getName().equalsIgnoreCase(name)); }

    public static class Macro{

        private String name;
        private String message;
        private List<Keyboard.Key> keys;
        private boolean isAutoSend;
        private transient boolean isAvailable;

        public Macro(String name, String message, List<Keyboard.Key> keys, boolean isAutoSend) {
            this.isAvailable = true;
            this.name = name;
            this.message = message;
            this.keys = keys;
            this.isAutoSend = isAutoSend;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public List<Keyboard.Key> getKeys() { return this.keys; }
        public void setKeys(List<Keyboard.Key> keys) { this.keys = keys; }

        public boolean isAutoSend() { return isAutoSend; }
        public void setAutoSend(boolean autoSend) { isAutoSend = autoSend; }

        public boolean isAvailable() { return isAvailable; }
        public void setAvailable(boolean available) { isAvailable = available; }

        public boolean hasAddition(int key){return getKeys().size() > 1 && getKeys().stream().skip(1).anyMatch(k->k.getKey() == key);}
    }
}
