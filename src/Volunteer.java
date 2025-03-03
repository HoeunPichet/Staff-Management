import Helper.Payable;

public class Volunteer implements Payable {
    private int id;
    private String name;
    private String address;
    private double salary;

    public Volunteer(int id, String name, String address, double salary) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.salary = salary;
    }

    @Override
    public double pay() {
        return salary;
    }

    @Override
    public String toString() {
        return "$" + salary;
    }
}
