package dev.loottech.api.manager.element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface RegisterElement {
    public String name();

    public String tag() default "4GquuoBHl7gkSDaNeMb5";

    public String description() default "No description.";
}
