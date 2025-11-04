package ticketing.sponsor;

public class Sponsor {
    private final String code;
    private String name;
    private String phone;
    private String address;

    public Sponsor(String code, String name, String phone, String address) {
        this.code = code;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}
