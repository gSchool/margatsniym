package com.houkcorp.margatsniym.events;

import com.houkcorp.margatsniym.models.Media;

/**
 * AN event for notifying when Media has changed.  It returns the modified media.
 */
public class MediaChangedEvent {
    private Media media;

    public MediaChangedEvent(Media media) {
        this.media = media;
    }

    public Media getMedia() {
        return media;
    }
}