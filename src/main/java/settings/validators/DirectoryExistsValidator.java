package settings.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

/**
 * Created by bulat on 06.01.17.
 */
public class DirectoryExistsValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (!Files.isDirectory(Paths.get(value), LinkOption.NOFOLLOW_LINKS)) {
            throw new ParameterException(String.format("Invalid directory: %s", name, value));
        }
    }
}
