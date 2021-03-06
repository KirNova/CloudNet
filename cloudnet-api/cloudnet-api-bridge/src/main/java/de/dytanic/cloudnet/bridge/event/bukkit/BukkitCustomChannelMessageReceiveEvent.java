/*
 * Copyright (c) Tarek Hosni El Alaoui 2017
 */

package de.dytanic.cloudnet.bridge.event.bukkit;

import de.dytanic.cloudnet.lib.utility.document.Document;
import lombok.AllArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called if a custom channel message was received
 */
@AllArgsConstructor
public class BukkitCustomChannelMessageReceiveEvent extends BukkitCloudEvent {

    private static HandlerList handlerList = new HandlerList();

    private String channel;

    private String message;

    private Document document;

    public String getMessage()
    {
        return message;
    }

    public Document getDocument()
    {
        return document;
    }

    public String getChannel()
    {
        return channel;
    }

    public static HandlerList getHandlerList()
    {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlerList;
    }
}