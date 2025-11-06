package ticketing.event.ticketType;

public class TicketType {

    private final String eventCode;
    private final String name;
    private String description;
    private double price;

    public TicketType(String eventCode, String name, String description, double price) {
        this.eventCode = eventCode;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getEventCode() {
        return eventCode;
    }

    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
