package de.leonheuer.skycave.islandsystem.models;

import de.leonheuer.skycave.islandsystem.enums.IslandTemplate;

public class SelectionProfile {

    private IslandTemplate template;
    private boolean large;

    public SelectionProfile(IslandTemplate template, boolean large) {
        this.template = template;
        this.large = large;
    }

    public IslandTemplate getTemplate() {
        return template;
    }

    public void setTemplate(IslandTemplate template) {
        this.template = template;
    }

    public boolean isLarge() {
        return large;
    }

    public void setLarge(boolean large) {
        this.large = large;
    }
}
