package org.ladysnake.satin.api.experimental;

import org.apiguardian.api.API;

@API(status=API.Status.EXPERIMENTAL)
public interface ReadableDepthFramebuffer {
    @API(status=API.Status.EXPERIMENTAL)
    public int getStillDepthMap();

    @API(status=API.Status.EXPERIMENTAL)
    public void freezeDepthMap();
}
