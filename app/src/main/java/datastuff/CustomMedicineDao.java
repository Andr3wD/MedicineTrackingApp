package datastuff;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CustomMedicineDao {

    @Query("SELECT * FROM custommedicineentity ORDER BY Id DESC")
    List<CustomMedicineEntity> getAllSortedId();

    @Query("SELECT name FROM custommedicineentity ORDER BY Id DESC")
    List<String> getAllNamesSortedId();

    @Query("SELECT * FROM custommedicineentity WHERE Id LIKE :id LIMIT 1")
    CustomMedicineEntity findById(int id);

    @Query("SELECT * FROM custommedicineentity ORDER BY Id DESC LIMIT 1 OFFSET :index")
    CustomMedicineEntity getIdSortedEntry(int index);

    @Insert
    void insertAll(CustomMedicineEntity ... customMedicineEntities);

    @Delete
    void delete(CustomMedicineEntity user);

    @Query("SELECT Count(*) FROM custommedicineentity")
    int size();

    @Query("SELECT * FROM custommedicineentity ORDER BY createdTimeDate DESC LIMIT 1 OFFSET :index")
    CustomMedicineEntity getTimeSortedEntry(int index);

    @Query("Update custommedicineentity SET name = :name, quantity = :quantity, dose = :dose, use = :use, reason = :reason, barcodes = :barcodes, medicineLeft = :medicineLeft WHERE Id = :Id")
    void update(String name, int quantity, String reason, int dose, long Id, String use, String barcodes, int medicineLeft);

}
