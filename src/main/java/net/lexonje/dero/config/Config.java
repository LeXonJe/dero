package net.lexonje.dero.config;

import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;

public abstract class Config extends YamlFile {

    private String path;
    private File file;

    public Config(String path) {
        this.path = path;
        this.file = new File(path);
        if (file.exists()) {
            this.load();
        }
    }

    @Override
    public boolean exists() {
        return file.exists();
    }

    @Override
    public void load() {
        try {
            super.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
            super.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
