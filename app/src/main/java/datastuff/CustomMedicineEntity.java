package datastuff;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CustomMedicineEntity {

    @PrimaryKey(autoGenerate = true)
    public int Id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "quantity")
    public int quantity;

    @ColumnInfo(name = "dose")
    public int dose;

    @ColumnInfo(name = "use")
    public String use;

    @ColumnInfo(name = "reason")
    public String reason;

    @ColumnInfo(name = "barcodes")
    public String barcodes;

    @ColumnInfo(name = "createdTimeDate")
    public long createdTimeDate;

    @ColumnInfo(name = "medicineLeft")
    public int medicineLeft;

}
