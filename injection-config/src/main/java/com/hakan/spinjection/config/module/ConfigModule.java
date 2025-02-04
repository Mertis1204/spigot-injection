package com.hakan.spinjection.config.module;

import com.hakan.spinjection.SpigotBootstrap;
import com.hakan.spinjection.config.annotations.ConfigFile;
import com.hakan.spinjection.config.executor.ConfigExecutor;
import com.hakan.spinjection.module.SpigotModule;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * ConfigModule is a module class
 * that is used to load configuration classes
 * which are annotated with {@link ConfigFile}.
 */
@SuppressWarnings({"rawtypes"})
public class ConfigModule extends SpigotModule<Class, ConfigFile> {

    /**
     * Constructor of ConfigModule.
     *
     * @param bootstrap bootstrap
     */
    public ConfigModule(@Nonnull SpigotBootstrap bootstrap) {
        super(bootstrap, Class.class, ConfigFile.class);
    }

    /**
     * Loads classes which are annotated with {@link ConfigFile}.
     * And creates {@link ConfigExecutor} for each class to
     * handle configuration processes.
     *
     * @param classes classes that are annotated with {@link ConfigFile}.
     */
    @Override
    public void load(@Nonnull Set<Class> classes) {
        for (Class clazz : classes) {
            if (!clazz.isInterface())
                throw new RuntimeException("configuration class must be interface!");


            ConfigExecutor configExecutor = new ConfigExecutor(clazz);
            configExecutor.execute(super.bootstrap, configExecutor.getInstance());

            super.bind(clazz).withInstance(configExecutor.getInstance());
        }
    }

    /**
     * Executes all configuration executors which are
     * saved in {@link #executors}.
     * <p>
     * Then it runs execute method of each executor.
     */
    @Override
    public void execute() {

    }
}
