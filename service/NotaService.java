package myproject.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import myproject.databases.NotaDatabase;
import myproject.databases.StudentDatabase;
import myproject.databases.TemaDatabase;
import myproject.entities.*;
import myproject.utils.events.ChangeEventType;
import myproject.utils.events.NotaChangeEvent;
import myproject.utils.observer.Observable;
import myproject.utils.observer.Observer;
import myproject.validator.NotaValidator;
import myproject.validator.StudentValidator;
import myproject.validator.TemaValidator;
import myproject.validator.ValidationException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class NotaService implements Observable<NotaChangeEvent>,Service<String, Nota>{
    StudentDatabase studRepo;
    TemaDatabase temaRepo;
    NotaDatabase notaRepo;
    public NotaService(String user, String passwd,String urlN,String urlS, String urlT) {
        studRepo=new StudentDatabase(new StudentValidator(),user,passwd,urlS);
        temaRepo=new TemaDatabase(new TemaValidator(),user,passwd,urlT);
        notaRepo=new NotaDatabase(new NotaValidator(),user,passwd,urlN,urlS,urlT);
    }

    public Tema findTema(int id){
        return temaRepo.findOne(id);
    }

    public Nota findOne(String id) {
        return (Nota) notaRepo.findOne(id);
    }

    public Iterable<Nota> findAll() {
        return notaRepo.findAll();
    }

    public Nota save(Nota entity) throws ValidationException {
       Nota n= notaRepo.save(entity);
        if(n == null) {
            notifyObservers(new NotaChangeEvent(ChangeEventType.ADD,n));
        }
        thread_send_email_to_student(entity.getStudent().getEmail());
        return n;
    }

    public Nota delete(String s) {
        Nota n= notaRepo.delete(s);
        if(n != null) {
            notifyObservers(new NotaChangeEvent(ChangeEventType.DELETE,n));
        }
        return n;
    }

    public Nota update(Nota entity) throws ValidationException {
       Nota n=notaRepo.update(entity);
        if(n == null) {
            notifyObservers(new NotaChangeEvent(ChangeEventType.UPDATE,n));
        }
        return n;
    }

    public int getPenality(Tema t,LocalDate data) {
        StructuraSemestru str = new StructuraSemestru();
        if (str.getSemestru(data) != null){
            if (str.getSemestru(data).getSaptamana() <= t.getDeadlineweek() && str.getSemestru(data).getNrsem()==t.getSem())
                return 0;
        if (str.getSemestru(data).getSaptamana() == t.getDeadlineweek() + 1 && str.getSemestru(data).getNrsem()==t.getSem())
            return 1;
        else if (str.getSemestru(data).getSaptamana() == t.getDeadlineweek() + 2 && str.getSemestru(data).getNrsem()==t.getSem())
            return 2;
        return -1;
        }
        return -100;
    }

    public float addGrade(Nota n,float nota, String feedback,int motivari) throws ValidationException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        String id=Integer.toString(s.getId())+Integer.toString(t.getId());
//        Nota n=this.findOne(id);
//        int deadlineweek=t.getDeadlineweek();
//        if (n!=null){
//            float notaf=n.setNota(nota,motivari,deadlineweek);
//            n.setFeedback(feedback);
//            this.update(n);}
//        else {
//            Nota nnew = new Nota(s, t, LocalDate.now().format(formatter), s.getCadruDidacticIndrumatorLab());
//            nnew.setNota(nota, motivari, deadlineweek);
//            nnew.setFeedback(feedback);
//            this.save(nnew);
//        }
        return -1;
    }

    public List<Student> filter_students_with_hw(int nr_hw){
        Iterable<Nota> notes=findAll();
        List<Nota> gr = new ArrayList<Nota>();
        notes.forEach(gr::add);
        return gr.stream()
                .filter(n->n.getTema().getId()==nr_hw)
                .map(x->{return x.getStudent();} )
                .collect(Collectors.toList());
    }

    public List<Student> filter_students_with_hw_teacher(int nr_hw,String name){
        Iterable<Nota> notes=findAll();
        List<Nota> gr = new ArrayList<Nota>();
        notes.forEach(gr::add);
        return gr.stream()
                .filter(n->n.getTema().getId()==nr_hw && n.getProfesor().equals(name))
                .map(x->{return x.getStudent();} )
                .collect(Collectors.toList());
    }

    public List<Float> filter_grades_in_week_of_hw(int hw,int week){
        Iterable<Nota> notes=findAll();
        List<Nota> gr = new ArrayList<Nota>();
        notes.forEach(gr::add);
        return gr.stream()
                .filter(n->n.getTema().getId()==hw && n.getWeek()==week && n.getNota()!=0)
                .map(x->{return x.getNota();} )
                .collect(Collectors.toList());
    }

    public double get_average_for_a_student(int student){
        List<Nota> listaNote= new ArrayList<Nota>();
        Iterable<Nota> note1=notaRepo.findAll();
        note1.forEach(listaNote::add);

        List<Tema> listaTeme= new ArrayList<Tema>();
        Iterable<Tema> teme=temaRepo.findAll();
        teme.forEach(listaTeme::add);

        return listaNote.stream().filter(grade -> grade.getStudent().getId().equals(student))
                .mapToDouble(grade ->Double.valueOf(Float.toString(grade.getNota()))*
                        (temaRepo.findOne(grade.getTema().getId()).getDeadlineweek() - temaRepo.findOne(grade.getTema().getId()).getStartWeek()))
                .sum() / (listaTeme.stream().mapToInt(tema ->
                tema.getDeadlineweek()-tema.getStartWeek())).sum();
    }

    public List<RaportDTO> get_average_for_students(){
        ArrayList<RaportDTO> aux = new ArrayList<RaportDTO>();
        studRepo.findAll()
                .forEach(stud ->{
                    aux.add(new RaportDTO(stud.getNume(), stud.getPrenume(), get_average_for_a_student(stud.getId()),stud.getCadruDidacticIndrumatorLab()));
                });
        return aux;
    }

    public List<RaportDTO> get_students_promoted(){
        return get_average_for_students()
                .stream()
                .filter(stud -> stud.getNota() >=5)
                .collect(Collectors.toList());
    }

    public List<Tema> get_teme_lipsa(int student){
        ArrayList<Nota> aux = new ArrayList<Nota>();
        notaRepo.findAll().forEach(aux::add);
        List<Tema> auxList  = aux.stream()
                .filter(grade -> grade.getStudent().getId().equals(student))
                .map(grade -> temaRepo.findOne(grade.getTema().getId()))
                .collect(Collectors.toList());
        List<Tema> result = new ArrayList<>();
        temaRepo.findAll().forEach(tema -> {
            if (auxList.stream().noneMatch(tema1 -> tema1.getId().equals(tema.getId())))
                result.add(tema);
        });
        result.sort(Comparator.comparingInt(Tema::getDeadlineweek));
        return result;
    }
    
    public List<RaportDTO> get_students_with_all_assignments(){
        List<Student> studentList = new ArrayList<Student>();
        List<Nota> gradeList = new ArrayList<Nota>();
        studRepo.findAll().forEach(studentList::add);
        notaRepo.findAll().forEach(gradeList::add);
        List<Nota> intarziat = gradeList.stream()
                .filter(grade -> grade.getFeedback().contains("intarziat"))
                .collect(Collectors.toList());
        return studentList.stream()
                .filter(student -> get_teme_lipsa(student.getId()).size() == 0)
                .filter(student -> intarziat.stream().noneMatch(grade -> grade.getStudent().getId().equals(student.getId())))
                .map(student -> new RaportDTO(student.getNume(), student.getPrenume(), get_average_for_a_student(student.getId()),student.getCadruDidacticIndrumatorLab()))
                .collect(Collectors.toList());
    }


    private Double get_average_at_an_assignment(int idtema){
        ArrayList<Nota> listaNote = new ArrayList<Nota>();
        ArrayList<Tema> listaTeme = new ArrayList<Tema>();
        notaRepo.findAll().forEach(listaNote::add);
        temaRepo.findAll().forEach(listaTeme::add);
        return listaNote.stream().filter(nota -> nota.getId().equals(idtema))
                .mapToDouble(Nota::getNota)
                .sum()/listaTeme.size();
    }

    public Tema get_hardest_assignment(){
        double medie= -1;
        Tema tema = null;
        for (Tema t: temaRepo.findAll()) {
            if (get_average_at_an_assignment(t.getId()) > medie){
                medie = get_average_at_an_assignment(t.getId());
                tema =t;
            }
        }
        return tema;
    }

    public void thread_send_email_to_student(String studentMail){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                send_email_to_student(studentMail);
            }
        });
    }

    public void send_email_to_student(String studentEmail) {
        final String username = "catalognotemap@gmail.com";
        final String password = "aplicatie999";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        session.setDebug(true);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(studentEmail));   // like inzi769@gmail.com
            message.setSubject("Ai primit o nota!");
            message.setText("Profesorul tau de laborator ti-a notat o tema! Intra in aplicatie pentru detalii!");
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveRaportInPdf(String prof) {
        Document document = new Document();
        String path="C:\\Users\\eliza\\IdeaProjects\\Proiect_map\\src\\com\\company\\resources\\PDFs\\RaportsPDF"+prof+".pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
        } catch (DocumentException e) {
        } catch (FileNotFoundException e) {
        }

        document.open();
        Font font = FontFactory.getFont(FontFactory.TIMES, 12, BaseColor.BLACK);
        String pdfcontext = "Raport profesor "+prof+"\n\n";

        try {
            document.add(new Paragraph(pdfcontext));
            document.add(new Paragraph("Medii studenti: "));
            for(var nota : get_average_for_students().stream().filter(n->n.getProfesor().compareTo(prof)==0).collect(Collectors.toList())) {
                document.add(new Paragraph(nota.getName() + " " + nota.getPrenume() + ":  " + String.valueOf(nota.getNota()) + "\n"));
            }
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Studentii care pot intra in examen (au media peste 5):\n\n"));
            for(var nota : get_average_for_students().stream().filter(n->n.getProfesor().compareTo(prof)==0).collect(Collectors.toList()))
                if(nota.getNota() > (double) 5)
                    document.add(new Paragraph(nota.getName() + " " + nota.getPrenume() + ":  " + String.valueOf(nota.getNota()) + "\n"));
                document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Stundentii fara intarzieri:\n\n"));
            for(var student : get_students_with_all_assignments().stream().filter(n->n.getProfesor().compareTo(prof)==0).collect(Collectors.toList()))
                document.add(new Paragraph(student.getName() + " " + student.getPrenume() + "\n"));
            Tema tema=get_hardest_assignment();
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Cea mai grea tema: "));
            document.add(new Paragraph("Laborator-ul "+tema.getId()+" din saptamana " + String.valueOf(tema.getStartWeek()) +"-"+String.valueOf(tema.getDeadlineweek())+  "\n\n\n"));

        }
        catch (DocumentException e) {
        }
        document.close();
    }
    private List<Observer<NotaChangeEvent>> observers=new ArrayList<>();

    @Override
    public void addObserver(Observer<NotaChangeEvent> e) {
        observers.add(e);
    }
    @Override
    public void removeObserver(Observer<NotaChangeEvent> e) {
        //observers.remove(e);
    }


    @Override
    public void notifyObservers(NotaChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }

}



 //   public int getPenality(Student s, Tema t){
//        Iterable<Nota> notes=this.findAll();
//        Nota nota=null;
//        StructuraSemestru str=new StructuraSemestru();
//        for (Nota n:notes)
//            if (n.getStudent().getId()==s.getId() && n.getTema().getId()==t.getId()) {
//                nota = n;
//            }
//        if (nota!=null) {
//            if (nota.getWeek() <= t.getDeadlineweek())
//                return 0;
//            if (nota.getWeek() == t.getDeadlineweek() + 1)
//                return 1;
//            else if (nota.getWeek() == t.getDeadlineweek() + 2)
//                return 2;
//            else if (nota.getWeek()>t.getDeadlineweek()+2)
//                return nota.getWeek()-t.getDeadlineweek();
//        }
//        else if(str.getSemestru(LocalDate.now()).getSaptamana()<=t.getDeadlineweek()+2)
//            return -1;
//        return -(str.getSemestru(LocalDate.now()).getSaptamana()-t.getDeadlineweek());
//    }
