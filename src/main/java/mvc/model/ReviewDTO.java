package mvc.model;

import java.util.List;

public class ReviewDTO {

    private int    id;
    private int    userId;
    private int    restaurantId;
    private int    rating;
    private String content;
    private String createdAt;
    private String username;        // JOIN용 (닉네임 표시)
    private String restaurantName;  // JOIN용
    private List<ReviewImageDTO> images;

    public ReviewDTO() {
        super();
    }

    public int getId()                                  { return id; }
    public void setId(int id)                           { this.id = id; }

    public int getUserId()                              { return userId; }
    public void setUserId(int userId)                   { this.userId = userId; }

    public int getRestaurantId()                        { return restaurantId; }
    public void setRestaurantId(int restaurantId)       { this.restaurantId = restaurantId; }

    public int getRating()                              { return rating; }
    public void setRating(int rating)                   { this.rating = rating; }

    public String getContent()                          { return content; }
    public void setContent(String content)              { this.content = content; }

    public String getCreatedAt()                        { return createdAt; }
    public void setCreatedAt(String createdAt)          { this.createdAt = createdAt; }

    public String getUsername()                         { return username; }
    public void setUsername(String username)            { this.username = username; }

    public String getRestaurantName()                   { return restaurantName; }
    public void setRestaurantName(String restaurantName){ this.restaurantName = restaurantName; }

    public List<ReviewImageDTO> getImages()             { return images; }
    public void setImages(List<ReviewImageDTO> images)  { this.images = images; }
}