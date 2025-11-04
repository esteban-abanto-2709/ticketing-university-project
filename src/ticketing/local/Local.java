package ticketing.local;

public class Local {

    private final String code;
    private String name;
    private String address;
    private int capacity;

    public Local(String code, String name, String address, int capacity) {
        this.code = code;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "[" + code + "] " + name;
    }
}
