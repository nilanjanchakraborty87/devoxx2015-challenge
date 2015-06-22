package pl.allegro.promo.devoxx2015.domain;

public class Offer {

    public static final double PRETTY_THRESHOLD = 0.7;
    private final String id;
    private final String title;
    private final String photoUrl;
    private final double photoScore;

    public Offer(String id, String title, String photoUrl, double photoScore) {
        this.id = id;
        this.title = title;
        this.photoUrl = photoUrl;
        this.photoScore = photoScore;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public double getPhotoScore() {
        return photoScore;
    }

    public boolean hasPrettyPhoto() {
        return photoScore >= PRETTY_THRESHOLD;
    }
}
