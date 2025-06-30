package dev.loottech.api.manager.module;

import dev.loottech.api.manager.module.Module;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface RegisterModule {
    public String name();

    public Module.Category category();

    public String tag() default " ";

    public String description() default "No description.";

    public boolean persistent() default false;

    public int bind() default 0;

    public boolean drawn() default true;
}
