package de.soup.vmchat.config.configs.shortcut.elements;

import com.google.common.collect.Lists;

import java.util.List;

public class Shortcuts {
    private List<Shortcut> shortcuts = Lists.newArrayList();

    public List<Shortcut> getShortcuts(){ return this.shortcuts; }
    public boolean isShortcutNameSaved(String name){ return getShortcuts().stream().anyMatch(m->m.getName().equalsIgnoreCase(name)); }
    public boolean isShortcutShortcut(String shortcut){ return getShortcuts().stream().anyMatch(m-> m.getShortcut().equalsIgnoreCase(shortcut)); }


    public static class Shortcut {

        private String name;
        private String shortcut;
        private String replacement;
        private boolean ignoreCommands;
        private boolean isAutoSend;

        public Shortcut(String name, String shortcut, String replacement, boolean ignoreCommands, boolean isAutoSend)
        {
            this.name = name;
            this.shortcut = shortcut;
            this.replacement = replacement;
            this.ignoreCommands = ignoreCommands;
            this.isAutoSend = isAutoSend;
        }

        public String getName(){ return this.name; }
        public void setName(String name){ this.name = name; }

        public String getShortcut() { return shortcut; }
        public void setShortcut(String shortcut) { this.shortcut = shortcut; }

        public String getReplacement() { return replacement; }
        public void setReplacement(String replacement) { this.replacement = replacement; }

        public boolean isIgnoreCommands() { return ignoreCommands; }
        public void setIgnoreCommands(boolean ignoreCommands) { this.ignoreCommands = ignoreCommands; }

        public boolean isAutoSend() { return isAutoSend; }
        public void setAutoSend(boolean autoSend) { isAutoSend = autoSend; }
    }
}
