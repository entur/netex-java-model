package org.rutebanken.netex;

import org.apache.commons.lang3.builder.ToStringStyle;

public class OmitNullsToStringStyle extends org.apache.commons.lang3.builder.ToStringStyle {

    public static ToStringStyle INSTANCE = new OmitNullsToStringStyle();

    public OmitNullsToStringStyle() {
        this.setUseClassName(false);
        this.setUseIdentityHashCode(false);
        this.setUseFieldNames(true);
        this.setContentStart("[");
        this.setContentEnd("]");
    }

    @Override
    public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
        if (value != null) {
            super.append(buffer, fieldName, value, fullDetail);
        }
    }

}
