package aagym.mqdigital.com.privatgo.entity;

public class FavoriteTeacher {

    String name;
    String university;
    int price;
    int rate;
    String review;

    public FavoriteTeacher(String name, String university, int price, int rate, String review) {
        this.name = name;
        this.university = university;
        this.price = price;
        this.rate = rate;
        this.review = review;
    }

    public String getName() {
        return name;
    }

    public String getUniversity() {
        return university;
    }

    public int getPrice() {
        return price;
    }

    public int getRate() {
        return rate;
    }

    public String getReview() {
        return review;
    }
}
