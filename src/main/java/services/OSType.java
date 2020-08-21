package services;

public class OSType {
    private static final boolean isMacOS = System.getProperty("os.name").toLowerCase().contains("mac");

    public static boolean systemIsMacOS(){
        return isMacOS;
    }
}
