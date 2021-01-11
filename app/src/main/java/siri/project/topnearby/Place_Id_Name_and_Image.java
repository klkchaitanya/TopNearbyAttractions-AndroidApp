package siri.project.topnearby;

public class Place_Id_Name_and_Image {

    String placeId;
    String placeImage;
    String placeTitle;

    public Place_Id_Name_and_Image(String id, String name, String image)
    {
        this.placeId = id;
        this.placeTitle = name;
        this.placeImage = image;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceImage() {
        return placeImage;
    }

    public void setPlaceImage(String placeImage) {
        this.placeImage = placeImage;
    }

    public String getPlaceTitle() {
        return placeTitle;
    }

    public void setPlaceTitle(String placeTitle) {
        this.placeTitle = placeTitle;
    }


}
