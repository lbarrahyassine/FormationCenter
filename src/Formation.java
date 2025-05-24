

import java.util.Date;

/**
 * Model class representing a formation/course
 */
public class Formation {
    private int formationId;
    private String title;
    private String description;
    private Date startDate;
    private int duree;
    private double price;

    // Constructor with all fields
    public Formation(int formationId, String title, String description,
                     Date startDate, int duree, double price) {
        this.formationId = formationId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.duree = duree;
        this.price = price;
    }

    public Formation() {

    }

    // Getters and setters
    public int getFormationId() {
        return formationId;
    }

    public void setFormationId(int formationId) {
        this.formationId = formationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }




    public int getduree() {
        return duree;
    }

    public void setduree(int maxParticipants) {
        this.duree = maxParticipants;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return this.title;
    }
}