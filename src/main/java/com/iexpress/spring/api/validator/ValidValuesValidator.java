package com.iexpress.spring.api.validator;

import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class ValidValuesValidator implements ConstraintValidator<ValidValues, Object> {

	private String fieldName;
	private List<String> allowedValues;
	
	@Override
	public void initialize(ValidValues constraintAnnotation) {
		fieldName = constraintAnnotation.fieldName();
		allowedValues = Arrays.asList(constraintAnnotation.acceptedValues());
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		boolean flag  = true;
		try {			
			
			if(!allowedValues.contains(value)){
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
					.addConstraintViolation();
					flag = false;
			}
		} catch (final Exception e) {
			return flag;
		}
		return flag;
	}
	
}

