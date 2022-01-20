package me.lucanius.infinity.managers;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.lucanius.infinity.Infinity;
import me.lucanius.infinity.utils.ConfigFile;
import me.lucanius.infinity.utils.ManagerUtil;
import org.bson.Document;

import java.util.Collections;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 19:28
 */
@Getter
public class MongoManager extends ManagerUtil {

    private final ConfigFile config = this.plugin.getSettingsConfig();

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private final String host = this.config.getUncoloredString("MONGO.HOST");
    private final int port = this.config.getInt("MONGO.PORT");
    private final String database = this.config.getUncoloredString("MONGO.DATABASE");
    private final boolean auth = this.config.getBoolean("MONGO.AUTH.ENABLED");
    private final String user = this.config.getUncoloredString("MONGO.AUTH.USER");
    private final String password = this.config.getUncoloredString("MONGO.AUTH.PASS");
    private final String authDb = this.config.getUncoloredString("MONGO.AUTH.AUTH-DB");

    private MongoCollection<Document> players;

    public MongoManager(Infinity plugin) {
        super(plugin);
        this.connect();
    }

    private void connect() {
        try {
            if (this.auth) {
                final MongoCredential credential = MongoCredential.createCredential(this.user, this.authDb, this.password.toCharArray());
                this.mongoClient = new MongoClient(new ServerAddress(this.host, this.port), Collections.singletonList(credential));
            } else {
                this.mongoClient = new MongoClient(this.host, this.port);
            }
            this.mongoDatabase = this.mongoClient.getDatabase(this.database);
            this.players = this.mongoDatabase.getCollection("players");
        } catch (Exception exception) {
            exception.printStackTrace();
            this.plugin.getPluginLoader().disablePlugin(this.plugin);
        }
    }

    public void disconnect() {
        if (this.mongoClient != null) {
            this.mongoClient.close();
        }
    }
}
