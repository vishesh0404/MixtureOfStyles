package liftup.tech.mixtureofstyles.Activity.ui.saved;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SavedVideoList extends ViewModel {
    public String id;
    public String videourl;
    public String thumb;
    public String vname;
    public String iname;
    public String timeline;
    public String istyle;
    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    public void setText(String s){
        mutableLiveData.setValue(s);
    }
    public MutableLiveData<String> getText(){
        return mutableLiveData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getIname() {
        return iname;
    }

    public void setIname(String iname) {
        this.iname = iname;
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

    public SavedVideoList(String id, String videourl, String thumb, String vname, String iname, String timeline, String istyle) {
        this.id = id;
        this.videourl = videourl;
        this.thumb = thumb;
        this.vname = vname;
        this.iname = iname;
        this.timeline = timeline;
        this.istyle = istyle;
    }
}