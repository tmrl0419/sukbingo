package org.redwings.cameratest2;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rlarh on 2018-03-02.
 */

public class MultipartUtility {

    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private DataOutputStream dataOutputStream;
    private PrintWriter writer;

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String charset) throws IOException {
        this.charset = charset;

        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";

        URL url = new URL(requestURL);
        Log.e("URL", "URL : " + requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        dataOutputStream = new DataOutputStream(httpConn.getOutputStream());
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        try {
            dataOutputStream.writeBytes("--" + boundary + LINE_FEED);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_FEED);
            dataOutputStream.writeBytes("Content-Type: text/plain; charset=" + charset + LINE_FEED);
            dataOutputStream.writeBytes(LINE_FEED);
            dataOutputStream.writeBytes(value + LINE_FEED);
            dataOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile) throws IOException {
        String fileName = uploadFile.getName();
        dataOutputStream.writeBytes("--" + boundary + LINE_FEED);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\";filename=\"" + fileName + "\"" + LINE_FEED);
        dataOutputStream.writeBytes(LINE_FEED);

//        byte[] buffer = new byte[1024*1024];
//        if (fileName != null) {
//            FileInputStream fileInputStream = new FileInputStream(fileName);
//            int res = 1;
//            while ((res = fileInputStream.read(buffer)) > 0) {
//                OutputStream os = httpConn.getOutputStream();
//                os.write(buffer, 0, res);
//            }
//        }
//
//        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
//        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
//        writer.append(LINE_FEED);
//        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4*1024]; ///4MB
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            OutputStream os = httpConn.getOutputStream();
            os.write(buffer, 0, bytesRead);
        }

        inputStream.close();

        dataOutputStream.writeBytes(LINE_FEED);
        dataOutputStream.flush();
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public String finish() throws IOException {
        StringBuffer response = new StringBuffer();

        dataOutputStream.writeBytes(LINE_FEED);
        dataOutputStream.flush();
        dataOutputStream.writeBytes("--"+boundary+"--"+LINE_FEED);
        dataOutputStream.close();
        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }

        return response.toString();
    }
}