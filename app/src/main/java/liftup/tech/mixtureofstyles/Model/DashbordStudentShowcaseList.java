package liftup.tech.mixtureofstyles.Model;

public class DashbordStudentShowcaseList {
    public String id;
    public String uname;
    public String vthub;
    public String vurl;
    public String timeline;
    public String style;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getVthub() {
        return vthub;
    }

    public void setVthub(String vthub) {
        this.vthub = vthub;
    }

    public String getVurl() {
        return vurl;
    }

    public void setVurl(String vurl) {
        this.vurl = vurl;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public DashbordStudentShowcaseList(String id, String uname, String vthub, String vurl, String timeline, String style) {
        this.id = id;
        this.uname = uname;
        this.vthub = vthub;
        this.vurl = vurl;
        this.timeline = timeline;
        this.style = style;
    }
}
