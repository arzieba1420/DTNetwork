package pl.nazwa.arzieba.dtnetworkproject.utils.validators;

import org.springframework.web.multipart.MultipartFile;
import pl.nazwa.arzieba.dtnetworkproject.utils.annotations.MultipartSelected;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class MultipartSelectedValidator implements
        ConstraintValidator<MultipartSelected, MultipartFile> {
    @Override
    public void initialize(MultipartSelected constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        return !Objects.equals(file.getOriginalFilename(), "");
    }
}
