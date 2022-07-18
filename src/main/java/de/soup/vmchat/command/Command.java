package de.soup.vmchat.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class Command {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Declaration{
        String name();
        String syntax();
        String description();
    }

    private Declaration getDeclaration(){ return getClass().getDeclaredAnnotation(Declaration.class); }

    private final String name = getDeclaration().name();
    private final String syntax = getDeclaration().syntax();
    private final String description = getDeclaration().description();

    public String getName(){ return this.name; }
    public String getSyntax(){ return CommandManager.COMMAND_PREFIX+" "+this.syntax; }
    public String getDescription(){ return this.description; }

    public abstract void run(String[] args);
}
