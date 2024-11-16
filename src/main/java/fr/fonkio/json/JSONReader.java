package fr.fonkio.json;

import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;

public final class JSONReader {

    private final String json;

    public JSONReader(File file) throws IOException {
        this(new InputStreamReader(Files.newInputStream(file.toPath())));
    }

    public JSONReader(Reader reader) throws IOException {
        this(new BufferedReader(reader));
    }

    public JSONReader(BufferedReader reader) throws IOException {
        json = load(reader);
    }

    private String load(BufferedReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        while(reader.ready()){
            builder.append(reader.readLine());
        }
        reader.close();
        return builder.length() == 0 ? "[]" : builder.toString();
    }

    public JSONObject toJSONObject() {
        return new JSONObject(json);
    }
}
