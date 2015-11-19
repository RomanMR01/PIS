package Model;

/**
 * Created by Vikno on 17.11.2015.
 */

/*
POJO що реалізує основні методи для класу Teacher
На основі цього класу відбувається обмін даними GUI з Базою даних.
 */
    /*
    WARNING!
    Назви гетерів і сетерів не змінювати.
    Вони використовуються при ініціалізації класу initialize() в MainController
    idColumn.setCellValueFactory(new PropertyValueFactory<Teacher, Integer>("id"));//getId = "id", getName = "name"
     */
public class Teacher {

    private int id;        // id для БД
    private String name;   // ім'я викладача
    private long phone;    // номер телефону
    private int exp;       // досвід роботи(стаж)
    private String type;   //тип заняття (лекція або практична)
    private String subject;//назва предмету
    private double rate;   //оплата за місяць з певного предмету
    private double salary; //заробітня плата за місяць

    /*
    Констуктор для ініціалізації даних
     */
    public Teacher(int id, String name, long phone, int exp, String type, String subject, double rate, double salary) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.exp = exp;
        this.type = type;
        this.subject = subject;
        this.rate = rate;
        this.salary = salary;
    }

    public Teacher() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
