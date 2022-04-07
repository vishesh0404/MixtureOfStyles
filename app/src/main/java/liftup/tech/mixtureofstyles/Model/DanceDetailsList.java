package liftup.tech.mixtureofstyles.Model;

public class DanceDetailsList {
    public String id;
    public String vname;
    public String vdesc;
    public String videourl;
    public String thumb;
    public String timeline;
    public String style;
    public String level;

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

    public String getVdesc() {
        return vdesc;
    }

    public void setVdesc(String vdesc) {
        this.vdesc = vdesc;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public DanceDetailsList(String id, String vname, String vdesc, String videourl, String thumb, String timeline, String style, String level) {
        this.id = id;
        this.vname = vname;
        this.vdesc = vdesc;
        this.videourl = videourl;
        this.thumb = thumb;
        this.timeline = timeline;
        this.style = style;
        this.level = level;
    }
}
