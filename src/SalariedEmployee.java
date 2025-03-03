import Helper.Payable;

public class SalariedEmployee implements Payable {
    private int id;
    private String name;
    private String address;
    private double salary;
    private double bonus;

    public SalariedEmployee(int id, String name, String address, double salary, double bonus) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.salary = salary;
        this.bonus = bonus;
    }

    @Override
    public double pay() {
        return salary + bonus;
    }

    @Override
    public String toString() {
        return "$" + salary + "--$" + bonus + "--$" + pay();
    }
}
