package siri.project.topnearby;

import org.json.JSONObject;

public class Place_All_Details {

    String place_id;
    String place_name;
    String place_rating;
    String place_phone_number;
    String place_address;
    String place_website;
    JSONObject place_opening_hours;
    String place_build_photo;





    public Place_All_Details(String place_name, String place_rating, String place_phone_number, String place_address, String place_website, JSONObject place_opening_hours, String place_build_photo)
    {
        this.place_address = place_address;
        this.place_phone_number = place_phone_number;
        this.place_name = place_name;
        this.place_rating = place_rating;
        this.place_website = place_website;
        this.place_opening_hours = place_opening_hours;
        this.place_build_photo = place_build_photo;
    }

    public String getPlace_build_photo() {
        return place_build_photo;
    }

    public void setPlace_build_photo(String place_build_photo) {
        this.place_build_photo = place_build_photo;
    }

    public JSONObject getPlace_opening_hours() {
        return place_opening_hours;
    }

    public void setPlace_opening_hours(JSONObject place_opening_hours) {
        this.place_opening_hours = place_opening_hours;
    }

    public String getPlace_address() {
        return place_address;
    }

    public void setPlace_address(String place_address) {
        this.place_address = place_address;
    }

    public String getPlace_phone_number() {
        return place_phone_number;
    }

    public void setPlace_phone_number(String place_phone_number) {
        this.place_phone_number = place_phone_number;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPlace_rating() {
        return place_rating;
    }

    public void setPlace_rating(String place_rating) {
        this.place_rating = place_rating;
    }

    public String getPlace_website() {
        return place_website;
    }

    public void setPlace_website(String place_website) {
        this.place_website = place_website;
    }


}
