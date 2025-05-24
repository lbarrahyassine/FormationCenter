
import java.util.Date;

/**
 * Model class representing enrollment of a client to a formation
 */
public class Enrollment {
    private int enrollmentId;
    private int clientId;
    private int formationId;
    private Date enrollmentDate;
    private String paymentStatus;

    // Client and Formation objects for displaying information
    private Client client;
    private Formation formation;

    // Constructor with all fields
    public Enrollment(int enrollmentId, int clientId, int formationId,
                      Date enrollmentDate, String paymentStatus) {
        this.enrollmentId = enrollmentId;
        this.clientId = clientId;
        this.formationId = formationId;
        this.enrollmentDate = enrollmentDate;
        this.paymentStatus = paymentStatus;
    }

    // Constructor for new enrollments
    public Enrollment(int clientId, int formationId, Date enrollmentDate, String paymentStatus) {
        this.clientId = clientId;
        this.formationId = formationId;
        this.enrollmentDate = enrollmentDate;
        this.paymentStatus = paymentStatus;
    }

    // Getters and setters
    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getFormationId() {
        return formationId;
    }

    public void setFormationId(int formationId) {
        this.formationId = formationId;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }
}