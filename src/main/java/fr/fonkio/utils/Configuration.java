package fr.fonkio.utils;

import fr.fonkio.json.JSONReader;
import fr.fonkio.json.JSONWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Configuration {

    private final JSONObject jsonObject;
    private final File file;

    public Configuration(String path) throws IOException {
        this.file = new File(path);
        if (this.file.exists()) {
            this.jsonObject = new JSONReader(this.file).toJSONObject();
        } else {
            jsonObject = new JSONObject();
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

    public String getToken() {
        if(!jsonObject.has("token")) {
            jsonObject.put("token", "Mettre token ici");
            save();
        }
        return jsonObject.getString("token");
    }
    public String getYtapi() {
        if(!jsonObject.has("ytapi")) {
            jsonObject.put("ytapi", "Mettre token ici");
            save();
        }
        return jsonObject.getString("ytapi");
    }

    public String getDCsong() {
        if(!jsonObject.has("dcsong")) {
            jsonObject.put("dcsong", "Mettre son deco ici");
            save();
        }
        return jsonObject.getString("dcsong");
    }

    /**
     *
     * @param guildId Id de la Guild à récupérer
     * @return Le JSONObject du la Guild, il en créé un si il ne trouve pas le serbeur dans la config
     */
    public JSONObject getServerConfig(String guildId) {
        if(!jsonObject.has(guildId)) {
            jsonObject.put(guildId, newServer());
            save();
        }
        return jsonObject.getJSONObject(guildId);
    }

    public JSONArray getBlackList(String guidId) {
        return getServerConfig(guidId).getJSONArray("blacklist");
    }

    public void addBlackList(String guildId, String idTextChannel) {
        JSONArray ja = getBlackList(guildId);
        ja.put(ja.length(), idTextChannel);
        getServerConfig(guildId).put("blacklist", ja);
        save();
    }
    public boolean delBlacklist(String guildId, String idTextChannel) {
        JSONArray ja = getBlackList(guildId);
        boolean trouve = false;
        for(int i = 0; i < ja.length(); i++) {
            if (ja.getString(i).equals(idTextChannel)) {
                ja.remove(i);
                trouve = true;
            }
        }
        if (trouve) {
            getServerConfig(guildId).put("blacklist", ja);
            save();
        }
        return trouve;
    }

    public String getGuildConfig(String guildId, ConfigurationEnum key) {
        String confString = "";
        try {
            confString = getServerConfig(guildId).getString(key.getKey());
        } catch (JSONException e) {
            confString = key.getDefaultValue();
            setGuildConfig(guildId, key, confString);
        }
        return confString;
    }

    public void setGuildConfig(String guildId, ConfigurationEnum key, String value) {
        getServerConfig(guildId).put(key.getKey(), value);
        save();
    }

    public void save() {
        try (JSONWriter writer = new JSONWriter(file)) {
            writer.write(this.jsonObject);
            writer.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }



}
