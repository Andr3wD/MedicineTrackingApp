package datastuff;

import androidx.room.Database;
import androidx.room.RoomDatabase;

//Followed https://developer.android.com/training/data-storage/room/index.html#java
@Database(entities = {IndividualMedicineEntity.class, CustomMedicineEntity.class}, version = 8)
public abstract class IndividualMedicineDatabase extends RoomDatabase {
    public abstract IndividualMedicineDao individualMedicineDao();
    public abstract CustomMedicineDao customMedicineDao();
}
