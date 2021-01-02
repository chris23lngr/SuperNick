package de.z1up.supernick.mysql;

// imports
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.z1up.supernick.SuperNick;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SQLConfig {

    private File dir;
    private File file;
    private FileConfiguration configuration;

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

        configuration.addDefault("host", "localhost");
        configuration.addDefault("port", "3306");
        configuration.addDefault("database", "database");
        configuration.addDefault("username", "username");
        configuration.addDefault("password", "password");

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
        data.put("host", configuration.getString("host"));
        data.put("port", configuration.getString("port"));
        data.put("database", configuration.getString("database"));
        data.put("username", configuration.getString("username"));
        data.put("password", configuration.getString("password"));

        return data;
    }
}