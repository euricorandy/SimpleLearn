package umn.ac.simplelearn;

public class ProductIndo {

    private int id;
    private String title;
    private int image;
    private String link;

    public ProductIndo(int id, String title, int image, String link) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
