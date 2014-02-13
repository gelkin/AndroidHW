package com.gmail.mazinva.RSSReaderService;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyService extends IntentService {
    protected static String url;
    protected List<RSSItem> items;

    private static final int KILOBYTE = 1024;

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String task = bundle.getString("task");
        if (task != null) {
            if (url != null) {
                if (task.equals("UPDATE")) {
                    RSSFetcher fetcher = new RSSFetcher();
                    fetcher.execute(url);
                    if (fetcher.getCause() != null) {
                        Toast.makeText(getApplicationContext(), fetcher.getCause(), 1).show();
                    }
                }
            }
            if (task.equals("SET_URL")) {
                url = bundle.getString("url");
                RSSFetcher fetcher = new RSSFetcher();
                fetcher.execute(url);
                if (fetcher.getCause() != null) {
                    Toast.makeText(getApplicationContext(), fetcher.getCause(), 1).show();
                }
            }
        }
    }

    private void setResult(List<RSSItem> items) {
        this.items = items;
        Intent intent = new Intent();
        intent.setAction("com.gmail.mazinva.ACTION.sendResult");
        intent.putExtra("items", (Serializable) items);
        sendBroadcast(intent);
    }

    private class RSSFetcher extends AsyncTask<String, Void, List<RSSItem>> {
        private String cause;

        public String getCause() {
            return cause;
        }

        @Override
        protected List<RSSItem> doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                inputStream = (new URL(strings[0])).openStream();
                String encoding;
                byte[] buf = new byte[KILOBYTE];
                int count = KILOBYTE;
                for (int i = 0; i < KILOBYTE; i++) {
                    int ch = inputStream.read();
                    if (ch == -1) {
                        count = i;
                        break;
                    }
                    buf[i] = (byte) ch;
                }

                byte[] newBuf = new byte[count];
                System.arraycopy(buf, 0, newBuf, 0, count);
                String s = new String(newBuf);
                Pattern pattern = Pattern.compile("encoding=\"(.*?)\"");

                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    encoding = matcher.group(1);
                } else {
                    encoding = null;
                }

                MySAXParser parser = new MySAXParser(new SequenceInputStream(new ByteArrayInputStream(newBuf),
                                                                         inputStream),
                                                    encoding);
                List<RSSItem> items = parser.parse();
                return items;
                
            } catch (MalformedURLException e) {
                cause = getString(R.string.WrongURL);
                return null;
            } catch (SAXException e) {
                cause = getString(R.string.RSSLost);
                return null;
            } catch (UnsupportedEncodingException e) {
                cause = getString(R.string.RSSLost);
                return null;
            } catch (IOException e) {
                cause = getString(R.string.ChannelReadingError);
                return null;
            } catch (ParserConfigurationException e) {
                cause = getString(R.string.ChannelReadingError);
                return null;
            } catch (Exception e) {
                cause = getString(R.string.ChannelReadingError);
                return null;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(List<RSSItem> items) {
            setResult(items);
        }
    }
}
