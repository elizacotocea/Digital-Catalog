package myproject.repository;

import myproject.entities.Nota;
import myproject.entities.Student;
import myproject.entities.Tema;
import myproject.validator.NotaValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NotaXMLRepository extends AbstractXMLRepository<String, Nota> {
    public NotaXMLRepository(String fileName,String fS, String fT) {
        super(new NotaValidator(), fileName,fS,fT);
    }

    @Override
    protected Element createElementfromEntity(Document document, Nota grade) {
        Element element = document.createElement("grade");

        element.setAttribute("id", grade.getId().toString());

        Element ids = document.createElement("idStudent");
        ids.setTextContent(Integer.toString(grade.getStudent().getId()));
        element.appendChild(ids);

        Element idh = document.createElement("idHomework");
        idh.setTextContent(Integer.toString(grade.getTema().getId()));
        element.appendChild(idh);

        Element gr= document.createElement("idGrade");
        gr.setTextContent(grade.getId());
        element.appendChild(gr);

        Element d= document.createElement("date");
        d.setTextContent(grade.getData());
        element.appendChild(d);

        Element pr= document.createElement("prof");
        pr.setTextContent(grade.getProfesor());
        element.appendChild(pr);

        Element v= document.createElement("value");
        v.setTextContent(Float.toString(grade.getNota()));
        element.appendChild(v);

        Element f= document.createElement("feedback");
        f.setTextContent(grade.getFeedback());
        element.appendChild(f);

        return element;
    }

    @Override
    protected Nota createEntityFromElement(Element eElement) {
        String id= eElement.getAttribute("id");
        String ids= eElement.getElementsByTagName("idStudent").item(0).getTextContent();
        String idt=eElement.getElementsByTagName("idHomework").item(0).getTextContent();
        String data= eElement.getElementsByTagName("date").item(0).getTextContent();
        String prof= eElement.getElementsByTagName("prof").item(0).getTextContent();
        String value=eElement.getElementsByTagName("value").item(0).getTextContent();
        String feedback= eElement.getElementsByTagName("feedback").item(0).getTextContent();
        Student st= (Student) srep.findOne(Integer.parseInt(ids));
        Tema tt=(Tema)trep.findOne(Integer.parseInt(idt));
        Nota nota=new Nota(st,tt,data,prof);
        nota.updNota(Float.parseFloat(value));
        nota.setFeedback(feedback);
        return nota;
    }
}
