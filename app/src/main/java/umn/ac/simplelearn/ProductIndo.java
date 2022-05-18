package umn.ac.simplelearn;

public class ProductIndo {

    private String title;
    private String link;

    public ProductIndo(){

    }

    public ProductIndo(String title, String link) {
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
