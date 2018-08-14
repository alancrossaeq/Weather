package ca.aequilibrium.weather;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import ca.aequilibrium.weather.DAOs.LocationDao;
import ca.aequilibrium.weather.DAOs.UserDao;
import ca.aequilibrium.weather.models.Location;
import ca.aequilibrium.weather.models.User;

@Database(entities = {User.class, Location.class}, version = 1, exportSchema = false)
@TypeConverters({DataTypeConverters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract LocationDao locationDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
