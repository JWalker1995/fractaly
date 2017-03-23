package net.walker9.fractaly;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by joel on 2/22/17.
 */

public class HttpInterface {
    public static InputStream request(String endpoint, JSONObject post_data) throws HttpException {
        URL url = make_url(endpoint);
        HttpURLConnection conn = open_connection(url);

        String data = post_data.toString();
        begin_request(conn, data);
        write_request(conn, data);
        end_request(conn);

        try {
            int code = read_response_code(conn);
            if (code == 200) {
                return get_input_stream(conn);
            } else {
                throw new HttpException("HTTP error code " + String.valueOf(code), new Exception());
            }
        } finally {
            conn.disconnect();
        }
    }

    public static InputStream request(String endpoint, InputStream post_data) throws HttpException {
        URL url = make_url(endpoint);
        HttpURLConnection conn = open_connection(url);

        begin_request(conn, post_data);
        write_request(conn, post_data);
        end_request(conn);

        try {
            int code = read_response_code(conn);
            if (code == 200) {
                return get_input_stream(conn);
            } else {
                throw new HttpException("HTTP error code " + String.valueOf(code), new Exception());
            }
        } finally {
            conn.disconnect();
        }
    }

    private static URL make_url(String endpoint) throws HttpException {
        try {
            return new URL("http://192.168.1.77:8082/" + endpoint);
        } catch (MalformedURLException e) {
            throw new HttpException("Malformed URL", e);
        }
    }

    private static HttpURLConnection open_connection(URL url) throws HttpException {
        try {
            return (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new HttpException("Cannot open HTTP connection", e);
        }
    }

    private static void begin_request(HttpURLConnection conn, String data) {
        conn.setDoOutput(true);
        conn.setFixedLengthStreamingMode(data.length());
    }
    private static void begin_request(HttpURLConnection conn, InputStream data) {
        conn.setDoOutput(true);
        conn.setChunkedStreamingMode(1024);
    }

    private static void write_request(HttpURLConnection conn, String data) throws HttpException {
        try {
            conn.getOutputStream().write(data.getBytes());
        } catch (IOException e) {
            throw new HttpException("Cannot write HTTP POST request", e);
        }
    }
    private static void write_request(HttpURLConnection conn, InputStream data) throws HttpException {
        try {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                conn.getOutputStream().write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new HttpException("Cannot write HTTP POST request", e);
        }
    }

    private static void end_request(HttpURLConnection conn) throws HttpException {
        try {
            conn.getOutputStream().close();
        } catch (IOException e) {
            throw new HttpException("Cannot close HTTP POST request", e);
        }
    }

    private static InputStream get_input_stream(HttpURLConnection conn) throws HttpException {
        try {
            return conn.getInputStream();
        } catch (IOException e) {
            throw new HttpException("Cannot open response input stream", e);
        }
    }

    private static int read_response_code(HttpURLConnection conn) throws HttpException {
        try {
            return conn.getResponseCode();
        } catch (IOException e) {
            throw new HttpException("Cannot read response code", e);
        }
    }
}
