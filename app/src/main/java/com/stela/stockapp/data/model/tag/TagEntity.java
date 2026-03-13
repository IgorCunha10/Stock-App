package com.stela.stockapp.data.model.tag;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tags")
public class TagEntity {
    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "tag_validity")
    public long validityStamp;

    @ColumnInfo(name = "tag_fabrication")
    public long fabricationStamp;

    public TagEntity() {
    }

    public TagEntity(@NonNull String id, long validityStamp, long fabricationStamp) {
        this.id = id;
        this.validityStamp = validityStamp;
        this.fabricationStamp = fabricationStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;

    }

    public long getValidityStamp() {
        return validityStamp;
    }

    public void setValidityStamp(long validity) {
        this.validityStamp = validity;
    }

    public long getFabricationStamp() {
        return fabricationStamp;
    }

    public void setFabricationStamp(long fabricationStamp) {
        this.fabricationStamp = fabricationStamp;
    }
}
