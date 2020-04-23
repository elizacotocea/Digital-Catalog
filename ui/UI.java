package myproject.ui;

import myproject.entities.Nota;
import myproject.entities.Student;
import myproject.entities.Tema;
import myproject.service.NotaService;
import myproject.service.StudentService;
import myproject.service.TemaService;
import myproject.validator.ValidationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DateTimeException;

public class UI {
    //    String fileStudent;
//    String fileTema;
//    String fileNota;
    StudentService studentService;
    TemaService temaService;
    NotaService notaService;

    public UI(String user, String passwd,String urlStudent, String urlTema, String urlNota) {
        studentService = new StudentService(user,passwd,urlStudent);
        temaService = new TemaService(user,passwd,urlTema);
        notaService = new NotaService(user,passwd,urlNota,urlStudent,urlTema);
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private void menu() {
        System.out.println("Meniu:");
        System.out.println("1.Add student/hw/grade");
        System.out.println("2.Find one student/hw/grade");
        System.out.println("3.Find all students/hw/grade");
        System.out.println("4.Delete student/hw/grade");
        System.out.println("5.Update student/hw/grade");
        System.out.println("6.Filter by group");
        System.out.println("7.Filter students by hw");
        System.out.println("8.Filter students by hw and prof");
        System.out.println("9.Filter grade by hw and week");
        System.out.println("Exit");
    }

    private Student readStudent() throws IOException {
        System.out.println("ID=");
        int id = Integer.parseInt(reader.readLine());
        System.out.println("Nume=");
        String nume = reader.readLine();
        System.out.println("Prenume=");
        String prenume = reader.readLine();
        System.out.println("Email=");
        String email = reader.readLine();
        System.out.println("Cadru=");
        String cadru = reader.readLine();
        System.out.println("Grupa=");
        String grupa = reader.readLine();
        Student s = new Student(id, nume, prenume, email, cadru, grupa);
        return s;
    }

    private Tema readTema() throws IOException {
        System.out.println("ID tema=");
        int idt = Integer.parseInt(reader.readLine());
        System.out.println("Descriere=");
        String desc = reader.readLine();
        System.out.println("Deadline=");
        int dl = Integer.parseInt(reader.readLine());
        Tema t = new Tema(idt, desc, dl);
        return t;
    }

    public void run() throws IOException {
            int stop = 0;
            while (stop == 0) {
                menu();
                try {
                    String cmd = reader.readLine();
                    if (cmd.equals("Exit")) {
                        stop = 1;
                        return;
                    } else if (cmd.equals("Add student")) {
                        Student s = readStudent();
                        try {
                            if (studentService.save(s) != null)
                                System.out.println("Exista un student cu acest id!");
                        } catch (ValidationException e) {
                            System.out.println(e.getMessage());
                        }
                    } else if (cmd.equals("Delete student")) {
                        System.out.println("ID=");
                        int id = Integer.parseInt(reader.readLine());
                        if (studentService.delete(id) == null)
                            System.out.println("Nu s-a gasit niciun student cu acest id!");
                    } else if (cmd.equals("Update student")) {
                        Student s = readStudent();
                        try {
                            if (studentService.update(s) != null)
                                System.out.println("Nu s-a gasit niciun student cu acest id!");
                        } catch (ValidationException e) {
                            System.out.println(e.getMessage());
                        }
                    } else if (cmd.equals("Find student")) {
                        System.out.println("ID=");
                        int id = Integer.parseInt(reader.readLine());
                        Student s = studentService.findOne(id);
                        if (s == null)
                            System.out.println("Nu exista!");
                        else
                            System.out.println(s);
                    } else if (cmd.equals("Find all students")) {
                        Iterable<Student> st = studentService.findAll();
                        if (st.spliterator().getExactSizeIfKnown() == 0)
                            System.out.println("Nu exista niciun student salvat!");
                        else {
                            for (Student s : st) {
                                System.out.println(s);
                            }
                        }
                    } else if (cmd.equals("Add hw")) {
                        Tema t = readTema();
                        try {
                            if (temaService.save(t) != null)
                                System.out.println("Mai exista o tema cu acest ID!");
                        } catch (ValidationException e) {
                            System.out.println(e.getMessage());
                        }
                    } else if (cmd.equals("Delete hw")) {
                        System.out.println("ID=");
                        int id = Integer.parseInt(reader.readLine());
                        if (temaService.delete(id) == null)
                            System.out.println("Nu exista tema cu acest ID!");
                    } else if (cmd.equals("Update hw")) {
                        Tema t = readTema();
                        try {
                            if (temaService.update(t) != null) {
                                System.out.println("Nu s-a gasit tema cu acest ID!");
                            }
                        } catch (ValidationException e) {
                            System.out.println(e.getMessage());
                        }
                    } else if (cmd.equals("Find hw")) {
                        System.out.println("ID=");
                        int id = Integer.parseInt(reader.readLine());
                        Tema tema = temaService.findOne(id);
                        if (tema == null)
                            System.out.println("Nu exista tema cu acest ID!");
                        else
                            System.out.println(tema);
                    } else if (cmd.equals("Find all hw")) {
                        Iterable<Tema> tt = temaService.findAll();
                        if (tt.spliterator().getExactSizeIfKnown() == 0)
                            System.out.println("Nu exista teme stocate!");
                        else {
                            for (Tema t : tt) {
                                System.out.println(t);
                            }
                        }
                    }
                    else if (cmd.equals("Delete grade")){
                        System.out.println("Dati nume student:");
                        String nume = reader.readLine();
                        System.out.println("Dati prenume student:");
                        String prenume = reader.readLine();
                        System.out.println("Dati nr tema: ");
                        int tema = Integer.parseInt(reader.readLine());
                        Student s = studentService.findByname(nume, prenume);
                        Tema t = temaService.findOne(tema);
                        if (t == null)
                            System.out.println("Nu exista tema cu acest id!");
                        else if (s == null)
                            System.out.println("Nu exista student cu acest nume!");
                        else {
                            try {
                                Nota n = notaService.delete(Integer.toString(s.getId()) + Integer.toString(t.getId()));
                                if (n == null)
                                    System.out.println("Nu exista o nota pt acest student si tema");
                            } catch (ValidationException v) {
                                System.out.println(v.getMessage());
                            }
                        }
                    }
                    else if (cmd.equals("Update grade")){
                        System.out.println("Dati nume student:");
                        String nume = reader.readLine();
                        System.out.println("Dati prenume student:");
                        String prenume = reader.readLine();
                        System.out.println("Dati nr tema: ");
                        int tema = Integer.parseInt(reader.readLine());
                        Student s = studentService.findByname(nume, prenume);
                        Tema t = temaService.findOne(tema);
                        String d=reader.readLine();
                        String nota=reader.readLine();
                        String f=reader.readLine();
                        if (t == null)
                            System.out.println("Nu exista tema cu acest id!");
                        else if (s == null)
                            System.out.println("Nu exista student cu acest nume!");
                        else {
                            try {
                                Nota n = notaService.update(new Nota(s, t,d, s.getCadruDidacticIndrumatorLab(),Float.parseFloat(nota),f));
                                if (n != null)
                                    System.out.println("Nu exista pt acest student si tema");
                            } catch (ValidationException v) {
                                System.out.println(v.getMessage());
                            }
                        }
                    }
                    else if (cmd.equals("Find grade")){
                        System.out.println("Dati nume student:");
                        String nume = reader.readLine();
                        System.out.println("Dati prenume student:");
                        String prenume = reader.readLine();
                        System.out.println("Dati nr tema: ");
                        int tema = Integer.parseInt(reader.readLine());
                        Student s = studentService.findByname(nume, prenume);
                        Tema t = temaService.findOne(tema);
                        if (t == null)
                            System.out.println("Nu exista tema cu acest id!");
                        else if (s == null)
                            System.out.println("Nu exista student cu acest nume!");
                        else {
                            try {
                                Nota n = notaService.findOne(Integer.toString(s.getId()) + Integer.toString(t.getId()));
                                if (n == null)
                                    System.out.println("Nu exista o nota pt acest student si tema");
                                System.out.println(n);
                            } catch (ValidationException v) {
                                System.out.println(v.getMessage());
                            }
                        }
                    }
                    else if (cmd.equals("Find all grades")){
                            try {
                                Iterable<Nota> n = notaService.findAll();
                                for (Nota t : n) {
                                    System.out.println(t);
                                }
                                if (n.spliterator().getExactSizeIfKnown()==0)
                                    System.out.println("Nu exista o nota pt acest student si tema");
                            } catch (ValidationException v) {
                                System.out.println(v.getMessage());
                            }
                    }
//                    else if (cmd.equals("Add student hw")) {
//                        String regex = "[0-9]{2}/[01][0-9]/[0-9]{4}";
//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                        System.out.println("Dati nume student:");
//                        String nume = reader.readLine();
//                        System.out.println("Dati prenume student:");
//                        String prenume = reader.readLine();
//                        System.out.println("Dati nr tema: ");
//                        int tema = Integer.parseInt(reader.readLine());
//                        Student s = studentService.findByname(nume, prenume);
//                        Tema t = temaService.findOne(tema);
//                        if (t == null)
//                            System.out.println("Nu exista tema cu acest id!");
//                        else if (s == null)
//                            System.out.println("Nu exista student cu acest nume!");
//                        else {
//                    System.out.println("Introduceti data cand ati depus tema: ");
//                    String data = reader.readLine();
//                    while (!data.matches(regex)) {
//                        System.out.println("Nu ati introdus formatul corect!");
//                        System.out.println("Introduceti data cand ati depus tema: ");
//                        data = reader.readLine();
////                    }
//                            Nota n = new Nota(s, t, LocalDate.now().format(formatter), s.getCadruDidacticIndrumatorLab());
//                            try {
//                                notaService.save(n);
//                            } catch (ValidationException e) {
//                                System.out.println(e.getMessage());
//                            }
//                        }
//                    } else if (cmd.equals("Add grade")) {
//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                        StructuraSemestru str = new StructuraSemestru();
//                        float nota;
//                        String feedback;
//                        System.out.println("Dati nume student:");
//                        String nume = reader.readLine();
//                        System.out.println("Dati prenume student:");
//                        String prenume = reader.readLine();
//                        System.out.println("Dati numarul temei: ");
//                        int tema = Integer.parseInt(reader.readLine());
//                        Student s = studentService.findByname(nume, prenume);
//                        Tema t = temaService.findOne(tema);
//                        if (t == null)
//                            System.out.println("Nu exista tema cu acest id!");
//                        else if (s == null)
//                            System.out.println("Nu exista student cu acest nume!");
//                        else {
//                            int penality = notaService.getPenality(s, t);
//                            int motivari = 0;
//                            if (penality == 0) {
//                                System.out.println("Dati nota:");
//                                nota = Float.parseFloat(reader.readLine());
//                                System.out.println("Dati feedback:");
//                                feedback = reader.readLine();
//                                try {
//                                    notaService.addGrade(s, t, nota, feedback, motivari);
//                                } catch (ValidationException e) {
//                                    System.out.println(e.getMessage());
//                                }
//                            } else if (penality == 1) {
//                                System.out.println("Studentului i se va aplica o penalizare de 1 pct, are motivare? Da/Nu");
//                                if (reader.readLine().equals("Da")) {
//                                    System.out.println("Din ce data este motivarea?");
//                                    String d = reader.readLine();
//                                    LocalDate data = LocalDate.parse(d, formatter);
//                                    int sap = str.getSemestru(data).getSaptamana();
//                                    if (sap >= t.getStartWeek() || sap<=t.getDeadlineweek()+1) {
//                                        motivari = 1;
//                                    } else {
//                                        System.out.println("Motivarea nu este buna!");
//                                        motivari = 0;
//                                    }
//                                    System.out.println("Dati nota:");
//                                    nota = Float.parseFloat(reader.readLine());
//                                    System.out.println("Dati feedback:");
//                                    feedback = reader.readLine();
//                                    try {
//                                        notaService.addGrade(s, t, nota, feedback, motivari);
//                                    } catch (ValidationException e) {
//                                        System.out.println(e.getMessage());
//                                    }
//                                } else {
//                                    System.out.println("Dati nota:");
//                                    nota = Float.parseFloat(reader.readLine());
//                                    System.out.println("Dati feedback:");
//                                    feedback = reader.readLine();
//                                    try {
//                                        notaService.addGrade(s, t, nota, feedback, 0);
//                                    } catch (ValidationException e) {
//                                        System.out.println(e.getMessage());
//                                    }
//                                }
//                            } else if (penality == 2) {
//                                System.out.println("Studentului i se vor aplica 2 penalizari de 1 pct, are motivare? Da/Nu");
//                                if (reader.readLine().equals("Da")) {
//                                    System.out.println("Cate? 1/2");
//                                    motivari = Integer.parseInt(reader.readLine());
//                                    if (motivari == 1) {
//                                        System.out.println("Din ce data este motivarea?");
//                                        String d = reader.readLine();
//                                        LocalDate data = LocalDate.parse(d, formatter);
//                                        int sap = str.getSemestru(data).getSaptamana();
//                                        if (sap < t.getStartWeek() || sap > t.getDeadlineweek()+2) {
//                                            System.out.println("Motivarea nu este buna!");
//                                            motivari = 0;
//                                        }
//                                    } else if (motivari == 2) {
//                                        System.out.println("Din ce data este prima motivare?");
//                                        String d = reader.readLine();
//                                        LocalDate data = LocalDate.parse(d, formatter);
//                                        int sap = str.getSemestru(data).getSaptamana();
//                                        if (sap < t.getStartWeek() || sap > t.getDeadlineweek()+2) {
//                                            System.out.println("Motivarea nu este buna!");
//                                            motivari -= 1;
//                                        }
//                                        System.out.println("Din ce data este a doua motivare?");
//                                        String d1 = reader.readLine();
//                                        LocalDate data1 = LocalDate.parse(d1, formatter);
//                                        int sap1 = str.getSemestru(data1).getSaptamana();
//                                        if (sap1 < t.getStartWeek() || sap1 > t.getDeadlineweek()+2) {
//                                            System.out.println("Motivarea nu este buna!");
//                                            motivari -= 1;
//                                        }
//                                    }
//                                    System.out.println("Dati nota:");
//                                    nota = Float.parseFloat(reader.readLine());
//                                    System.out.println("Dati feedback: ");
//                                    feedback = reader.readLine();
//                                    try {
//                                        notaService.addGrade(s, t, nota, feedback, motivari);
//                                    } catch (ValidationException e) {
//                                        System.out.println(e.getMessage());
//                                    }
//                                } else {
//                                    System.out.println("Dati nota: ");
//                                    nota = Float.parseFloat(reader.readLine());
//                                    System.out.println("Dati feedback:");
//                                    feedback = reader.readLine();
//                                    try {
//                                        notaService.addGrade(s, t, nota, feedback, 0);
//                                    } catch (ValidationException e) {
//                                        System.out.println(e.getMessage());
//                                    }
//                                }
//                            } else if (penality > 2) {
//                                System.out.printf("Studentul a intarziat %d saptamani, are motivari? %n", penality);
//                                if (reader.readLine().equals("Da")) {
//                                    System.out.println("Cate?");
//                                    motivari = Integer.parseInt(reader.readLine());
//                                    for (int i = 0; i < motivari; i++) {
//                                        System.out.println("Din ce data este  motivarea?");
//                                        String d1 = reader.readLine();
//                                        LocalDate data1 = LocalDate.parse(d1, formatter);
//                                        int sap1 = str.getSemestru(data1).getSaptamana();
//                                        if (sap1 < t.getStartWeek() || sap1 > t.getDeadlineweek() + penality) {
//                                            System.out.println("Motivarea nu este buna!");
//                                            motivari -= 1;
//                                        }
//                                    }
//                                    if (penality - motivari >= 3) {
//                                        System.out.println("Prea putine motivari! Studentul va primi nota 1!");
//                                        try {
//                                            notaService.addGrade(s, t, 1, "Ai trimis tema prea tarziu si nu ai destule motivari!", 99);
//                                        } catch (ValidationException e) {
//                                            System.out.println(e.getMessage());
//                                        }
//                                    } else {
//                                        System.out.println("Dati nota:");
//                                        nota = Float.parseFloat(reader.readLine());
//                                        System.out.println("Dati feedback:");
//                                        feedback = reader.readLine();
//                                        try {
//                                            notaService.addGrade(s, t, nota, feedback, motivari);
//                                        } catch (ValidationException e) {
//                                            System.out.println(e.getMessage());
//                                        }
//                                    }
//                                } else {
//                                    System.out.println("Studentul va primi nota 1!");
//                                    try {
//                                        notaService.addGrade(s, t, 1, "Nu ai trimis tema si nu ai destule motivari!", 99);
//                                    } catch (ValidationException e) {
//                                        System.out.println(e.getMessage());
//                                    }
//                                }
//                            } else if (penality == -1) {
//                                System.out.println("Studentul inca nu a predat tema, dar mai are timp!");
//                            } else {
//                                System.out.printf("Studentul nu a predat tema! Au trecut %d sapt de la deadline! Are motivari? %n", -penality);
//                                if (reader.readLine().equals("Da")) {
//                                    System.out.println("Cate? ");
//                                    motivari = Integer.parseInt(reader.readLine());
//                                    for (int i = 0; i < motivari; i++) {
//                                        System.out.println("Din ce data este  motivarea?");
//                                        String d1 = reader.readLine();
//                                        LocalDate data1 = LocalDate.parse(d1, formatter);
//                                        int sap1 = str.getSemestru(data1).getSaptamana();
//                                        if (sap1 < t.getStartWeek() || sap1 > t.getDeadlineweek() + Math.abs(penality)) {
//                                            System.out.println("Motivarea nu este buna!");
//                                            motivari -= 1;
//                                        }
//                                    }
//                                    if (Math.abs(penality) - motivari >= 3) {
//                                        System.out.println("Prea putine motivari! Studentul va primi nota 1!");
//                                        try {
//                                            notaService.addGrade(s, t, 1, "Nu ai trimis tema si nu ai destule motivari!", 99);
//                                        } catch (ValidationException e) {
//                                            System.out.println(e.getMessage());
//                                        }
//                                    } else {
//                                        System.out.println("Studentul mai are la dispozitie timp!");
//                                    }
//                                } else {
//                                    System.out.println("Studentul va primi nota 1");
//                                    try {
//                                        notaService.addGrade(s, t, 1, "Nu ai trimis tema!", 99);
//                                    } catch (ValidationException e) {
//                                        System.out.println(e.getMessage());
//                                    }
//                                }
//                            }
//
//                        }

//                    }
//                else if (cmd.equals("Filter by group")){
//                        System.out.println("Insert group: ");
//                    String gr=reader.readLine();
//                    while (!gr.matches("[0-9]{3}")){
//                        System.out.println("Wrong group");
//                        System.out.println("Insert group: ");
//                        gr=reader.readLine();
//                    }
//                    List<Student> rez=studentService.filter_by_group(gr);
//                    rez.forEach(s-> System.out.println(s));
//                    if (rez.isEmpty())
//                        System.out.println("There is no student in this group!");
//                    }
//
//                else if (cmd.equals("Filter students by hw")){
//                        System.out.println("Insert number: ");
//                        String nr=reader.readLine();
//                        while (!nr.matches("[0-9]?")){
//                            System.out.println("Wrong number");
//                            System.out.println("Insert number: ");
//                            nr=reader.readLine();
//                        }
//                        List<Student> rez=notaService.filter_students_with_hw(Integer.parseInt(nr));
//                        rez.forEach(s-> System.out.println(s));
//                        if (rez.isEmpty())
//                            System.out.println("There is no student with this hw!");
//                    }
//                    else if (cmd.equals("Filter students by hw and prof")){
//                        System.out.println("Insert number: ");
//                        String nr=reader.readLine();
//                        while (!nr.matches("[0-9]?")){
//                            System.out.println("Wrong number");
//                            System.out.println("Insert number: ");
//                            nr=reader.readLine();
//                        }
//                        System.out.println("Insert prof name: ");
//                        String name=reader.readLine();
//                        List<Student> rez=notaService.filter_students_with_hw_teacher(Integer.parseInt(nr),name);
//                        rez.forEach(s-> System.out.println(s));
//                        if (rez.isEmpty())
//                            System.out.println("There is no student with this hw & proffesor!");
//                    }
//                    else if (cmd.equals("Filter grade by hw and week")){
//                        System.out.println("Insert number: ");
//                        String nr=reader.readLine();
//                        while (!nr.matches("[0-9]?")){
//                            System.out.println("Wrong number");
//                            System.out.println("Insert number: ");
//                            nr=reader.readLine();
//                        }
//                        System.out.println("Insert week: ");
//                        String week=reader.readLine();
//                        while (!week.matches("[1][0-4]") && !week.matches("[1-9]")){
//                            System.out.println("Wrong number");
//                            System.out.println("Insert number: ");
//                            week=reader.readLine();
//                        }
//                        List<Float> rez=notaService.filter_grades_in_week_of_hw(Integer.parseInt(nr),Integer.parseInt(week));
//                        rez.forEach(s-> System.out.println(s));
//                        if (rez.isEmpty())
//                            System.out.println("There is no grade in this week for this hw!");
//                    }
                } catch (NumberFormatException e) {
                    System.out.println("Value must be number!!!");
                } catch (DateTimeException e) {
                    System.out.println("You wrote a wrong date!");

                }
            }
    }
}