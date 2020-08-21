package services;

import exceptions.CouldNotFindIPException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class IPAddressChecker {
    //Returns localIP of server
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

    //Returns remoteIP of server
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
