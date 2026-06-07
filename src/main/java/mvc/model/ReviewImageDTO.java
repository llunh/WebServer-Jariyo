package mvc.model;

public class ReviewImageDTO {

    private int    id;
    private int    reviewId;
    private String fileName;
    private String oriName;

    public ReviewImageDTO() {
        super();
    }

    public int getId()                        { return id; }
    public void setId(int id)                 { this.id = id; }

    public int getReviewId()                  { return reviewId; }
    public void setReviewId(int reviewId)     { this.reviewId = reviewId; }

    public String getFileName()               { return fileName; }
    public void setFileName(String fileName)  { this.fileName = fileName; }

    public String getOriName()                { return oriName; }
    public void setOriName(String oriName)    { this.oriName = oriName; }
}