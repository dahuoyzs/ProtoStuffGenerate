package cn.bigfire.stuff.gentlp;


import cn.bigfire.stuff.bo.FieldInfo;
import cn.bigfire.stuff.bo.ProtoInfo;
import cn.bigfire.stuff.util.Utils;

public class GenJavaBean {


    //enum模板
    public static String enumStartTlp = "public enum %s  {\n";
    public static String enumFieldTlp = "    %s(%s),\n";
    public static String enumMethodTlp = "    ;\n" +
            "    private int value;\n" +
            "\n" +
            "    private %s(int value) {\n" +
            "      this.value = value;\n" +
            "    }\n" +
            "\n" +
            "    public int getValue() {\n" +
            "      return value;\n" +
            "    }\n";
    //class模板
    public static String beanStartTlp = "public class %s  {\n";
    public static String beanFieldTlp = "    %s %s;\n";
    public static String beanGetMethodTlp = "    public %s get%s() {\n" +
            "        return %s;\n" +
            "    }\n";
    public static String beanSetMethodTlp = "    public void set%s(%s %s) {\n" +
            "        this.%s = %s;\n" +
            "    }\n";

    //end
    public static String END = "}\n";

    //生成代码
    public static void gen(ProtoInfo protoInfo) {
        if (protoInfo.getJava_multiple_files()) {//多文件
            genMultiple(protoInfo);
        } else {//单文件
            genSingle(protoInfo);
        }
    }

    //生成多文件代码
    public static void genMultiple(ProtoInfo protoInfo){
        String packStr = Utils.getPackageStr();
        String importStr = "import java.util.*;\n" +
                "import java.lang.*;\n" +
                "import io.protostuff.Tag;\n";
        //多文件
        protoInfo.getEnumMap().forEach((enumName,fieldInfos)->{
            //构建字符串
            String enumStart = String.format(enumStartTlp, enumName);
            StringBuilder fieldSb = new StringBuilder();
            for (FieldInfo fieldInfo : fieldInfos) {
                String pbName = fieldInfo.getPbName();
                String pbNumber = fieldInfo.getPbNumber();
                fieldSb.append(String.format(enumFieldTlp, pbName, pbNumber));
            }
            String enumMethod = String.format(enumMethodTlp, enumName);
            String enumCode = enumStart + fieldSb + enumMethod + END;
            //代码生成
            String code = packStr + enumCode;
            String targetFileName = Utils.genDir + Utils.upperFirst(enumName) + ".java";
//            System.out.println("code:" + code);
//            System.out.println("targetFileName:" + targetFileName);
            Utils.write(targetFileName, code);
        });

        protoInfo.getObjMap().forEach((objName,fieldInfos)->{
            //构建字符串
            String beanStart = String.format(beanStartTlp, objName);

            StringBuilder fieldSb = new StringBuilder();
            for (FieldInfo fieldInfo : fieldInfos) {
                String fieldName = fieldInfo.getBeanName();
                if (fieldInfo.getBeanType().contains("java.util.List") || fieldInfo.getBeanType().startsWith("List")) {
                    fieldName += " = new java.util.ArrayList<>()";
                } else if (fieldInfo.getBeanType().contains("java.util.Map") || fieldInfo.getBeanType().startsWith("Map")) {
                    fieldName += " = new java.util.HashMap<>()";
                }
                fieldSb.append(String.format(beanFieldTlp, fieldInfo.getBeanType(), fieldName));
            }

            StringBuilder methodSb = new StringBuilder();
            for (FieldInfo fieldInfo : fieldInfos) {
                String type = fieldInfo.getBeanType();
                String fieldName = fieldInfo.getBeanName();
                String upFieldName = Utils.upperFirst(fieldInfo.getBeanName());
                methodSb.append(String.format(beanGetMethodTlp, type, upFieldName, fieldName));
                methodSb.append(String.format(beanSetMethodTlp, upFieldName, type, fieldName, fieldName, fieldName));
            }
            String beanCode = beanStart + fieldSb + methodSb + END;

            //代码生成
            String code = packStr + importStr + beanCode;
            String targetFileName = Utils.genDir +  Utils.upperFirst(objName) + ".java";
//            System.out.println("code:" + code);
//            System.out.println("targetFileName:" + targetFileName);
            Utils.write(targetFileName, code);
        });
    }

    //生成单文件代码
    public static void genSingle(ProtoInfo protoInfo){
        String targetFileName = Utils.genDir +  Utils.upperFirst(Utils.beanName) + ".java";
        String packStr = Utils.getPackageStr();
        String packAndImportStr = packStr +
                "import java.util.*;\n" +
                "import java.lang.*;\n";
        StringBuilder singleSb = new StringBuilder(packAndImportStr);
        singleSb.append("public class ").append(Utils.beanName).append(" {\n\n");

        protoInfo.getEnumMap().forEach((enumName,fieldInfos)->{
            //构建字符串
            String enumStart = String.format(enumStartTlp, enumName);
            StringBuilder fieldSb = new StringBuilder();
            for (FieldInfo fieldInfo : fieldInfos) {
                String pbName = fieldInfo.getPbName();
                String pbNumber = fieldInfo.getPbNumber();
                fieldSb.append(String.format(enumFieldTlp, pbName, pbNumber));
            }
            String enumMethod = String.format(enumMethodTlp, enumName);
            String enumCode = enumStart + fieldSb + enumMethod + END;

            singleSb.append(enumCode).append("\n");
        });

        protoInfo.getObjMap().forEach((objName,fieldInfos)->{
            //构建字符串
            String beanStart ="public static class "+objName+" {\n";
            StringBuilder fieldSb = new StringBuilder();
            for (FieldInfo fieldInfo : fieldInfos) {
                String fieldName = fieldInfo.getBeanName();
                if (fieldInfo.getBeanType().contains("java.util.List") || fieldInfo.getBeanType().startsWith("List")) {
                    fieldName += " = new java.util.ArrayList<>()";
                } else if (fieldInfo.getBeanType().contains("java.util.Map") || fieldInfo.getBeanType().startsWith("Map")) {
                    fieldName += " = new java.util.HashMap<>()";
                }
                fieldSb.append(String.format(beanFieldTlp, fieldInfo.getBeanType(), fieldName));
            }

            StringBuilder methodSb = new StringBuilder();
            for (FieldInfo fieldInfo : fieldInfos) {
                String type = fieldInfo.getBeanType();
                String fieldName = fieldInfo.getBeanName();
                String upFieldName = Utils.upperFirst(fieldInfo.getBeanName());
                methodSb.append(String.format(beanGetMethodTlp, type, upFieldName, fieldName));
                methodSb.append(String.format(beanSetMethodTlp, upFieldName, type, fieldName, fieldName, fieldName));
            }
            String beanCode = beanStart + fieldSb + methodSb + END;
            singleSb.append(beanCode).append("\n");
        });
        String code = singleSb + END;
//        System.out.println("code:" + code);
//        String formatCode = Utils.formatJavaCode(code);
//        System.out.println("formatCode:" + formatCode);
//        System.out.println("targetFileName:" + targetFileName);
        Utils.write(targetFileName, code);
    }


}
