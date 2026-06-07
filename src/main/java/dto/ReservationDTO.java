package dto;

public class ReservationDTO {
    private int id;
    // user_id -> 로그인 기능 구현된 후에 
    private int restaurantId;
    private String reservationDate;
    private String reservationTime;
    private int partySize;
    private String status;
    
    
    public int getId() {return id;}
    public void setId(int id) { this.id = id; }
    
    // getter, setter -> user_id
    
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

