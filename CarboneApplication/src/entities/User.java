package entities;

public class User {
    private int id;
    private String name;
    private int age;
    private String cin;

    public User(int id, String name, int age, String cin) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.cin = cin;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCin() {
        return cin;
    }

    @Override
    public String toString() {
        return "ID: " + id + "| Name: " + name + "| Age: " + age + "| CIN: " + cin;
    }
}
