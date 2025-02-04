package com.hakan.spinjection.config.container;

import com.hakan.spinjection.config.annotations.ConfigFile;
import com.hakan.spinjection.config.annotations.ConfigValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * ConfigContainer is an abstract class
 * that is used to load and save config files.
 */
public abstract class Container {

    protected final Object instance;
    protected final String path;
    protected final String resource;

    /**
     * Creates a new ConfigContainer.
     *
     * @param instance   instance of the class
     * @param annotation ConfigFile annotation
     */
    public Container(@Nonnull Object instance,
                     @Nonnull ConfigFile annotation) {
        this.instance = instance;
        this.path = annotation.path();
        this.resource = annotation.resource();
    }


    /**
     * Gets instance of the class.
     *
     * @return instance of the class
     */
    public @Nonnull Object getInstance() {
        return this.instance;
    }

    /**
     * Gets the path of the config file.
     *
     * @return path of the config file
     */
    public @Nonnull String getPath() {
        return this.path;
    }

    /**
     * Gets resource of the config file.
     *
     * @return resource of the config file
     */
    public @Nonnull String getResource() {
        return this.resource;
    }



    /**
     * Gets value from config file
     * with the given key.
     *
     * @param key value key
     * @param <T> value type
     * @return value
     */
    public abstract @Nullable <T> T get(@Nonnull String key);

    /**
     * Gets value from config file
     * with the given key.
     *
     * @param key   value key
     * @param clazz value class
     * @param <T>   value type
     * @return value
     */
    public abstract @Nullable <T> T get(@Nonnull String key, @Nonnull Class<T> clazz);

    /**
     * Gets value from config file with the given key, and if
     * colored is true and value is String, it will be colored.
     *
     * @param method     method
     * @param annotation ConfigValue annotation
     * @param <T>        value type
     * @return value
     */
    public abstract @Nullable <T> T get(@Nonnull Method method, @Nonnull ConfigValue annotation);

    /**
     * Sets value to config file
     * with the given key and save
     * it to file.
     *
     * @param key   key
     * @param value value
     * @return instance of the class
     */
    public abstract @Nonnull Container set(@Nonnull String key, @Nonnull Object value);

    /**
     * Sets value to config file
     * with the given key.
     *
     * @param key   key
     * @param value value
     * @param save  save config file after
     *              setting the value
     * @return instance of the class
     */
    public abstract @Nonnull Container set(@Nonnull String key, @Nonnull Object value, boolean save);

    /**
     * Saves last data to
     * config file as persist.
     *
     * @return instance of the class
     */
    public abstract @Nonnull Container save();

    /**
     * Gets all nodes from config
     * and sets it to the all fields
     * of this class.
     *
     * @return instance of the class
     */
    public abstract @Nonnull Container reload();
}