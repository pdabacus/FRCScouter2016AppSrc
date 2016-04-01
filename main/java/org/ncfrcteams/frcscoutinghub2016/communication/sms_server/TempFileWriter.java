package org.ncfrcteams.frcscoutinghub2016.communication.sms_server;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Kyle Brown on 3/17/2016.
 */
public class TempFileWriter {
    public static final String DELIM = ";";
    private File subdirFile;
    private String prefix;
    private int numFiles;

    public static TempFileWriter openTempFileWriter(Context context, String subdir) {
        File subdirFile = new File(context.getCacheDir(),subdir);
        String prefix = subdir + "_";

        if(!subdirFile.exists()) {
            subdirFile.mkdir();
        } else {
            if(!subdirFile.isDirectory())
                return null;
        }

        TempFileWriter tempFileWriter = new TempFileWriter(subdirFile,prefix);
        tempFileWriter.clear();

        return tempFileWriter;
    }

    private TempFileWriter(File subdirFile, String prefix) {
        this.subdirFile = subdirFile;
        this.prefix = prefix;

        final TempFileWriter thisOne = this;
        this.numFiles = subdirFile.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith(thisOne.prefix);
            }
        }).length;
    }

    private void clear() {
        File[] files = subdirFile.listFiles();

        for(File file : files) {
            file.delete();
        }

        numFiles = 0;
    }

    public File createFile(String[] contents) {
        File output = new File(subdirFile,prefix + numFiles);

        try {
            output.createNewFile();
            PrintWriter outputStream = new PrintWriter(output);

            StringBuilder stringBuilder = new StringBuilder();

            for(String line : contents) {
                stringBuilder.append(line);
                stringBuilder.append(DELIM);
            }

            outputStream.write(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        numFiles++;
        return output;
    }
}
