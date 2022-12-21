package de.paradubsch.paradubschmanager.config;

import org.bukkit.Bukkit;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.io.File;

public class WebserverManager {
    private Server webServer;
    private ServerConnector webServerConnector;

    public WebserverManager startWebserver() {
        QueuedThreadPool threadPool = new QueuedThreadPool(
                ConfigurationManager.getInt("http.maxThreads"),
                ConfigurationManager.getInt("http.minThreads"),
                ConfigurationManager.getInt("http.idleTimeout"));
        webServer = new Server(threadPool);

        webServerConnector = new ServerConnector(webServer);
        webServerConnector.setPort(ConfigurationManager.getInt("http.port"));
        webServer.setConnectors(new Connector[] {webServerConnector});

        ContextHandler ctx = new ContextHandler("/downloadBackup");
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);
        resourceHandler.setResourceBase("." + File.separator + "plugins" + File.separator + "WorldEdit" + File.separator + "uploadSchematics");
        ctx.setHandler(resourceHandler);
        webServer.setHandler(ctx);

        try {
            webServer.start();
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to start web service");
            Bukkit.getLogger().warning(e.getMessage());
        }
        return this;
    }

    public void stopWebserver() {
        try {
            webServer.removeConnector(webServerConnector);
        } catch (NoClassDefFoundError ex) {
            Bukkit.getLogger().warning("NoClassDefFoundError while stopping webserver");
        }

        webServerConnector.getConnectedEndPoints().forEach(EndPoint::close);

        try {
            webServer.stop();
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to stop web service");
            Bukkit.getLogger().warning(e.getMessage());
        }
        webServer.destroy();
    }

    public static void clearSchematicFiles() {
        File index = new File("." + File.separator + "plugins" + File.separator + "WorldEdit" + File.separator + "uploadSchematics");
        if (index.exists()) {
            String[] entries = index.list();
            if (entries != null) {
                for (String s : entries) {
                    File currentFile = new File(index.getPath(), s);
                    if (!currentFile.delete()) {
                        Bukkit.getLogger().warning("Could not delete file " + currentFile.getName());
                    }
                }
            }
        } else {
            if (!index.mkdir()) {
                Bukkit.getLogger().warning("Could not create directory " + index.getName());
            }
        }
    }
}
