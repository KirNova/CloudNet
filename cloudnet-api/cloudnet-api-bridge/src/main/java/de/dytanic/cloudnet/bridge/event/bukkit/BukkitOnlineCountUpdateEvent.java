/*
 * Copyright (c) Tarek Hosni El Alaoui 2017
 */

package de.dytanic.cloudnet.bridge.event.bukkit;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Tareko on 11.10.2017.
 */
@Getter
public class BukkitOnlineCountUpdateEvent extends BukkitCloudEvent {

    private static HandlerList handlerList = new HandlerList();

    private int onlineCount;

    public BukkitOnlineCountUpdateEvent(int onlineCount)
    {
        this.onlineCount = onlineCount;
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