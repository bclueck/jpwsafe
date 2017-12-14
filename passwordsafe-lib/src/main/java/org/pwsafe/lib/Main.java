package org.pwsafe.lib;

public class Main {
    public static void main(String[] args) {
        System.out.println("Loading your log file");
        try {
        org.pwsafe.lib.file.PwsFileFactory.loadFile(args[0], new StringBuilder().append(args[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}