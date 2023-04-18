package de.craftery.craftinglib.command;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(value = CheckArg.List.class)
public @interface CheckArg {
    int index();
    ArgType type() default ArgType.NOT_PROVIDED;
    boolean checkType() default true;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface List {
        CheckArg[] value();
    }
}
