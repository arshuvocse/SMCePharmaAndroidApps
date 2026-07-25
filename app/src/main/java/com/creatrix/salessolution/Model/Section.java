package com.creatrix.salessolution.Model;

import java.util.List;

public class Section {
    public final String typeName;              // e.g., "DCP", "CCP", "XYZ"
    public final List<DcpCcpData> items;

    public Section(String typeName, List<DcpCcpData> items) {
        this.typeName = typeName;
        this.items = items;
    }
}
