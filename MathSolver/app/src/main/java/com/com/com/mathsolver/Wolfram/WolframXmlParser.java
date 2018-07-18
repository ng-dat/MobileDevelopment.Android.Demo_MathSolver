package com.com.com.mathsolver.Wolfram;

import android.util.Xml;

import com.com.com.mathsolver.WolframModels.Pod;
import com.com.com.mathsolver.WolframModels.Subpod;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DatGatto on 7/14/2018.
 */


public class WolframXmlParser {
    // We don't use namespaces
    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readResult(parser);
        } finally {
            in.close();
        }
    }
    private List readResult(XmlPullParser parser) throws XmlPullParserException, IOException {
        List pods = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "queryresult");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("pod")) {
                pods.add(readPod(parser));
            } else {
                skip(parser);
            }
        }
        return pods;    }


    private Pod readPod (XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "pod");
        String title = "***";
        String scanner = "***";
        String id = "***";
        String position = "***";
        String error = "***";
        String numsubpods = "***";
        List<Subpod> subpods = new ArrayList();

        title = parser.getAttributeValue(null, "title");
        scanner = parser.getAttributeValue(null, "scanner");
        id = parser.getAttributeValue(null, "id");
        position = parser.getAttributeValue(null, "position");
        error = parser.getAttributeValue(null, "error");
        numsubpods = parser.getAttributeValue(null, "numsubpods");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("subpod")) {
                subpods.add(readSubpod(parser));
            }
            else {
                skip(parser);
            }
        }
        return new Pod(title,scanner,id,position,error,numsubpods,subpods);
    }

    private Subpod readSubpod (XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "subpod");
        String title = "***";
        String plaintext = "***";
        title = parser.getAttributeValue(null, "title");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("plaintext")) {
                plaintext = readPlaintext(parser);
            } else {
                skip(parser);
            }
        }
        return new Subpod(title,plaintext);
    }

    private String readPlaintext(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "plaintext");
        String plaintext = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "plaintext");
        return plaintext;
    }
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "***";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
