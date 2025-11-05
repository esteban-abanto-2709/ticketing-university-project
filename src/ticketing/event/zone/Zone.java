package ticketing.event.zone;

public class Zone {
    private final String eventCode;
    private final String name;
    private int capacity;
    private double price;

    public Zone(String eventCode, String name, int capacity, double price) {
        this.eventCode = eventCode;
        this.name = name;
        this.capacity = capacity;
        this.price = price;
    }

    public String getEventCode() {
        return eventCode;
    }

    public String getName() {
        return name;
    }


    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " | Cap: " + capacity + ", (S/ " + String.format("%.2f", price) + ")";
    }
}
