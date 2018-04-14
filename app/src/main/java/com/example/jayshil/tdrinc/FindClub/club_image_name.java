package com.example.jayshil.tdrinc.FindClub;

/**
 * Created by jayshil on 10/17/17.
 */

public class club_image_name {

    public club_image_name(int image_id, String club_name){

        this.setClub_name(club_name);
        this.setImage_id(image_id);

    }

    private int image_id;
    private String club_name;

    public int getImage_id() {
        return image_id;
    }

    public String getClub_name() {
        return club_name;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public void setClub_name(String club_name) {
        this.club_name = club_name;
    }
}
