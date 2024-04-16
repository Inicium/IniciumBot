package fr.fonkio.utils;

import fr.fonkio.json.JSONReader;
import fr.fonkio.json.JSONWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

    /** Récupère la config d'une guilde, si elle n'existe pas il l'a créé
     *
     * @param guildId Id de la Guild à récupérer
     * @return Le JSONObject du la Guild
     */
    public JSONObject getServerConfig(String guildId) {
        if(!jsonObject.has(guildId)) { // Nouveau serveur dans la config
            jsonObject.put(guildId, new JSONObject());
            save();
        }
        return jsonObject.getJSONObject(guildId);
    }

    /** Retourne à partir d'un ID de serveur son JSONArray dans la config
     * correspondant à sa blacklist
     * Si elle n'existe pas, elle est créée
     *
     * @param guidId Id du serveur
     * @return JSONArray blacklist du serveur
     */
    public JSONArray getBlackList(String guidId) {
        JSONObject serverConfig = getServerConfig(guidId);
        if (!serverConfig.has(ConfigurationEnum.BLACK_LIST.getKey())) {
            serverConfig.put(ConfigurationEnum.BLACK_LIST.getKey(), new JSONArray());
            save();
        }
        return serverConfig.getJSONArray(ConfigurationEnum.BLACK_LIST.getKey());
    }

    public boolean blackListContains(String guidId, String idTextChannel) {
        JSONArray jsonArray = getBlackList(guidId);
        for (int i = 0; i < jsonArray.length(); i++) {
            if (idTextChannel.equals(jsonArray.getString(i))) {
                return true;
            }
        }
        return false;
    }

    public void addBlackList(String guildId, List<String> idTextChannelList) {
        JSONArray ja = getBlackList(guildId);
        for (String id : idTextChannelList) {
            ja.put(id);
        }
        getServerConfig(guildId).put(ConfigurationEnum.BLACK_LIST.getKey(), ja);
        save();
    }
    public void delBlacklist(String guildId) {
        JSONArray ja = new JSONArray();
        getServerConfig(guildId).put(ConfigurationEnum.BLACK_LIST.getKey(), ja);
        save();
    }

    public String getGuildConfig(String guildId, ConfigurationEnum key) {
        String confString;
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
