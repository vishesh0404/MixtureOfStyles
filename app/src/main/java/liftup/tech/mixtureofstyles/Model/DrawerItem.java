package liftup.tech.mixtureofstyles.Model;

public class DrawerItem {
    String ItemName;
    int imgResID;

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getImgResID() {
        return imgResID;
    }

    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

    public DrawerItem(String itemName, int imgResID) {
        ItemName = itemName;
        this.imgResID = imgResID;
    }
}
