package liftup.tech.mixtureofstyles.Model;

public class ShowYearList {
    public String yid;
    public String year;

    public String getYid() {
        return yid;
    }

    public void setYid(String yid) {
        this.yid = yid;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public ShowYearList(String yid, String year) {
        this.yid = yid;
        this.year = year;
    }
}
