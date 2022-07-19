package de.soup.vmchat.config.configs.shortcut;

import de.soup.vmchat.config.Config;
import de.soup.vmchat.config.configs.shortcut.elements.Shortcuts;

import java.io.File;

public class ShortcutConfig extends Config<Shortcuts> {
    public ShortcutConfig(File parent) {
        super(new File(parent,"shortcuts.json"),Shortcuts.class);
        save();
    }
}
