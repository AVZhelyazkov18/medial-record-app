package bg.nbu.medialrecordapp.validation;

import bg.nbu.medialrecordapp.data.dto.RegisterRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, RegisterRequestDTO> {

    @Override
    public boolean isValid(RegisterRequestDTO dto, ConstraintValidatorContext context) {
        if (dto == null)
            return true;

        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();

        if (password == null || confirmPassword == null)
            return true;

        boolean matches = password.equals(confirmPassword);

        if (!matches) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }

        return matches;
    }
}