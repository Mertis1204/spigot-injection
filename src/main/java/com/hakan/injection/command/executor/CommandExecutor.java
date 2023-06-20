package com.hakan.injection.command.executor;

import com.hakan.injection.command.annotations.Command;
import com.hakan.injection.command.annotations.Parameter;
import com.hakan.injection.command.supplier.ParameterSuppliers;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.reflections.ReflectionUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Command executor to
 * listen bukkit commands and
 * invoke related method.
 */
public class CommandExecutor extends BukkitCommand {

    private final Object instance;
    private final Method method;

    /**
     * Constructor of {@link CommandExecutor}.
     *
     * @param command  annotation
     * @param instance class instance
     * @param method   method
     */
    public CommandExecutor(@Nonnull Command command,
                           @Nonnull Object instance,
                           @Nonnull Method method) {
        super(command.name(), command.description(), command.usage(), Arrays.asList(command.aliases()));
        this.instance = instance;
        this.method = method;
    }

    /**
     * Registers the command to
     * bukkit command map and routes
     * the command to this executor.
     */
    public void register() {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            org.bukkit.command.Command command = commandMap.getCommand(this.getName());

            if (command != null && command.isRegistered())
                return;

            commandMap.register(this.getName(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Executes the command, returning its success.
     *
     * @param sender the sender of the command
     * @param label  the command label
     * @param args   the command arguments
     * @return true if the command was successful
     */
    @Override
    public boolean execute(@Nonnull CommandSender sender,
                           @Nonnull String label,
                           @Nonnull String[] args) {
        java.lang.reflect.Parameter[] parameters = this.method.getParameters();
        Object[] objects = new Object[parameters.length];

        objects[0] = sender;

        for (int i = 1; i < parameters.length; i++) {
            if (!parameters[i].isAnnotationPresent(Parameter.class))
                throw new RuntimeException("parameter must be annotated with @CommandParameter!");


            String parameter = args[i - 1];
            Class<?> parameterType = parameters[i].getType();

            objects[i] = ParameterSuppliers.apply(parameterType, parameter);
        }

        ReflectionUtils.invoke(this.method, this.instance, objects);
        return false;
    }
}