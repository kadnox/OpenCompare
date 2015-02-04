package org.diverse.pcm;

import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.impl.io.KMFJSONLoader;
import static org.junit.Assert.*;

import org.diverse.pcm.api.java.io.PCMLoader;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by gbecan on 02/02/15.
 */
public class MyPCMPrinterTest {

    @Test
    public void testMyPCMPrinter() throws FileNotFoundException {

        // Load a PCM
        File pcmFile = new File("pcms/example.pcm");
        PCMLoader loader = new KMFJSONLoader();
        PCM pcm = loader.load(pcmFile);
        assertNotNull(pcm);

        // Execute the printer
        MyPCMPrinter myPrinter = new MyPCMPrinter();
        myPrinter.print(pcm);


    }

}
