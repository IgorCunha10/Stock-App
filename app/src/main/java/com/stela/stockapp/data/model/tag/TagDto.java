package com.stela.stockapp.data.model.tag;

public class TagDto {

    private String id;
    private long validityStamp;
    private long fabricationStamp;

    public TagDto(String id, long validityStamp, long fabricationStamp) {
        this.id = id;
        this.validityStamp = validityStamp;
        this.fabricationStamp = fabricationStamp;
    }

    public String getId() {
        return id;
    }

    public long getValidityStamp() {
        return validityStamp;
    }

    public long getFabricationStamp() {
        return fabricationStamp;
    }

}
