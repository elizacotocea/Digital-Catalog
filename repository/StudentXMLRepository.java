package myproject.repository;

import myproject.entities.Student;
import myproject.validator.StudentValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class StudentXMLRepository extends AbstractXMLRepository<Integer, Student> {

    public StudentXMLRepository(String fileName) {
        super(new StudentValidator(), fileName);
    }

    @Override
    protected Element createElementfromEntity(Document document, Student student) {
        Element element = document.createElement("student");

        element.setAttribute("id", student.getId().toString());

        Element name = document.createElement("firstName");
        name.setTextContent(student.getNume());
        element.appendChild(name);

        Element pra = document.createElement("lastName");
        pra.setTextContent(student.getPrenume());
        element.appendChild(pra);

        Element em= document.createElement("email");
        em.setTextContent(student.getEmail());
        element.appendChild(em);

        Element prof= document.createElement("prof");
        prof.setTextContent(student.getCadruDidacticIndrumatorLab());
        element.appendChild(prof);

        Element grupa = document.createElement("group");
        grupa.setTextContent(student.getGrupa());
        element.appendChild(grupa);

        return element;

    }

    @Override
    protected Student createEntityFromElement(Element eElement) {
                String id= eElement.getAttribute("id");
                String fName= eElement.getElementsByTagName("firstName").item(0).getTextContent();
                String lName=eElement.getElementsByTagName("lastName").item(0).getTextContent();
                String email= eElement.getElementsByTagName("email").item(0).getTextContent();
                String prof=eElement.getElementsByTagName("prof").item(0).getTextContent();
                String grupa=eElement.getElementsByTagName("group").item(0).getTextContent();
                Student s=new Student(Integer.parseInt(id),fName,lName,email,prof,grupa);
                return s;
}}
