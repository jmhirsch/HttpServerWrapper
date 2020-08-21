package com.flynnbuc.httpserverwrapper.services;

/**
 * Returns boolean value representing whether the current system is running MacOS
 */
public class OSType {
    private static final boolean isMacOS = System.getProperty("os.name").toLowerCase().contains("mac");

    /**
     * Returns boolean representation of whether the current system is running MacOS
     *
     * @return true if system is running on macOS, false otherwise
     */
    public static boolean systemIsMacOS(){
        return isMacOS;
    }
}
