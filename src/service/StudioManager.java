package service;

import model.Studio;

import java.util.ArrayList;

public class StudioManager {
    ArrayList<Studio> studios = new ArrayList<Studio>();

    public Studio createStudio(String name) {
        if (getStudio(name) == null) {
            Studio studio = new Studio(name);
            studios.add(studio);
            return studio;
        }
        return null;
    }

    public void removeStudio(Studio studio) {
        studios.remove(studio);
    }

    public Studio getStudio(String name) {
        for (Studio studio : studios) {
            if (studio.getName().equals(name)){
                return studio;
            }
        }
        return null;
    }

    public ArrayList<Studio> getStudios() {
        return studios;
    }
}
