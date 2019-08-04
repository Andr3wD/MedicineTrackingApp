package datastuff;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IndividualMedicineDao {
    @Query("SELECT * FROM individualmedicineentity")
    List<IndividualMedicineEntity> getAll();

    @Query("SELECT * FROM individualmedicineentity WHERE position IN (:positions)")
    List<IndividualMedicineEntity> loadAllByPositions(int[] positions);

    @Query("SELECT * FROM individualmedicineentity WHERE position LIKE :position LIMIT 1")
    IndividualMedicineEntity findByPosition(int position);

    @Insert
    void insertAll(IndividualMedicineEntity... individualMedicineEntities);

    @Delete
    void delete(IndividualMedicineEntity user);

    @Query("SELECT Count(*) FROM individualmedicineentity")
    int size();

    @Query("SELECT * FROM individualmedicineentity ORDER BY takenDateTime DESC LIMIT 1 OFFSET :index")
    IndividualMedicineEntity getTimeSortedEntry(int index);

    @Query("Update individualmedicineentity SET name = :name, takenDateTime = :takenDateTime, " +
            "quantity = :quantity, reason = :reason, dose = :dose, baseCustomMedicineID = :baseCustomMedicineID WHERE position = :position")
    void update(int position, String name, long takenDateTime, int quantity, String reason, int dose, long baseCustomMedicineID);

}
