package com.hakan.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hakan.injection.command.module.CommandModule;
import com.hakan.injection.listener.module.ListenerModule;
import com.hakan.injection.module.SpigotModule;
import com.hakan.injection.scanner.module.ComponentModule;
import com.hakan.injection.scanner.module.ServiceModule;
import com.hakan.injection.scheduler.module.SchedulerModule;
import com.hakan.injection.utils.ReflectionUtils;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;

import javax.annotation.Nonnull;

/**
 * SpigotBootstrap is bootstrap class
 * of the injection library. It starts
 * automatic injection and inject
 * all classes that are specified
 * in modules.
 */
public class SpigotBootstrap extends AbstractModule {

    /**
     * Starts automatic injection.
     *
     * @param plugin plugin instance
     * @return bootstrap
     */
    public static @Nonnull SpigotBootstrap run(@Nonnull Plugin plugin) {
        SpigotBootstrap bootstrap = new SpigotBootstrap(plugin);

        bootstrap.register(new ServiceModule(plugin, bootstrap.injector, bootstrap.reflections));
        bootstrap.register(new ComponentModule(plugin, bootstrap.injector, bootstrap.reflections));
        bootstrap.register(new CommandModule(plugin, bootstrap.injector, bootstrap.reflections));
        bootstrap.register(new ListenerModule(plugin, bootstrap.injector, bootstrap.reflections));
        bootstrap.register(new SchedulerModule(plugin, bootstrap.injector, bootstrap.reflections));

        return bootstrap;
    }



    private final Plugin plugin;
    private final Injector injector;
    private final Reflections reflections;

    /**
     * Constructor of SpigotBootstrap.
     *
     * @param plugin plugin instance
     */
    private SpigotBootstrap(@Nonnull Plugin plugin) {
        this.plugin = plugin;
        this.injector = Guice.createInjector(this);
        this.reflections = ReflectionUtils.createFrom(plugin);
    }

    /**
     * Returns plugin instance.
     *
     * @return plugin
     */
    public @Nonnull Plugin getPlugin() {
        return this.plugin;
    }

    /**
     * Returns injector instance.
     *
     * @return injector
     */
    public @Nonnull Injector getInjector() {
        return this.injector;
    }

    /**
     * Returns reflections instance.
     *
     * @return reflections
     */
    public @Nonnull Reflections getReflections() {
        return this.reflections;
    }

    /**
     * Registers a module.
     *
     * @param module module
     * @return injector
     */
    public @Nonnull Injector register(@Nonnull SpigotModule module) {
        this.install(module);
        return Guice.createInjector(module);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        this.bind(Plugin.class).toInstance(this.plugin);

        this.install(this.injector.getInstance(ServiceModule.class));
        this.install(this.injector.getInstance(ComponentModule.class));
        this.install(this.injector.getInstance(CommandModule.class));
        this.install(this.injector.getInstance(ListenerModule.class));
        this.install(this.injector.getInstance(SchedulerModule.class));
    }
}
