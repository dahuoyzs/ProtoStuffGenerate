package cn.bigfire.stuff.bo;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProtoInfo {

    String pbName = "xxx";
    //syntax
    String syntax = "proto3";//默认proto3
    //package
    String packageName = "";//pb缺省时 默认空包名

    //option
    String optimize_for = "SPEED";//pb缺省时 默认速度模式
    String java_outer_classname = "";//pb缺省时 默认文件名首字母大写
    Boolean java_multiple_files = false;//pb缺省时 默认单文件

    Map<String, List<FieldInfo>> objMap = new HashMap<>();
    Map<String, List<FieldInfo>> enumMap = new HashMap<>();

    public String getPbName() {
        return pbName;
    }

    public void setPbName(String pbName) {
        this.pbName = pbName;
    }

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getOptimize_for() {
        return optimize_for;
    }

    public void setOptimize_for(String optimize_for) {
        this.optimize_for = optimize_for;
    }

    public String getJava_outer_classname() {
        return java_outer_classname;
    }

    public void setJava_outer_classname(String java_outer_classname) {
        this.java_outer_classname = java_outer_classname;
    }

    public Boolean getJava_multiple_files() {
        return java_multiple_files;
    }

    public void setJava_multiple_files(Boolean java_multiple_files) {
        this.java_multiple_files = java_multiple_files;
    }

    public Map<String, List<FieldInfo>> getObjMap() {
        return objMap;
    }

    public void setObjMap(Map<String, List<FieldInfo>> objMap) {
        this.objMap = objMap;
    }

    public Map<String, List<FieldInfo>> getEnumMap() {
        return enumMap;
    }

    public void setEnumMap(Map<String, List<FieldInfo>> enumMap) {
        this.enumMap = enumMap;
    }
}
