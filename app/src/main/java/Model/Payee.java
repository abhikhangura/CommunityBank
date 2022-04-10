package Model;

public class Payee {

    private String Name,payeeEmail;

    public Payee() {
    }

    public Payee(String name, String payeeEmail) {
        Name = name;
        this.payeeEmail = payeeEmail;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPayeeEmail() {
        return payeeEmail;
    }

    public void setPayeeEmail(String payeeEmail) {
        this.payeeEmail = payeeEmail;
    }

    @Override
    public String toString() {
        return Name ;
    }
}
