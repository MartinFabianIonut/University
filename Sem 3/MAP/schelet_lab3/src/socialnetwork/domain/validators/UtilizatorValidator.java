package socialnetwork.domain.validators;

import socialnetwork.domain.Utilizator;
import socialnetwork.domain.exceptions.ValidationException;

import java.util.Objects;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        String error = "";
        if(entity.getId() < 0)
            error+= "Id invalid\n";
        if(Objects.equals(entity.getLastName(), ""))
            error+= "Nume invalid\n";
        if(Objects.equals(entity.getFirstName(), ""))
            error+= "Prenume invalid\n";
        if(!error.equals(""))
            throw new ValidationException(error);
    }
}
