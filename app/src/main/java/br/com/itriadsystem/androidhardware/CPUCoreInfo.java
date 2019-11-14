package br.com.itriadsystem.androidhardware;


public class CPUCoreInfo {

    private String coreName;
    private String coreValue;

    public CPUCoreInfo(String coreName, String coreValue) {
        this.coreName = coreName;
        this.coreValue = coreValue;
    }

    public String getCoreName() {
        return coreName;
    }

    public String getCoreValue() {
        return coreValue;
    }
}