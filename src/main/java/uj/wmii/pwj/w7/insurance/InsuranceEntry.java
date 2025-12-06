
package uj.wmii.pwj.w7.insurance;

public class InsuranceEntry {
    private final String county;
    private final double tiv2011;
    private final double tiv2012;

    public InsuranceEntry(String county, double tiv2011, double tiv2012) {
        this.county = county;
        this.tiv2011 = tiv2011;
        this.tiv2012 = tiv2012;
    }

    public String getCounty() { return county; }
    public double getTiv2011() { return tiv2011; }
    public double getTiv2012() { return tiv2012; }

    public static InsuranceEntry fromCsvLine(String line) {
        String[] cols = line.split(",", -1);
        String county = cols[2];
        double tiv2011 = Double.parseDouble(cols[7]);
        double tiv2012 = Double.parseDouble(cols[8]);
        return new InsuranceEntry(county, tiv2011, tiv2012);
    }
}