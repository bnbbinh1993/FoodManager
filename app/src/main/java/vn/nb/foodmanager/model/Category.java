package vn.nb.foodmanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category")
public class Category {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "categoryId")
    private int id;

    @ColumnInfo(name = "categoryName")
    private String name;
    @ColumnInfo(name = "categoryDescription")
    private String description;
    @ColumnInfo(name = "categoryDeleted")
    private boolean deleted;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Category(String name, String description) {

        this.name = name;
        this.description = description;
    }

    public Category() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
