package ticketing.ticket;

public class Ticket {

    private final String eventCode;
    private final String zoneName;
    private final int ticketNumber;
    private String type;
    private String status;
    private Integer saleId;

    public Ticket(String eventCode, String zoneName, int ticketNumber) {
        this.eventCode = eventCode;
        this.zoneName = zoneName;
        this.ticketNumber = ticketNumber;
        this.status = "AVAILABLE";
    }

    public String getEventCode() {
        return eventCode;
    }

    public String getZoneName() {
        return zoneName;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    @Override
    public String toString() {
        return zoneName + " - #" + String.format("%04d", ticketNumber);
    }
}