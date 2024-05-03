package vn.nb.foodmanager.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food")
public class Food {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "foodId")
    private int id ;

    @ColumnInfo(name = "foodName")
    private String name;
    @ColumnInfo(name = "foodImage")
    private String image;
    @ColumnInfo(name = "foodPrice")
    private long price;

    @ColumnInfo(name = "foodServiceTime")
    private long serviceTime;

    @ColumnInfo(name = "category_Id")
    private int categoryId;

    @ColumnInfo(name = "foodDeleted")

    private boolean deleted;

    public Food( String name, String image, long price, long serviceTime, int categoryId, boolean deleted) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.serviceTime = serviceTime;
        this.categoryId = categoryId;
        this.deleted = deleted;
    }

    public Food() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(long serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
