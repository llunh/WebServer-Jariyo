package mvc.model;

public class RestaurantDTO {

    private int    id;
    private String name;
    private String address;
    private String category;

    public RestaurantDTO() {
        super();
    }

    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }

    public String getName()                 { return name; }
    public void setName(String name)        { this.name = name; }

    public String getAddress()              { return address; }
    public void setAddress(String address)  { this.address = address; }

    public String getCategory()             { return category; }
    public void setCategory(String category){ this.category = category; }
}