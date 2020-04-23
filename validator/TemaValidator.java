package myproject.validator;

import myproject.entities.Tema;

public class TemaValidator implements Validator<Tema> {
    @Override
    public void validate(Tema e) throws ValidationException {
        String errMsg="";
        if (e.getDescriere()==null)
            errMsg+="Descriere error";
        if (e.getDeadlineweek()<0 || e.getDeadlineweek()>=14)
            errMsg+="Deadlineweek week it's not between 1 and 14";
        if (e.getDeadlineweek()<e.getStartWeek())
            errMsg+="Deadline bigger than StartWeek";
        if (errMsg!="" )
            throw new ValidationException(errMsg);
    }
}
