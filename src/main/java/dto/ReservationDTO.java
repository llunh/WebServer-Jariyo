package dto;

public class ReservationDTO {
    private int id;
    private int userId;
    private int restaurantId;
    private String reservationDate;
    private String reservationTime;
    private int partySize;
    private String status;
    
    
    public int getId() {return id;}
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRestaurantId() {return restaurantId;}
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }
    
    public String getReservationDate() { return reservationDate; }
    public void setReservationDate(String reservationDate) { this.reservationDate = reservationDate; }

    public String getReservationTime() { return reservationTime; }
    public void setReservationTime(String reservationTime) { this.reservationTime = reservationTime; }

    public int getPartySize() { return partySize; }
    public void setPartySize(int partySize) { this.partySize = partySize; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

