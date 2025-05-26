package com.creatrix.salessolution.Model;

public class Transport {
    public  int TransportId;
    public  String  TransportName;


    public int getTransportId() {
        return TransportId;
    }

    public void setTransportId(int transportId) {
        TransportId = transportId;
    }

    public String getTransportName() {
        return TransportName;
    }

    public void setTransportName(String transportName) {
        TransportName = transportName;
    }


    @Override
    public String toString() {
        return TransportName;
    }
}
