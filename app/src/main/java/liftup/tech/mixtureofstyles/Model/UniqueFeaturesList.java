package liftup.tech.mixtureofstyles.Model;

public class UniqueFeaturesList {
    public String id;
    public String vname;
    public String vdescrip;
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

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getVdescrip() {
        return vdescrip;
    }

    public void setVdescrip(String vdescrip) {
        this.vdescrip = vdescrip;
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

    public UniqueFeaturesList(String id, String vname, String vdescrip, String uname, String vthub, String vurl, String timeline, String style) {
        this.id = id;
        this.vname = vname;
        this.vdescrip = vdescrip;
        this.uname = uname;
        this.vthub = vthub;
        this.vurl = vurl;
        this.timeline = timeline;
        this.style = style;
    }
}
