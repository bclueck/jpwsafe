package org.pwsafe.lib;

import org.pwsafe.lib.file.*;

import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        System.out.println("Loading your log file");
        try {
            PwsFile pwFile = PwsFileFactory.loadFile(args[0], new StringBuilder().append(args[1]));
            PwsFile pwFile2 = PwsFileFactory.newFile();
            pwFile2.setStorage(new PwsFileStorage("C:\\Development\\akka\\jpwsafe\\pwsafe64-3.44.0\\temp.psafe3"));
            pwFile2.setPassphrase(new StringBuilder("test"));
            for (Iterator<? extends PwsRecord> it = pwFile.getRecords(); it.hasNext(); ) {
                PwsRecord record = it.next();
                System.out.println("Name:"+record.getField(PwsFieldTypeV3.TITLE));
                System.out.println("Group:"+record.getField(PwsFieldTypeV3.GROUP));
                record.setField(new PwsStringUnicodeField(PwsFieldTypeV3.PASSWORD, "test"));
                pwFile2.add(record);
            }
            pwFile2.save();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}