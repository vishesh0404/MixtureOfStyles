package liftup.tech.mixtureofstyles.Model;

public class PhotosWorkshopList {
    public String Iid;
    public String Ilogin;
    public String Vid;
    public String vname;
    public String videourl;
    public String thumb;
    public String timeline;
    public String istyle;
    public String vdescri;

    public String getIid() {
        return Iid;
    }

    public void setIid(String iid) {
        Iid = iid;
    }

    public String getIlogin() {
        return Ilogin;
    }

    public void setIlogin(String ilogin) {
        Ilogin = ilogin;
    }

    public String getVid() {
        return Vid;
    }

    public void setVid(String vid) {
        Vid = vid;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
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

    public String getIstyle() {
        return istyle;
    }

    public void setIstyle(String istyle) {
        this.istyle = istyle;
    }

    public String getVdescri() {
        return vdescri;
    }

    public void setVdescri(String vdescri) {
        this.vdescri = vdescri;
    }

    public PhotosWorkshopList(String iid, String ilogin, String vid, String vname, String videourl, String thumb, String timeline, String istyle, String vdescri) {
        Iid = iid;
        Ilogin = ilogin;
        Vid = vid;
        this.vname = vname;
        this.videourl = videourl;
        this.thumb = thumb;
        this.timeline = timeline;
        this.istyle = istyle;
        this.vdescri = vdescri;
    }
}
