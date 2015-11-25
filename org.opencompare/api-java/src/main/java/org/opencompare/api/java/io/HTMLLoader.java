package org.opencompare.api.java.io;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.opencompare.api.java.*;
import org.w3c.dom.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbecan on 4/2/15.
 */
public class HTMLLoader implements PCMLoader {

    private PCMFactory factory;
    private boolean productsAsLines;
    private IOMatrixLoader ioMatrixLoader;

    public HTMLLoader(PCMFactory factory) {
        this(factory, true);
    }

    public HTMLLoader(PCMFactory factory, boolean productsAsLines) {
        this.factory = factory;
        this.productsAsLines = productsAsLines;

        if (this.productsAsLines) {
            ioMatrixLoader = new IOMatrixLoader(this.factory, PCMDirection.PRODUCTS_AS_LINES);
        } else {
            ioMatrixLoader = new IOMatrixLoader(this.factory, PCMDirection.PRODUCTS_AS_COLUMNS);
        }
    }

    private List<IOMatrix> createMatrices(Document doc) {
        List<IOMatrix> matrices = new ArrayList<>();
        String pageName = doc.head().getElementsByTag("title").text();
        int index = 0;
        Elements tables = doc.getElementsByTag("table");
        for (Element table: tables) {
            IOMatrix matrix = new IOMatrix();
            if (tables.size() > 1) {
                matrix.setName(pageName + " #" + index);
            } else {
                matrix.setName(pageName);
            }

            int i = 0;
            for (Element line: table.getElementsByTag("tr")) {
                int j = 0;

                for (Element column: line.getAllElements()) {
                    if (column.tag().getName().equals("th") || column.tag().getName().equals("td")){
                        if (matrix.getCell(i, j) != null) {
                            j++;
                        }
                        int rowspan = 1;
                        int colspan = 1;
                        if (!column.attributes().get("rowspan").isEmpty()) {
                            rowspan = Integer.valueOf(column.attributes().get("rowspan"));
                        }
                        if (!column.attributes().get("colspan").isEmpty()) {
                            colspan = Integer.valueOf(column.attributes().get("colspan"));
                        }



                        matrix.setCell(new IOCell(cellToText(column)), i, j, rowspan, colspan);
                        j += colspan;
                    }
                }
                i++;
            }
            matrices.add(matrix);
            index++;
        }
        return matrices;
    }

    private String cellToText(Element element) {
        String text = "";
        for (Node node : element.childNodes()) {
            switch (node.nodeName()) {
                case "#text":
                    TextNode tn = (TextNode) node;
                    text += tn.getWholeText();
                    break;
                case "br":
                    text += "\n";
                    break;
                default:
            }

        }
        return text;
    }

    @Override
    public List<PCMContainer> load(String pcm) {
        Document doc = Jsoup.parse(pcm);
        List<IOMatrix> matrices = createMatrices(doc);
        List<PCMContainer> containers = load(matrices.get(0));
        return containers;
    }

    @Override
    public List<PCMContainer> load(File file) throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");
        List<PCMContainer> containers = new ArrayList<>();
        for (IOMatrix matrix: createMatrices(doc)) {
            containers.add(load(matrix).get(0));
        }
        return containers;
    }

    public List<PCMContainer> load(IOMatrix matrix) {
        List<PCMContainer> result = new ArrayList<>();
        result.add(ioMatrixLoader.load(matrix));
        return result;
    }

}

