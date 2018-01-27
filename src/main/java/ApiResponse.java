import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


public class ApiResponse {
    private String base;
    private RateObject rates;

    public ApiResponse() {
        this.rates = new RateObject();
    }

    public ApiResponse(String from, String to) {
        this.base = from;
        this.rates = new RateObject(to);
    }

    public String getBase() {
        return base;
    }

    public RateObject getRates() {
        return rates;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setRates(RateObject rates) {
        this.rates = rates;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ApiResponse response = (ApiResponse) obj;
        return new EqualsBuilder()
                .append(base, response.base)
                .append(rates.getName(), response.getRates().getName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(base)
                .append(rates.getName())
                .toHashCode();
    }
}
