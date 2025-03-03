import Helper.Payable;

public class HourlySalaryEmployee implements Payable {
    private int id;
    private String name;
    private String address;
    private int hourWorked;
    private double rate;

    public HourlySalaryEmployee(int id, String name, String address, int hourWorked, double rate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.hourWorked = hourWorked;
        this.rate = rate;
    }

    @Override
    public double pay() {
        return hourWorked * rate;
    }

    @Override
    public String toString() {
        return hourWorked + "--" + "$" + rate + "--$" + pay();
    }
}
