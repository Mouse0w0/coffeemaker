package com.github.mouse0w0.coffeemaker.template;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate to static non-final field.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface DeclareFieldForeach {
    /**
     * @return The expression of iterable.
     */
    String iterable();

    /**
     * @return The variable name of each element.
     */
    String variableName();

    /**
     * @return The field definition of each element.
     */
    String expression() default "";

    boolean modifyFinal() default true;
}
