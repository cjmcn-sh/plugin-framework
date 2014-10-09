/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : iccs-common
 * user created : pippo
 * date created : 2011-1-27 - 上午11:04:54
 */
package com.sirius.utils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 2011-1-27
 * @author pippo
 */
public final class AddressUtils {

	private static Logger logger = LoggerFactory.getLogger(AddressUtils.class);

	private static String localIP = null;

	public static String getLocalIP() {
		if (StringUtils.isNotBlank(localIP)) {
			return localIP;
		}

		try {
			Enumeration<?> network_e = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
			while (network_e.hasMoreElements()) {
				NetworkInterface network = (NetworkInterface) network_e.nextElement();
				if (network == null || "".equals(network.getName().trim())) {
					continue;
				}

				if (network.getName().equals("lo")) {
					continue;
				}

				InetAddress address = getAddress(network);

				if (address != null) {
					localIP = address.getHostAddress();
					break;
				}

			}
		} catch (SocketException e) {
			logger.error("get local ip address due to error", e);
		}

		return localIP;
	}

	private static InetAddress getAddress(NetworkInterface network) {
		Enumeration<?> address_e = network.getInetAddresses();
		while (address_e.hasMoreElements()) {
			InetAddress address = (InetAddress) address_e.nextElement();
			if (address instanceof Inet6Address) {
				continue;
			}

			return address;
		}

		return null;
	}

}
