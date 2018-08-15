package ca.aequilibrium.weather.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ca.aequilibrium.weather.models.Location;

@Dao
public interface LocationDao {
    @Query("SELECT * FROM location ORDER BY uid DESC")
    List<Location> getAll();

    @Query("SELECT * FROM location WHERE uid IN (:locationIds)")
    List<Location> loadAllByIds(int[] locationIds);

    @Query("SELECT * FROM location WHERE name LIKE :name LIMIT 1")
    Location findByName(String name);

    @Insert
    void insertAll(Location... locations);

    @Delete
    void delete(Location location);

    @Query("DELETE FROM location")
    void deleteAll();
}
