package myproject.validator;

import myproject.entities.Nota;

import java.time.format.DateTimeFormatter;

public class NotaValidator implements Validator<Nota> {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Override
    public void validate(Nota e) throws ValidationException {
        String errMsg="";
        if ((int)e.getNota()==0 && !e.getFeedback().equals("nenotat"))
            errMsg+="nota nu poate fi 0!";
        else if ((int)e.getNota()>11 || ((int)e.getNota()<1 && !e.getFeedback().equals("nenotat")))
            errMsg+="Nota nu este in intervalul [1-10]";
        if (errMsg!="")
            throw new ValidationException(errMsg);
    }
}
