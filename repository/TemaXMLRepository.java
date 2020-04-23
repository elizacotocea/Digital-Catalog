package myproject.repository;

import myproject.entities.Tema;
import myproject.validator.TemaValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TemaXMLRepository  extends AbstractXMLRepository<Integer, Tema> {
    public TemaXMLRepository(String fileName) {
        super(new TemaValidator(), fileName);
    }

    @Override
    protected Element createElementfromEntity(Document document, Tema homework) {
        Element element = document.createElement("homework");

        element.setAttribute("id", homework.getId().toString());

        Element name = document.createElement("descriere");
        name.setTextContent(homework.getDescriere());
        element.appendChild(name);

        Element pra = document.createElement("startweek");
        pra.setTextContent(Integer.toString(homework.getStartWeek()));
        element.appendChild(pra);

        Element em= document.createElement("deadline");
        em.setTextContent(Integer.toString(homework.getDeadlineweek()));
        element.appendChild(em);
        return element;
    }

    @Override
    protected Tema createEntityFromElement(Element eElement) {
        String id= eElement.getAttribute("id");
        String desc= eElement.getElementsByTagName("descriere").item(0).getTextContent();
        String startweek=eElement.getElementsByTagName("startweek").item(0).getTextContent();
        String deadline= eElement.getElementsByTagName("deadline").item(0).getTextContent();
        Tema t=new Tema(Integer.parseInt(id),desc,Integer.parseInt(startweek),Integer.parseInt(deadline),1);
        return t;
    }
}
