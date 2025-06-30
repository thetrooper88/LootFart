package dev.loottech.api.manager.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface RegisterCommand {
    public String name();

    public String syntax();

    public String[] aliases();

    public String description() default "No description.";
}
