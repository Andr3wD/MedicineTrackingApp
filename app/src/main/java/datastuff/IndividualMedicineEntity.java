package datastuff;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class IndividualMedicineEntity {

    @PrimaryKey
    public int position;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "takenDateTime")
    public long takenDateTime;

    @ColumnInfo(name = "inputTimeDate")
    public long inputTimeDate;

    @ColumnInfo(name = "quantity")
    public int quantity;

    @ColumnInfo(name = "reason")
    public String reason;

    @ColumnInfo(name = "dose")
    public int dose;

    @ColumnInfo(name = "baseCustomMedicineID")
    public int baseCustomMedicineID;


}
