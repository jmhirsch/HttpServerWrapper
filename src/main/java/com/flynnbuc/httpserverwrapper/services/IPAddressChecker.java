package com.flynnbuc.httpserverwrapper.services;

import com.flynnbuc.httpserverwrapper.exceptions.CouldNotFindIPException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


/**
 * <p>
 * Defines two methods to check for Local and Remote IP Addresses
 * </p>
 * <p>
 * Machine must be connected to the internet, otherwise CouldNotFindIPException will be thrown
 * </p>
 */
public class IPAddressChecker {

    /**
     * Returns Local IP Address <br>
     * On MacOS: Pings google.com, returns string version of local IP Address<br>
     * On Other systems: Requests the local IP of a Datagramsocket
     *
     * @return ip : String representation of local IP address
     * @throws CouldNotFindIPException Typically thrown when there is no connection
     */
    public static String getLocalIP() throws CouldNotFindIPException {
        String ip = null;

        if (OSType.systemIsMacOS()) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("google.com", 80));
                ip = socket.getLocalAddress().toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try (final DatagramSocket socket = new DatagramSocket()) {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                ip = socket.getLocalAddress().getHostAddress();
            } catch (SocketException | UnknownHostException e) {
                e.printStackTrace();
            }
        }

        if (ip == null){
            throw new CouldNotFindIPException();
        }

        return ip.replace("/", "");
    }

    /**
     * Returns remote IP Address - Should be the same accross multiple machines on the same LAN
     *
     * @return ip: String representation of the remote IP of this system
     * @throws CouldNotFindIPException Typically thrown when there is no connection
     */
    public static String getRemoteIP() throws CouldNotFindIPException{
        String ip = "";
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            ip = in.readLine(); //you get the IP as a String
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ip == null){
            throw new CouldNotFindIPException();
        }

        return ip;
    }
}
