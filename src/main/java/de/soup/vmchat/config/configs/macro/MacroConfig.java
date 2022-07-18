package de.soup.vmchat.config.configs.macro;

import de.soup.vmchat.config.Config;
import de.soup.vmchat.config.configs.macro.elements.Macros;

import java.io.File;

public class MacroConfig extends Config<Macros> {
    public MacroConfig(File parent) {
        super(new File(parent,"macros.json"), Macros.class);
    }
}
