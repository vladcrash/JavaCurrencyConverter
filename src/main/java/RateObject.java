public class RateObject {
    private String name;
    private double rate;

    public RateObject(String name, double rate) {
        this.name = name;
        this.rate = rate;
    }

    public RateObject(String name) {
        this.name = name;
    }

    public RateObject() {

    }

    public String getName() {
        return name;
    }

    public double getRate() {
        return rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }


}
