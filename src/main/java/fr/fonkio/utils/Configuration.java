package fr.fonkio.utils;

import fr.fonkio.json.JSONReader;
import fr.fonkio.json.JSONWriter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Configuration {
    private final JSONObject object;
    private final File file;

    public Configuration(String path) throws IOException {
        this.file = new File(path);
        if (this.file.exists()) {
            this.object = new JSONReader(this.file).toJSONObject();
        } else {
            object = new JSONObject();
        }
    }

   public JSONObject newServer() {
        JSONObject jo = new JSONObject();
        jo.put("blacklist", new JSONArray());
        jo.put("prefix", "!");
        jo.put("welcome", "");
        jo.put("quit", "");
        return jo;
   }

    public JSONObject getServerConfig(String key) {
        if(!object.has(key)) {
            object.put(key, newServer());
            save();
        }
        return object.getJSONObject(key);
    }

    public JSONArray getBlackList(String key) {
        return getServerConfig(key).getJSONArray("blacklist");
    }

    public void addBlackList(String key, String idTextChannel) {
        JSONArray ja = getBlackList(key);
        ja.put(ja.length(), idTextChannel);
        getServerConfig(key).put("blacklist", ja);
        save();
    }
    public boolean delBlacklist(String key, String idTextChannel) {
        JSONArray ja = getBlackList(key);
        boolean trouve = false;
        for(int i = 0; i < ja.length(); i++) {
            if (ja.getString(i).equals(idTextChannel)) {
                ja.remove(i);
                trouve = true;
            }
        }
        if (trouve) {
            getServerConfig(key).put("blacklist", ja);
            save();
        }
        return trouve;
    }

    public void setQuit(String key, String idTextChannel) {
        getServerConfig(key).put("quit", idTextChannel);
        save();
    }

    public void setWelcome(String key, String idTextChannel) {
        getServerConfig(key).put("welcome", idTextChannel);
        save();
    }
    public String getQuit(String key) {
        return getServerConfig(key).getString("quit");
    }

    public String getWelcome(String key) {
        return getServerConfig(key).getString("welcome");
    }

    public String getPrefix(String key) {
        return getServerConfig(key).getString("prefix");
    }

    public void setPrefix(String key, String prefix) {
        getServerConfig(key).put("prefix", prefix);
        save();
    }

    public void save() {
        try (JSONWriter writer = new JSONWriter(file)) {
            writer.write(this.object);
            writer.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getToken() {
        if(!object.has("token")) {
            object.put("token", "Mettre token ici");
            save();
        }
        return object.getString("token");
    }
    public String getYtapi() { 
        if(!object.has("ytapi")) { 
            object.put("ytapi", "Mettre token ici"); 
            save(); 
        } 
        return object.getString("ytapi"); 
    }

    public String getDCsong() {
        if(!object.has("dcsong")) {
            object.put("dcsong", "Mettre son deco ici");
            save();
        }
        return object.getString("dcsong");
    }
}
