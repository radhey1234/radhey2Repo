package com.iexpress.spring.api.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidValuesValidator.class)
public @interface ValidValues {

	String message() default "{invalid.deviceType}";
	String[] acceptedValues();
	String fieldName() default "";

	Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    /**
	 * Defines several <code>@FieldMatch</code> annotations on the same element
	 * 
	 * @see ValidAcceptedValueForField
	 */
	@Target( { ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		ValidValues[] value();
	}
}

