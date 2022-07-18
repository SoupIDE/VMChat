package de.soup.vmchat.config.configs.filter.elements;

public enum FilterType{
    IGNORE,
    SECOND_CHAT,
    COPY_SECOND_CHAT;

    @Override
    public String toString() { return (name().equals("IGNORE")) ? "Ignore" : (name().equals("SECOND_CHAT") ? "2ndChat" : "Copy2ndChat"); }
}
