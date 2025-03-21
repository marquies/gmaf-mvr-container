package de.swa;


import de.swa.gmaf.plugin.GMAF_Plugin;
import de.swa.mmfg.MMFG;
import de.swa.mmfg.Node;

import java.io.File;
import java.net.URL;
import java.util.Vector;

public class MyGmafPlugin implements GMAF_Plugin {
    @Override
    public void process(URL url, File f, byte[] bytes, MMFG fv) {
        System.out.println("MyGmafPlugin process");
        System.out.println("Datei entpacken");
        System.out.println("Inhalt analysieren");
        Node node = new Node("Raumschiff", fv);

        fv.addNode(node);
    }

    @Override
    public boolean providesRecoursiveData() {
        return false;
    }

    @Override
    public boolean isGeneralPlugin() {
        return false;
    }

    @Override
    public Vector<Node> getDetectedNodes() {
        return null;
    }

    @Override
    public boolean canProcess(String extension) {
        System.out.println("I got asked if I can process the extension " + extension);
        return extension.equals(".mvr");
    }
}
