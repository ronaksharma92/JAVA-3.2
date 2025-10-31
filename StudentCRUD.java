package com.hibernateapp;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.util.List;
import java.util.Scanner;

public class StudentCRUD {
    private static SessionFactory factory;

    public static void main(String[] args) {
        factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        Scanner sc = new Scanner(System.in);

        int choice;
        do {
            System.out.println("\n--- Student CRUD Menu ---");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student Marks");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> addStudent(sc);
                case 2 -> viewStudents();
                case 3 -> updateStudent(sc);
                case 4 -> deleteStudent(sc);
                case 5 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 5);

        factory.close();
        sc.close();
    }

    private static void addStudent(Scanner sc) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        System.out.print("Enter Name: ");
        sc.nextLine();
        String name = sc.nextLine();
        System.out.print("Enter Marks: ");
        double marks = sc.nextDouble();

        Student student = new Student(name, marks);
        session.save(student);

        tx.commit();
        session.close();
        System.out.println("Student added successfully!");
    }

    private static void viewStudents() {
        Session session = factory.openSession();
        List<Student> list = session.createQuery("from Student", Student.class).list();
        list.forEach(s -> System.out.println(s.getId() + " | " + s.getName() + " | " + s.getMarks()));
        session.close();
    }

    private static void updateStudent(Scanner sc) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        System.out.print("Enter Student ID: ");
        int id = sc.nextInt();
        System.out.print("Enter New Marks: ");
        double marks = sc.nextDouble();

        Student s = session.get(Student.class, id);
        if (s != null) {
            s.setMarks(marks);
            session.update(s);
            tx.commit();
            System.out.println("Updated successfully!");
        } else {
            System.out.println("Student not found!");
        }
        session.close();
    }

    private static void deleteStudent(Scanner sc) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        System.out.print("Enter Student ID: ");
        int id = sc.nextInt();

        Student s = session.get(Student.class, id);
        if (s != null) {
            session.delete(s);
            tx.commit();
            System.out.println("Deleted successfully!");
        } else {
            System.out.println("Student not found!");
        }
        session.close();
    }
}
