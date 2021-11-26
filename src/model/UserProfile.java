package model;

import java.io.Serializable;

public class UserProfile implements Serializable {
    public String id;
    public String username;

    private Studio studio;

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    public Studio getStudio() {
        return studio;
    }
}
