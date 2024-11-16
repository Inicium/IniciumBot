package fr.fonkio.utils;

import fr.fonkio.enums.ConfigurationBotEnum;
import fr.fonkio.enums.ConfigurationGuildEnum;
import fr.fonkio.json.JSONReader;
import fr.fonkio.json.JSONWriter;
import fr.fonkio.message.StringsConst;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Configuration {

    private final JSONObject jsonObject;
    private final File file;
    private final Logger logger = LoggerFactory.getLogger(Configuration.class);

    public Configuration(String path) throws IOException {
        this.file = new File(path);
        if (this.file.exists()) {
            this.jsonObject = new JSONReader(this.file).toJSONObject();
        } else {
            jsonObject = new JSONObject();
        }
    }

    /**
     * Retourne un string de config globale à tout les serveurs
     * @see ConfigurationBotEnum
     * @param config Configuration à retourner
     * @return String La valeur de la configuration
     */
    public String getGlobalParam(ConfigurationBotEnum config) {
        if(!jsonObject.has(config.getKey())) {
            jsonObject.put(config.getKey(), "to complete");
            save();
        }
        return jsonObject.getString(config.getKey());
    }

    /** Retourne à partir d'un ID de serveur la liste correspondant à sa blacklist
     * Si elle n'existe pas, elle est créée
     *
     * @param guidId Id du serveur
     * @return JSONArray blacklist du serveur
     */
    public Set<String> getBlackList(String guidId) {
        Set<String> blacklist = new HashSet<>();
        JSONArray jsonArray = getJSONArray(guidId, ConfigurationGuildEnum.BLACK_LIST);
        jsonArray.forEach(o -> blacklist.add((String) o));
        return blacklist;
    }

    /** Retourne à partir d'un ID de serveur la liste des musiqued de sa playlist
     * Si elle n'existe pas, elle est créée
     *
     * @param guidId Id du serveur
     * @return List<PlaylistItem> la playlist du serveur guidId
     */
    public List<PlaylistItem> getPlaylist(String guidId) {
        List<PlaylistItem> playList = new ArrayList<>();
        JSONArray jsonArray = getJSONArray(guidId, ConfigurationGuildEnum.PLAYLIST);
        jsonArray.forEach(o -> {
                    JSONObject jsonObject = (JSONObject) o;
                    playList.add(new PlaylistItem(jsonObject.getString("url"), jsonObject.getString("label")));
                });

        return playList;
    }

    /**
     * Supprime toutes les musique de la playlist du serveur
     * @param guildId Serveur discord
     * @param urlsToDelete Liste des URLs des musiques à supprimer
     */
    public void removeFromPlaylist(String guildId, List<String> urlsToDelete) {
        JSONArray jsonArray = getJSONArray(guildId, ConfigurationGuildEnum.PLAYLIST);

        for(Iterator<Object> iterator = jsonArray.iterator(); iterator.hasNext(); ) {
            JSONObject jsonObject = (JSONObject) iterator.next();
            if (urlsToDelete.contains(jsonObject.getString("url"))) {
                iterator.remove();
            }
        }
        getServerConfig(guildId).put(ConfigurationGuildEnum.PLAYLIST.getKey(), jsonArray);
        save();
    }

    /**
     * Ajoute une musique à la playlist du serveur
     * @param guildId Serveur discord
     * @param url URL de la musique à ajouter
     * @param label Nom de la musique dans la playlist
     * @throws IllegalStateException Si on dépasse 25 titres dans la playlist ou si l'URL existe déjà
     */
    public void addToPlaylist(String guildId, String url, String label) throws IllegalStateException {
        JSONArray jsonArray = getJSONArray(guildId, ConfigurationGuildEnum.PLAYLIST);
        if (jsonArray.length() >= 25) {
            throw new IllegalStateException(StringsConst.COMMAND_PLAYLIST_ADD_FULL);
        }
        List<PlaylistItem> playList = getPlaylist(guildId);
        if (playList.stream().anyMatch(pli -> pli.getUrl().equals(url))) {
            throw new IllegalStateException(StringsConst.COMMAND_PLAYLIST_ADD_ALREADY_EXIST);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", url);
        jsonObject.put("label", label);
        jsonArray.put(jsonObject);
        getServerConfig(guildId).put(ConfigurationGuildEnum.PLAYLIST.getKey(), jsonArray);
        save();
    }

    public boolean blackListContains(String guidId, String idTextChannel) {
        return getBlackList(guidId).contains(idTextChannel);
    }

    public void addBlackList(String guildId, List<String> idTextChannelList) {
        JSONArray ja = getJSONArray(guildId, ConfigurationGuildEnum.BLACK_LIST);
        for (String id : idTextChannelList) {
            ja.put(id);
        }
        getServerConfig(guildId).put(ConfigurationGuildEnum.BLACK_LIST.getKey(), ja);
        save();
    }
    public void delBlacklist(String guildId) {
        JSONArray ja = new JSONArray();
        getServerConfig(guildId).put(ConfigurationGuildEnum.BLACK_LIST.getKey(), ja);
        save();
    }

    public String getGuildConfig(String guildId, ConfigurationGuildEnum key) {
        String confString;
        try {
            confString = getServerConfig(guildId).getString(key.getKey());
        } catch (JSONException e) {
            confString = key.getDefaultValue();
            setGuildConfig(guildId, key, confString);
        }
        return confString;
    }

    public void setGuildConfig(String guildId, ConfigurationGuildEnum key, String value) {
        getServerConfig(guildId).put(key.getKey(), value);
        save();
    }

    public void save() {
        try (JSONWriter writer = new JSONWriter(file)) {
            writer.write(this.jsonObject);
            writer.flush();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private JSONObject getServerConfig(String guildId) {
        if(!jsonObject.has(guildId)) {
            jsonObject.put(guildId, new JSONObject());
            save();
        }
        return jsonObject.getJSONObject(guildId);
    }

    private JSONArray getJSONArray(String guidId, ConfigurationGuildEnum configurationGuildEnum) {
        JSONObject serverConfig = getServerConfig(guidId);
        if (!serverConfig.has(configurationGuildEnum.getKey())) {
            serverConfig.put(configurationGuildEnum.getKey(), new JSONArray());
            save();
        }
        return serverConfig.getJSONArray(configurationGuildEnum.getKey());
    }


}
