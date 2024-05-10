package cn.bigfire.stuff.bo;


public class FieldInfo {

    String pbLabel;
    String pbType;
    String pbName;
    String pbNumber;

    String pbMapKeyType;
    String pbMapValType;

    String beanType;
    String beanName;
    String defaultVal;

    public String getPbLabel() {
        return pbLabel;
    }

    public void setPbLabel(String pbLabel) {
        this.pbLabel = pbLabel;
    }

    public String getPbType() {
        return pbType;
    }

    public void setPbType(String pbType) {
        this.pbType = pbType;
    }

    public String getPbName() {
        return pbName;
    }

    public void setPbName(String pbName) {
        this.pbName = pbName;
    }

    public String getPbNumber() {
        return pbNumber;
    }

    public void setPbNumber(String pbNumber) {
        this.pbNumber = pbNumber;
    }

    public String getPbMapKeyType() {
        return pbMapKeyType;
    }

    public void setPbMapKeyType(String pbMapKeyType) {
        this.pbMapKeyType = pbMapKeyType;
    }

    public String getPbMapValType() {
        return pbMapValType;
    }

    public void setPbMapValType(String pbMapValType) {
        this.pbMapValType = pbMapValType;
    }

    public String getBeanType() {
        return beanType;
    }

    public void setBeanType(String beanType) {
        this.beanType = beanType;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }
}
