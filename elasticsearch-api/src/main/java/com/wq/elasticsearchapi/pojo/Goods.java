package com.wq.elasticsearchapi.pojo;

public class Goods {
    private String title;
    private String price;
    private String image;

    @Override
    public String toString() {
        return "Goods{" +
                "title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
