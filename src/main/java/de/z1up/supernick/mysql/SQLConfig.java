package de.z1up.supernick.mysql;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.z1up.supernick.SuperNick;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SQLConfig {

    private File                    dir;
    private File                    file;
    private FileConfiguration       configuration;

    private final String ATTRIBUTE_HOST = "host";
    private final String ATTRIBUTE_PORT = "port";
    private final String ATTRIBUTE_DB = "database";
    private final String ATTRIBUTE_USER = "username";
    private final String ATTRIBUTE_PW = "password";

    public SQLConfig() {
        dir = SuperNick.getInstance().getDataFolder();
        createFile();
        loadFileConfiguration();
        setDefaults();
    }

    void createFile() {
        if(!dir.exists()) {
            dir.mkdirs();
        }

        file = new File(dir.getPath(), "mysql.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    void loadFileConfiguration() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    void setDefaults() {

        configuration.options().copyDefaults(true);

        configuration.addDefault(ATTRIBUTE_HOST, "localhost");
        configuration.addDefault(ATTRIBUTE_PORT, "3306");
        configuration.addDefault(ATTRIBUTE_DB, "database");
        configuration.addDefault(ATTRIBUTE_USER, "username");
        configuration.addDefault(ATTRIBUTE_PW, "password");

        save();
    }

    void save() {
        try {
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public HashMap<String, String> readData() {

        HashMap<String, String> data = new HashMap<>();
        data.put(ATTRIBUTE_HOST, configuration.getString(ATTRIBUTE_HOST));
        data.put(ATTRIBUTE_PORT, configuration.getString(ATTRIBUTE_PORT));
        data.put(ATTRIBUTE_DB, configuration.getString(ATTRIBUTE_DB));
        data.put(ATTRIBUTE_USER, configuration.getString(ATTRIBUTE_USER));
        data.put(ATTRIBUTE_PW, configuration.getString(ATTRIBUTE_PW));

        return data;
    }
}