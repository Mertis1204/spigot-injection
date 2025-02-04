package com.hakan.spinjection.command.module;

import com.hakan.spinjection.SpigotBootstrap;
import com.hakan.spinjection.command.annotations.Command;
import com.hakan.spinjection.command.executor.CommandExecutor;
import com.hakan.spinjection.executor.SpigotExecutor;
import com.hakan.spinjection.module.SpigotModule;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * CommandModule registers command executors.
 */
@SuppressWarnings({"rawtypes"})
public class CommandModule extends SpigotModule<Class, Command> {

    /**
     * Constructor of {@link CommandModule}.
     *
     * @param bootstrap bootstrap
     */
    public CommandModule(@Nonnull SpigotBootstrap bootstrap) {
        super(bootstrap, Class.class, Command.class);
    }

    /**
     * Loads classes which are annotated with {@link Command}.
     * And creates {@link CommandExecutor} for each class to
     * handle command processes.
     *
     * @param classes classes that are annotated with {@link Command}.
     */
    @Override
    public void load(@Nonnull Set<Class> classes) {
        for (Class clazz : classes) {
            super.executors.add(new CommandExecutor(clazz));
        }
    }

    /**
     * Executes all command executors which are
     * saved in {@link #executors}.
     * <p>
     * Then it runs execute method of each executor.
     */
    @Override
    public void execute() {
        for (SpigotExecutor executor : super.executors) {
            executor.execute(super.bootstrap, super.bootstrap.getInstance(executor.getDeclaringClass()));
        }
    }
}
