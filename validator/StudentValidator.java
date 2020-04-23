package myproject.validator;

import myproject.entities.Student;

public class StudentValidator implements Validator<Student> {

    //private Object ValidationException;

    @Override
    public void validate(Student e) throws ValidationException {
        String errMsg="";
        if (e.getNume() == null || "".equals(e.getNume()) || e.getNume().matches(".*\\d.*") || e.getNume().matches(".*[.?!@#$%^&*()].*"))
            errMsg+="Numele trebuie sa contina doar litere \n ";
        if (e.getPrenume() == null || "".equals(e.getPrenume()) || e.getPrenume().matches(".*\\d.*") || e.getPrenume().matches(".*[.?!@#$%^&*()].*"))
            errMsg+="Prenumele trebuie sa contina doar litere \n ";
        if (e.getCadruDidacticIndrumatorLab() == null || "".equals(e.getCadruDidacticIndrumatorLab()) || e.getCadruDidacticIndrumatorLab().matches(".*\\d.*"))
            errMsg+="Numele profesorului trebuie sa contina doar litere \n ";
        if (e.getEmail() == null || "".equals(e.getEmail()) || !e.getEmail().contains("@") || !e.getEmail().contains("."))
            errMsg+="Email-ul este in format invalid! \n";
        if (e==null || !e.getGrupa().matches("[0-9]{3}")){
            errMsg+="Grupa trebuie sa fie formata din 3 cifre \n";
        }
        if (errMsg!="")
            throw new ValidationException(errMsg);
    }

}
