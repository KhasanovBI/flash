package settings.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Created by bulat on 06.01.17.
 */
public class PortValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        try {
            int port = Integer.parseInt(value);
            if (port < 0 || port > 65535) {
                throw new ParameterException("Parameter " + name + " should be in range [0, 65535]");
            }
        } catch (NumberFormatException ignored) {
            // Валидация идет раньше конвертации, при конвертации валидируется int
        }
    }
}
