package com.houkcorp.margatsniym.events;

import com.houkcorp.margatsniym.models.Media;

public class MediaChangedEvent {
    private Media media;

    public MediaChangedEvent(Media media) {
        this.media = media;
    }

    public Media getMedia() {
        return media;
    }
}