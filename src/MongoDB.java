import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
public class MongoDB {
    private static final String URI = "mongodb://localhost:27017"; // Update with your MongoDB URI if different
    private static final String DATABASE_NAME = "Airline";        // Use your database name
    private static MongoDatabase database;

    public static MongoDatabase connect() {
        if (database == null) {
            MongoClient mongoClient = MongoClients.create(URI);   // Create a client connection
            database = mongoClient.getDatabase(DATABASE_NAME);    // Connect to the 'Airline' database
        }
        return database;
    }
}
