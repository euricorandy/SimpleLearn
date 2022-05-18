package umn.ac.simplelearn;

public class ProductIng {

    private String title;
    private String link;

    public ProductIng(){

    }

    public ProductIng(String title, String link) {
        if (title.trim().equals("")) {
            title = "No Title";
        }

        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
