package com.github.tartaricacid.touhoulittlemaid.ai.service;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.net.*;
import java.util.List;

public class ConfigProxySelector extends ProxySelector {
    private static final List<Proxy> NO_PROXY_LIST = List.of(Proxy.NO_PROXY);
    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    private final ForgeConfigSpec.ConfigValue<String> config;

    public ConfigProxySelector(ForgeConfigSpec.ConfigValue<String> config) {
        this.config = config;
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException e) {
    }

    @Override
    public synchronized List<Proxy> select(URI uri) {
        String scheme = uri.getScheme().toLowerCase();
        if (HTTP.equals(scheme) || HTTPS.equals(scheme)) {
            String value = this.config.get();
            return getProxyFromConfig(value.trim());
        } else {
            return NO_PROXY_LIST;
        }
    }

    private synchronized List<Proxy> getProxyFromConfig(String proxyAddress) {
        if (StringUtils.isBlank(proxyAddress)) {
            return NO_PROXY_LIST;
        }
        String[] split = proxyAddress.split(":", 2);
        if (split.length != 2) {
            return NO_PROXY_LIST;
        }
        String hostname = split[0];
        String portString = split[1];
        if (!StringUtils.isNumeric(portString) || !NumberUtils.isParsable(portString)) {
            return NO_PROXY_LIST;
        }
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostname, Integer.parseInt(portString)));
        return List.of(proxy);
    }
}
