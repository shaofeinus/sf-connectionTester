package core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HostTester {

	private static final Logger LOG = Logger.getLogger(HostTester.class
			.getName());

	public HostTester() {

	}

	/**
	 * Looks for a list of IP addresses that are resolved to hostAddr
	 * 
	 * @param hostAddr
	 * @return List of IP addresses. Null if hostAddr is not a valid address.
	 */
	public List<String> getIPaddresses(String hostAddr) {
		try {
			LOG.info("Getting host IPs @ " + hostAddr);
			InetAddress[] addresses;
			addresses = InetAddress.getAllByName(hostAddr);
			List<String> ips = new ArrayList<String>();
			for (InetAddress addr : addresses) {
				String ip = addr.getHostAddress();
				ips.add(ip);
				LOG.info("IP: " + ip);

			}
			return ips;
		} catch (UnknownHostException e) {
			LOG.info("Invalid host address: " + hostAddr);
			return null;
		}
	}

	/**
	 * Test the connectivity to ipAddress and port
	 * 
	 * @param ipAddress
	 * @return true if success, false if failure
	 */
	public boolean testAddress(String ipAddress, int port) {
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(ipAddress, port), 1000);
			socket.close();
			LOG.info(ipAddress + ":" + port + " reachable");
			return true;
		} catch (IOException e) {
			LOG.info(ipAddress + ":" + port + " unreachable");
			return false;
		}
	}

}
