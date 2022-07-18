package de.soup.vmchat.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.soup.vmchat.VMChat;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Config<T> {

    private final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    private File file;
    private Class<? extends T> defaults;
    private T settings;

    public Config(File file, Class<? extends T> defaults)
    {
        this.file = file;
        this.defaults = defaults;
        loadConfig(false);
    }
    private void loadConfig(boolean reload)
    {
        if(!this.file.getParentFile().exists()) this.file.getParentFile().mkdir();

        boolean created = reload && this.file.exists();

        if(!this.file.exists())
        {
            try
            {
                this.file.createNewFile();
                created = true;
            }
            catch (IOException ex) { ex.printStackTrace(); }
        }

        FileInputStream stream = null;

        try{ stream = new FileInputStream(this.file); }
        catch (FileNotFoundException ex) { ex.printStackTrace(); }

        try
        {
            this.settings = GSON.fromJson(created ? GSON.toJson(this.defaults.newInstance()) : IOUtils.toString(stream, StandardCharsets.UTF_8),this.defaults);
            VMChat.LOGGER.info("Loaded "+((this.settings == null) ? "new " : "") +this.file.getName()+"!");

            if(!reload && this.settings == null) this.loadConfig(true);
            else if(reload && this.settings == null) this.save();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            VMChat.LOGGER.info("Failed to load "+this.file.getName()+" config!");
            if(!reload) this.loadConfig(true);
        }
        finally
        {
            try{ stream.close(); }
            catch (IOException ex) {ex.printStackTrace();}
        }
    }

    public void save()
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.file),StandardCharsets.UTF_8),true);
            writer.print(GSON.toJson(this.settings));
            writer.flush();
            writer.close();
        }
        catch (Exception ex) { ex.printStackTrace(); }
    }

    public File getFile(){ return this.file; }
    public T getSettings(){ return this.settings; }
}
