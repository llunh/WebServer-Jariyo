package mvc.model;

public class RestaurantDTO {

    private int    id;
    private String name;
    private String address;
    private String category;
    private String phone;
    private String openingHours;
    private String imageFilename;
    private int    maxCapacity;

    public RestaurantDTO() {
        super();
    }

    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }

    public String getName()                         { return name; }
    public void setName(String name)                { this.name = name; }

    public String getAddress()                      { return address; }
    public void setAddress(String address)          { this.address = address; }

    public String getCategory()                     { return category; }
    public void setCategory(String category)        { this.category = category; }

    public String getPhone()                        { return phone; }
    public void setPhone(String phone)              { this.phone = phone; }

    public String getOpeningHours()                 { return openingHours; }
    public void setOpeningHours(String openingHours){ this.openingHours = openingHours; }

    public String getImageFilename()                { return imageFilename; }
    public void setImageFilename(String imageFilename){ this.imageFilename = imageFilename; }

    public int getMaxCapacity()                     { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity)     { this.maxCapacity = maxCapacity; }
}