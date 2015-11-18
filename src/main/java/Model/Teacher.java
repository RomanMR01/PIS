package Model;

/**
 * Created by Vikno on 17.11.2015.
 */

/*
  POJO клас для обміну даними між БД та GUI
*/
public class Teacher {

    private int id;
    private String name;
    private long phone;
    private int exp;
    private String type;
    private String subject;
    private double rate;
    private double salary;

    public Teacher(int id, String name,long phone,int exp, String type,String subject,double rate,double salary) {
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

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setPhone(long phone)
    {
        this.phone = phone;
    }

    public long getPhone()
    {
        return phone;
    }
    public void setExp(int exp)
    {
        this.exp = exp;
    }

    public int getExp()
    {
        return exp;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getSubject()
    {
        return subject;
    }
    public void setRate(double rate)
    {
        this.rate = rate;
    }

    public double getRate()
    {
        return rate;
    }
    public void setSalary(double salary)
    {
        this.salary = salary;
    }

    public double getSalary()
    {
        return salary;
    }
}
