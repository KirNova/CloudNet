/*
 * Copyright (c) Tarek Hosni El Alaoui 2017
 */

package de.dytanic.cloudnet.bridge.internal.command.proxied;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.CloudProxy;
import de.dytanic.cloudnet.lib.DefaultType;
import de.dytanic.cloudnet.lib.NetworkUtils;
import de.dytanic.cloudnet.lib.database.Database;
import de.dytanic.cloudnet.lib.network.WrapperInfo;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import de.dytanic.cloudnet.lib.proxylayout.AutoSlot;
import de.dytanic.cloudnet.lib.server.ProxyGroup;
import de.dytanic.cloudnet.lib.server.ServerConfig;
import de.dytanic.cloudnet.lib.server.ServerGroup;
import de.dytanic.cloudnet.lib.server.SimpleServerGroup;
import de.dytanic.cloudnet.lib.server.info.ProxyInfo;
import de.dytanic.cloudnet.lib.server.info.ServerInfo;
import de.dytanic.cloudnet.lib.server.template.Template;
import de.dytanic.cloudnet.lib.utility.Acceptable;
import de.dytanic.cloudnet.lib.utility.CollectionWrapper;
import de.dytanic.cloudnet.lib.utility.document.Document;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tareko on 02.06.2017.
 */
public final class CommandCloud extends Command implements TabExecutor {

    public CommandCloud()
    {
        super("cloud", "cloudnet.command.cloud");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args)
    {
        if (args.length > 2)
        {
            if (args[0].equalsIgnoreCase("cmds") && commandSender.hasPermission("cloudnet.command.cloud.commandserver"))
            {
                if (CloudProxy.getInstance().getCachedServers().containsKey(args[1]))
                {
                    StringBuilder builder = new StringBuilder();

                    for (short i = 2; i < args.length; i++) builder.append(args[i]).append(" ");

                    CloudAPI.getInstance().sendConsoleMessage(DefaultType.BUKKIT, args[1], builder.substring(0, builder.length() - 1));
                    commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                            "The information was sent to the cloud");
                    return;
                }
            }
            if (args[0].equalsIgnoreCase("cmdp") && commandSender.hasPermission("cloudnet.command.cloud.commandproxy"))
            {
                StringBuilder builder = new StringBuilder();

                for (short i = 2; i < args.length; i++) builder.append(args[i]).append(" ");

                CloudAPI.getInstance().sendConsoleMessage(DefaultType.BUNGEE_CORD, args[1], builder.substring(0, builder.length() - 1));
                commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "The information was sent to the cloud");
                return;
            }
        }

        switch (args.length)
        {
            case 1:
                if (args[0].equalsIgnoreCase("help"))
                {
                    commandSender.sendMessages(
                            " ",
                            CloudAPI.getInstance().getPrefix() + "All command arguments",
                            "§7/cloud toggle autoslot",
                            "§7/cloud toggle maintenance",
                            "§7/cloud toggle maintenance <time>",
                            "§7/cloud setMaxPlayers <maxonlinecount>",
                            "§7/cloud whitelist <add : remove> <name>",
                            "§7/cloud start <group> <count>",
                            "§7/cloud start <group> <template>",
                            "§7/cloud startcs <name> <memory> <priorityStop>",
                            "§7/cloud cmds (command server) <server> <command>",
                            "§7/cloud cmdp (command proxy) <proxy> <command>",
                            "§7/cloud stop <serverId>",
                            "§7/cloud stopGroup <group>",
                            "§7/cloud ustopGroup <group>",
                            "§7/cloud listProxys",
                            "§7/cloud listOnline",
                            "§7/cloud listServers",
                            "§7/cloud log <server>",
                            "§7/cloud listGroups",
                            "§7/cloud rl",
                            "§7/cloud rlconfig",
                            "§7/cloud list",
                            "§7/cloud maintenance <group>",
                            "§7/cloud copy <server>",
                            "§7/cloud copy <server> <directory>",
                            "§7/cloud version",
                            "§7/cloud statistics",
                            " "
                    );
                    return;
                }
                if (args[0].equalsIgnoreCase("rl") && commandSender.hasPermission("cloudnet.command.cloud.reload"))
                {
                    CloudAPI.getInstance().sendCloudCommand("reload all");
                    commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                            "The information was sent to the cloud");
                    return;
                }
                if (args[0].equalsIgnoreCase("statistics") && commandSender.hasPermission("cloudnet.ommand.cloud.statistics"))
                {
                    Document document = CloudAPI.getInstance().getStatistics();

                    commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "Statistics");

                    for (String key : document.keys())
                        if (!Database.UNIQUE_NAME_KEY.equalsIgnoreCase(key))
                            commandSender.sendMessage( "§3" + key + "§8| §e " + document.get(key).toString());
                            return;
                }
                if (args[0].equalsIgnoreCase("version") && commandSender.hasPermission("cloudnet.command.cloud.version"))
                {
                    commandSender.sendMessage("CloudNet " + NetworkUtils.class.getPackage().getSpecificationVersion() + " #" + NetworkUtils.class.getPackage().getImplementationVersion() + " by Dytanic");
                    return;
                }
                if (args[0].equalsIgnoreCase("rlconfig") && commandSender.hasPermission("cloudnet.command.cloud.reloadconfig"))
                {
                    CloudAPI.getInstance().sendCloudCommand("reload config");
                    commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                            "The information was sent to the cloud");
                    return;
                }
                if (args[0].equalsIgnoreCase("list") && commandSender.hasPermission("cloudnet.command.cloud.list"))
                {
                    commandSender.sendMessage(" ");

                    int maxMemory = 0;
                    int usedMemory = 0;

                    for (WrapperInfo cnsInfo : CloudAPI.getInstance().getWrappers())
                    {
                        commandSender.sendMessage("§8[§7" + cnsInfo.getServerId() + "§8/§7" + cnsInfo.getHostName() + "§8] §7Cores: " + cnsInfo.getAvailableProcessors());
                        maxMemory = maxMemory + cnsInfo.getMemory();
                    }
                    commandSender.sendMessage(" ");
                    for (ProxyInfo simpleProxyInfo : CloudAPI.getInstance().getProxys())
                    {
                        commandSender.sendMessage("§8[§c" + simpleProxyInfo.getServiceId().getServerId() + "§8] §8(§e" + simpleProxyInfo.getOnlineCount() + "§8) : §7" + simpleProxyInfo.getMemory() + "MB");
                        usedMemory = usedMemory + simpleProxyInfo.getMemory();
                    }
                    commandSender.sendMessage(" ");
                    for (ServerInfo simpleProxyInfo : CloudAPI.getInstance().getServers())
                    {
                        TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText("§8[§c" + simpleProxyInfo.getServiceId().getServerId() + "§8] §8(§e" + simpleProxyInfo.getOnlineCount() + "§8) §e" + simpleProxyInfo.getServerState().name() + " §8: §7" + simpleProxyInfo.getMemory() + "MB"));
                        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + simpleProxyInfo.getServiceId().getServerId()));
                        commandSender.sendMessage(textComponent);
                        usedMemory = usedMemory + simpleProxyInfo.getMemory();
                    }
                    commandSender.sendMessage(" ");

                    commandSender.sendMessage("§7Memory in use: " + usedMemory + "§8/§7" + maxMemory + "MB");
                }
                if (args[0].equalsIgnoreCase("listProxys") && commandSender.hasPermission("cloudnet.command.listproxys"))
                {
                    commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "Proxys:");
                    for (ProxyInfo proxy : CloudAPI.getInstance().getProxys())
                    {
                        commandSender.sendMessage("§7- " + (proxy.isOnline() ? "§e" : "§c") + proxy.getServiceId().getServerId() + " §8(§e" + proxy.getOnlineCount() + "§8) ");
                    }
                    return;
                }
                if (args[0].equalsIgnoreCase("listServers") && commandSender.hasPermission("cloudnet.command.cloud.listservers"))
                {
                    commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "Server:");
                    for (ServerInfo server : CloudProxy.getInstance().getCachedServers().values())
                    {
                        TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText("§7- " + (server.isOnline() ? "§e" : "§c") + server.getServiceId().getServerId() + "§8(" + server.getOnlineCount() + "§8) §7State: " + server.getServerState()));
                        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + server.getServiceId().getServerId()));
                        commandSender.sendMessage(textComponent);
                    }
                    return;
                }
                if (args[0].equalsIgnoreCase("listOnline") && commandSender.hasPermission("cloudnet.command.cloud.listonline"))
                {
                    for (CloudPlayer playerWhereAmI : CloudAPI.getInstance().getOnlinePlayers())
                    {
                        commandSender.sendMessage("§7- §e" + playerWhereAmI.getName() + " §7on §e" + playerWhereAmI.getServer() + "/" + playerWhereAmI.getProxy());
                    }
                    return;
                }
                if (args[0].equalsIgnoreCase("listGroups") && commandSender.hasPermission("cloudnet.command.cloud.listgroups"))
                {
                    StringBuilder builder = new StringBuilder();

                    for (SimpleServerGroup group : CloudAPI.getInstance().getCloudNetwork().getServerGroups().values())
                    {
                        builder.append((!group.isMaintenance() ? "§e" : "§c")).append(group.getName()).append("§7, ");
                    }

                    commandSender.sendMessage("§7The following server groups are registered:");
                    commandSender.sendMessage(builder.substring(0));
                    return;
                }
                break;
            case 2:
                if (args[0].equalsIgnoreCase("toggle"))
                {
                    switch (args[1].toLowerCase())
                    {
                        case "autoslot":
                        {
                            if (commandSender.hasPermission("cloudnet.command.cloud.autoslot"))
                            {
                                ProxyGroup proxyGroup = CloudProxy.getInstance().getProxyGroup();
                                proxyGroup.getProxyConfig().setAutoSlot(new AutoSlot(proxyGroup.getProxyConfig().getAutoSlot().getDynamicSlotSize(),
                                        !proxyGroup.getProxyConfig().getAutoSlot().isEnabled()));
                                CloudAPI.getInstance().updateProxyGroup(proxyGroup);
                                commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "The autoslot state was updated.");
                            }
                        }
                        return;
                        case "maintenance":
                        {
                            if (commandSender.hasPermission("cloudnet.command.cloud.maintenance"))
                            {
                                ProxyGroup proxyGroup = CloudProxy.getInstance().getProxyGroup();
                                proxyGroup.getProxyConfig().setMaintenance(!proxyGroup.getProxyConfig().isMaintenance());
                                CloudAPI.getInstance().updateProxyGroup(proxyGroup);
                                commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "The maintenance state was updated.");
                            }
                            return;
                        }
                    }
                    return;
                }
                if (args[0].equalsIgnoreCase("log") && commandSender.hasPermission("cloudnet.command.cloud.log"))
                {
                    if (CloudProxy.getInstance().getCachedServers().containsKey(args[1]) ||
                            CollectionWrapper.filter(CloudAPI.getInstance().getProxys(), new Acceptable<ProxyInfo>() {
                                @Override
                                public boolean isAccepted(ProxyInfo proxyInfo)
                                {
                                    return proxyInfo.getServiceId().getServerId().equalsIgnoreCase(args[1]);
                                }
                            }) != null)
                    {
                        String url = CloudAPI.getInstance().createServerLogUrl(args[1]);
                        TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText("§n§l§b" + url));
                        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                        commandSender.sendMessage(new TextComponent(TextComponent.fromLegacyText(CloudAPI.getInstance().getPrefix() + "You can review the log at: ")), textComponent);
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "The log is dynamic and will be deleted on 10 minutes");
                    } else
                    {
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "The server doesn't exist.");
                    }
                    return;
                }
                if (args[0].equalsIgnoreCase("setMaxPlayers") && commandSender.hasPermission("cloudnet.command.cloud.setmaxplayers"))
                {
                    if (checkAsNumber(args[1]))
                    {
                        ProxyGroup proxyGroup = CloudProxy.getInstance().getProxyGroup();
                        proxyGroup.getProxyConfig().setMaxPlayers(Integer.parseInt(args[1]));
                        CloudAPI.getInstance().updateProxyGroup(proxyGroup);
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "The maximum onlinecount was updated.");
                    } else
                    {
                        commandSender.sendMessage("§7The second argument is not a number.");
                    }
                    return;
                }
                if (args[0].equalsIgnoreCase("start") && commandSender.hasPermission("cloudnet.command.cloud.start"))
                {
                    if (CloudAPI.getInstance().getCloudNetwork().getServerGroups().containsKey(args[1]))
                    {
                        CloudAPI.getInstance().startGameServer(CloudAPI.getInstance().getServerGroupData(args[1]), new ServerConfig(false, "extra", new Document(), System.currentTimeMillis()), true);
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                                "The information was sent to the cloud");
                    } else if (CloudAPI.getInstance().getCloudNetwork().getProxyGroups().containsKey(args[1]))
                    {
                        CloudAPI.getInstance().startProxy(CloudAPI.getInstance().getProxyGroupData(args[1]));
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                                "The information was sent to the cloud");
                    } else
                    {
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "The group doesn't exist.");
                    }
                    return;
                }
                if (args[0].equalsIgnoreCase("maintenance") && commandSender.hasPermission("cloudnet.command.cloud.maintenancegroup"))
                {
                    if (CloudAPI.getInstance().getServerGroupMap().containsKey(args[1]))
                    {
                        ServerGroup serverGroup = CloudAPI.getInstance().getServerGroup(args[1]);
                        serverGroup.setMaintenance(!serverGroup.isMaintenance());
                        CloudAPI.getInstance().updateServerGroup(serverGroup);
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                                "The information was sent to the cloud");
                    } else
                    {
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "The group doesn't exist.");
                    }
                    return;
                }
                if (args[0].equalsIgnoreCase("stop") && commandSender.hasPermission("cloudnet.command.cloud.stop"))
                {
                    if (CloudProxy.getInstance().getCachedServers().containsKey(args[1]))
                    {
                        CloudAPI.getInstance().stopServer(args[1]);
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                                "The information was sent to the cloud");
                    } else if (CollectionWrapper.filter(CloudAPI.getInstance().getProxys(), new Acceptable<ProxyInfo>() {
                        @Override
                        public boolean isAccepted(ProxyInfo proxyInfo)
                        {
                            return proxyInfo.getServiceId().getServerId().equalsIgnoreCase(args[1]);
                        }
                    }) != null)
                    {
                        CloudAPI.getInstance().stopProxy(args[1]);
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                                "The information was sent to the cloud");
                    } else
                    {
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "The specified server isn't online.");
                    }
                    return;
                }
                if (args[0].equalsIgnoreCase("stopGroup") && commandSender.hasPermission("cloudnet.command.cloud.stopgroup"))
                {
                    if (CloudAPI.getInstance().getServerGroupMap().containsKey(args[1]))
                    {
                        List<String> servers = CloudProxy.getInstance().getServers(args[1]);

                        for (String server : servers)
                            CloudAPI.getInstance().stopServer(server);

                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                                "The information was sent to the cloud");
                        return;
                    }

                    if (CloudAPI.getInstance().getProxyGroupMap().containsKey(args[1]))
                    {
                        Collection<ProxyInfo> servers = CloudAPI.getInstance().getProxys();

                        for (ProxyInfo proxyInfo : servers)
                            if (proxyInfo.getServiceId().getGroup().equalsIgnoreCase(args[1]))
                                CloudAPI.getInstance().stopProxy(proxyInfo.getServiceId().getServerId());

                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                                "The information was sent to the cloud");
                        return;
                    }

                    return;
                }
                if (args[0].equalsIgnoreCase("ustopGroup") && commandSender.hasPermission("cloudnet.command.cloud.useless-stopgroup"))
                {
                    if (CloudAPI.getInstance().getServerGroupMap().containsKey(args[1]))
                    {
                        Collection<ServerInfo> servers = CollectionWrapper.filterMany(CloudProxy.getInstance().getCachedServers().values(),
                                new Acceptable<ServerInfo>() {
                                    @Override
                                    public boolean isAccepted(ServerInfo serverInfo)
                                    {
                                        return serverInfo.getServiceId().getGroup() != null && serverInfo.getServiceId().getGroup().equalsIgnoreCase(args[1]);
                                    }
                                });

                        for (ServerInfo server : servers)
                            if (server.getOnlineCount() == 0)
                                CloudAPI.getInstance().stopServer(server.getServiceId().getServerId());

                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                                "The information was sent to the cloud");
                        return;
                    }

                    if (CloudAPI.getInstance().getProxyGroupMap().containsKey(args[1]))
                    {
                        Collection<ProxyInfo> servers = CloudAPI.getInstance().getProxys();

                        for (ProxyInfo proxyInfo : servers)
                            if (proxyInfo.getServiceId().getGroup().equalsIgnoreCase(args[1]) && proxyInfo.getOnlineCount() == 0)
                                CloudAPI.getInstance().stopProxy(proxyInfo.getServiceId().getServerId());

                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                                "The information was sent to the cloud");
                        return;
                    }

                    return;
                }
                if (args[0].equalsIgnoreCase("copy") && commandSender.hasPermission("cloudnet.command.cloud.copy"))
                {
                    CloudAPI.getInstance().sendCloudCommand("copy " + args[1]);
                    commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                            "The information was sent to the cloud");
                    return;
                }
                break;
            case 3:
                if(args[0].equalsIgnoreCase("copy"))
                {
                    if(!CloudProxy.getInstance().getCachedServers().containsKey(args[1]))
                    {
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "The server doesn't exists");
                        return;
                    }

                    CloudAPI.getInstance().copyDirectory(CloudProxy.getInstance().getCachedServers().get(args[1]),
                            args[2]);
                    commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "The wrapper tried to copy the directory...");

                    return;
                }
                if (args[0].equalsIgnoreCase("toggle"))
                {
                    switch (args[1].toLowerCase())
                    {
                        case "maintenance":
                        {
                            if (commandSender.hasPermission("cloudnet.command.cloud.maintenance"))
                            {
                                if (!NetworkUtils.checkIsNumber(args[2])) return;
                                ProxyServer.getInstance().getScheduler().schedule(CloudProxy.getInstance().getPlugin(), new Runnable() {
                                    @Override
                                    public void run()
                                    {
                                        ProxyGroup proxyGroup = CloudProxy.getInstance().getProxyGroup();
                                        proxyGroup.getProxyConfig().setMaintenance(!proxyGroup.getProxyConfig().isMaintenance());
                                        CloudAPI.getInstance().updateProxyGroup(proxyGroup);
                                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "The maintenance state was updated.");
                                    }
                                }, Integer.parseInt(args[2]), TimeUnit.SECONDS);
                                commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "The maintenance will be changed in " + args[2] + " seconds");
                            }
                            return;
                        }
                    }
                    return;
                }
                if (args[0].equalsIgnoreCase("start") && commandSender.hasPermission("cloudnet.command.cloud.start"))
                {
                    if (CloudAPI.getInstance().getServerGroupMap().containsKey(args[1]))
                    {
                        if (checkAsNumber(args[2]))
                        {
                            for (short i = 0; i < Integer.parseInt(args[2]); i++)
                            {
                                CloudAPI.getInstance().startGameServer(CloudAPI.getInstance().getServerGroupData(args[1]), new ServerConfig(false, "extra", new Document(), System.currentTimeMillis()), true);
                            }
                            commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                                    "The information was sent to the cloud");
                        } else
                        {
                            ServerGroup serverGroup = CloudAPI.getInstance().getServerGroup(args[1]);
                            if (CollectionWrapper.filter(serverGroup.getTemplates(), new Acceptable<Template>() {
                                @Override
                                public boolean isAccepted(Template value)
                                {
                                    return value.getName().equalsIgnoreCase(args[2]);
                                }
                            }) != null)
                            {
                                CloudAPI.getInstance().startGameServer(CloudAPI.getInstance().getServerGroupData(args[1]), new ServerConfig(false, "extra", new Document(), System.currentTimeMillis()), true, CollectionWrapper.filter(serverGroup.getTemplates(), new Acceptable<Template>() {
                                    @Override
                                    public boolean isAccepted(Template value)
                                    {
                                        return value.getName().equalsIgnoreCase(args[2]);
                                    }
                                }));
                                commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                                        "The information was sent to the cloud");
                            }
                        }
                    } else if (CloudAPI.getInstance().getProxyGroupMap().containsKey(args[1]))
                    {
                        if (checkAsNumber(args[2]))
                        {
                            for (short i = 0; i < Integer.parseInt(args[2]); i++)
                            {
                                CloudAPI.getInstance().startProxy(CloudAPI.getInstance().getProxyGroupData(args[1]));
                            }
                            commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                                    "The information was sent to the cloud");
                        } else
                        {
                            CloudAPI.getInstance().startProxy(CloudAPI.getInstance().getProxyGroupData(args[1]));
                            commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                                    "The information was sent to the cloud");
                        }
                    } else
                    {
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "The group doesn't exist.");
                    }
                } else if (args[0].equalsIgnoreCase("whitelist") && commandSender.hasPermission("cloudnet.command.cloud.whitelist"))
                {
                    if (args[1].equalsIgnoreCase("add"))
                    {
                        ProxyGroup proxyGroup = CloudProxy.getInstance().getProxyGroup();
                        if (proxyGroup.getProxyConfig().getWhitelist().contains(args[2]))
                        {
                            commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + " The user " + args[2] + " is already on the whitelist.");
                            return;
                        }
                        proxyGroup.getProxyConfig().getWhitelist().add(args[2]);
                        CloudAPI.getInstance().updateProxyGroup(proxyGroup);
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + " You added " + args[2] + " to the whitelist of the maintenance mode.");
                    } else if (args[1].equalsIgnoreCase("remove"))
                    {
                        ProxyGroup proxyGroup = CloudProxy.getInstance().getProxyGroup();
                        proxyGroup.getProxyConfig().getWhitelist().remove(args[2]);
                        CloudAPI.getInstance().updateProxyGroup(proxyGroup);
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + " You removed " + args[2] + " from the whitelist of the maintenance mode.");
                    }
                }
                break;
            case 4:
                if (args[0].equalsIgnoreCase("startcs") && commandSender.hasPermission("cloudnet.command.cloud.startcs"))
                {
                    if (NetworkUtils.checkIsNumber(args[2]) && Integer.parseInt(args[2]) > 128)
                    {
                        CloudAPI.getInstance().startCloudServer(args[1], Integer.parseInt(args[2]), args[3].equalsIgnoreCase("true"));
                        commandSender.sendMessage(CloudAPI.getInstance().getPrefix() +
                                "The information was sent to the cloud");
                    } else
                    {
                        commandSender.sendMessage("Invalid arguments!");
                    }
                }
                break;
            default:
                commandSender.sendMessage(CloudAPI.getInstance().getPrefix() + "Use /cloud help");
                break;
        }

    }

    private boolean checkAsNumber(String input)
    {
        try
        {
            Short.parseShort(input);
            return true;
        } catch (NumberFormatException e)
        {
            return false;
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args)
    {
        return new LinkedList<>(CloudProxy.getInstance().getCachedServers().keySet());
    }
}