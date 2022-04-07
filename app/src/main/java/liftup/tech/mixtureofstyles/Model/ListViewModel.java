package liftup.tech.mixtureofstyles.Model;

public class ListViewModel {
    public String lid;
    public String name;
    public String uname;
    public String style;
    public String video;
    public String pimage;

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public ListViewModel(String lid, String name, String uname, String style, String video, String pimage) {
        this.lid = lid;
        this.name = name;
        this.uname = uname;
        this.style = style;
        this.video = video;
        this.pimage = pimage;
    }
}
