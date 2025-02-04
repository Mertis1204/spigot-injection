package com.hakan.spinjection.config.container.impl;

import com.hakan.spinjection.config.annotations.ConfigFile;
import com.hakan.spinjection.config.annotations.ConfigValue;
import com.hakan.spinjection.config.container.Container;
import com.hakan.spinjection.config.utils.ColorUtils;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Method;

/**
 * {@inheritDoc}
 */
@SuppressWarnings({"unchecked"})
public class YamlContainer extends Container {

    private final File file;
    private final FileConfiguration configuration;

    /**
     * {@inheritDoc}
     */
    public YamlContainer(@Nonnull Object instance,
                         @Nonnull ConfigFile annotation) {
        super(instance, annotation);
        this.file = new File(super.path);
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable <T> T get(@Nonnull String key) {
        return (T) this.configuration.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable <T> T get(@Nonnull String key, @Nonnull Class<T> clazz) {
        return clazz.cast(this.configuration.get(key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable <T> T get(@Nonnull Method method, @Nonnull ConfigValue annotation) {
        Object value = this.get(annotation.value(), method.getReturnType());
        return ((value instanceof String) && (annotation.colored())) ?
                (T) ColorUtils.colored(value.toString()) : (T) value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull Container set(@Nonnull String key, @Nonnull Object value) {
        return this.set(key, value, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull Container set(@Nonnull String key, @Nonnull Object value, boolean save) {
        this.configuration.set(key, value);
        if (save) this.save();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public synchronized @Nonnull Container save() {
        this.configuration.save(this.file);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public synchronized @Nonnull Container reload() {
        this.configuration.load(this.file);
        return this;
    }
}