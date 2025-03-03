import Helper.Color;

abstract class StaffMember implements Color {
    protected int id;
    protected String name;
    protected String address;

    public StaffMember(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return id + "--" + name + "--" + address;
    }
}