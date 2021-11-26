package service;

import model.Studio;

import java.util.ArrayList;

public class StudioManager {
    ArrayList<Studio> studios;

    public StudioManager() {
        studios = new ArrayList<>();
    }

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

    public Studio getOrCreateStudio(String name) {
        Studio studio = getStudio(name);
        if (studio != null) return studio;
        return createStudio(name);
    }

    public ArrayList<Studio> getStudios() {
        return studios;
    }
}
