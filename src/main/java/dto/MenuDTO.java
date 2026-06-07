package dto;

// menus 테이블의 데이터를 담는 객체
public class MenuDTO {

	private int id;
	private int restaurantId;   // 해당 메뉴가 속한 식당 ID
	private String name;
	private int price;
	private String description;
	private String category;
	private String imageFilename;

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public int getRestaurantId() { return restaurantId; }
	public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public int getPrice() { return price; }
	public void setPrice(int price) { this.price = price; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public String getCategory() { return category; }
	public void setCategory(String category) { this.category = category; }

	public String getImageFilename() { return imageFilename; }
	public void setImageFilename(String imageFilename) { this.imageFilename = imageFilename; }

}
