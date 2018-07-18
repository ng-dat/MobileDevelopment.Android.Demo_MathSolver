package com.com.com.mathsolver.Wolfram;

import android.app.FragmentManager;
import android.os.AsyncTask;

import com.com.com.mathsolver.Common.Store;
import com.com.com.mathsolver.R;
import com.com.com.mathsolver.ResultFragment;
import com.com.com.mathsolver.WolframModels.Pod;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by DatGatto on 7/17/2018.
 */

public class WolframConnector {
    private FragmentManager fragmentManager;
    private List<Pod> pods;
    public void GetSolution(String URL, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        new DownloadXmlTask().execute(URL);
    }

    private class DownloadXmlTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                LoadXmlFromNetwork(urls[0]);
                publishProgress();
                return "";
            } catch (IOException e) {
                return "connection error";
            } catch (XmlPullParserException e) {
                return "xml error";
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            Store.pods = pods;
            (new Store()).PodsToSolution();

            fragmentManager.beginTransaction().replace(R.id.content_frame,new ResultFragment()).commit();
        }
        @Override
        public void onPostExecute(String result) {
        }

        private void LoadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            WolframXmlParser wolframXmlParser = new WolframXmlParser();
            pods = null;
            try {
                stream = DownloadUrl(urlString);
                pods = wolframXmlParser.parse(stream);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

        }
        private InputStream DownloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }
    }
}
