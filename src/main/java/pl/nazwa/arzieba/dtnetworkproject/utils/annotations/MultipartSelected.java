package pl.nazwa.arzieba.dtnetworkproject.utils.annotations;

import pl.nazwa.arzieba.dtnetworkproject.utils.validators.MultipartSelectedValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MultipartSelectedValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)

public @interface MultipartSelected {
    String message() default "Invalid phone number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
