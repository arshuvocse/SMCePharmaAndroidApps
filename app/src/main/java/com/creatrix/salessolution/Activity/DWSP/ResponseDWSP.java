package com.creatrix.salessolution.Activity.DWSP;

import com.creatrix.salessolution.Activity.DWSP.Model.ModelDWSPTargetArea;

import java.util.List;

public class ResponseDWSP {
    String target;
    List<ModelDWSPTargetArea> dwspData;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<ModelDWSPTargetArea> getDwspData() {
        return dwspData;
    }

    public void setDwspData(List<ModelDWSPTargetArea> dwspData) {
        this.dwspData = dwspData;
    }
}
