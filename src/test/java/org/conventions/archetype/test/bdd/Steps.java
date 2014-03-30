package org.conventions.archetype.test.bdd;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Steps {

	
	Class<?>[] value();
}
