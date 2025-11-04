package ticketing.event.zone;

public class Zone {
    private String eventCode;
    private String name;
    private int capacity;
    private double basePrice;

    public Zone(String eventCode, String name, int capacity, double basePrice) {
        this.eventCode = eventCode;
        this.name = name;
        this.capacity = capacity;
        this.basePrice = basePrice;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public String toString() {
        return name + " (Capacidad: " + capacity + ", Precio base: " + basePrice + ")";
    }
}
