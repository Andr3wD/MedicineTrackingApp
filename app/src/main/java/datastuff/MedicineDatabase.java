package datastuff;

import androidx.room.Database;
import androidx.room.RoomDatabase;

//Followed https://developer.android.com/training/data-storage/room/index.html#java
@Database(entities = {IndividualMedicineEntity.class, CustomMedicineEntity.class}, version = 1)
public abstract class MedicineDatabase extends RoomDatabase {
    public abstract IndividualMedicineDao individualMedicineDao();
    public abstract CustomMedicineDao customMedicineDao();
}
