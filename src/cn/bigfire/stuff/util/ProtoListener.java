package cn.bigfire.stuff.util;


import cn.bigfire.stuff.antlr.gen.Protobuf3BaseListener;
import cn.bigfire.stuff.antlr.gen.Protobuf3Parser;
import cn.bigfire.stuff.bo.FieldInfo;
import cn.bigfire.stuff.bo.ProtoInfo;
import cn.bigfire.stuff.config.MyPluginSettings;

import java.util.ArrayList;
import java.util.List;

public class ProtoListener extends Protobuf3BaseListener {


    public static List<FieldInfo> currFields = new ArrayList<>();

    @Override
    public void enterProto(Protobuf3Parser.ProtoContext ctx) {
        Utils.protoInfo = new ProtoInfo();//每次进入保证是新的对象，防止老数据影响新数据
    }


    @Override
    public void enterPackageStatement(Protobuf3Parser.PackageStatementContext ctx) {
//        System.out.println(ctx.fullIdent().getText());
        String pbPackageName = ctx.fullIdent().getText();
        Utils.protoInfo.setPackageName(pbPackageName);
    }

    @Override
    public void enterOptionStatement(Protobuf3Parser.OptionStatementContext ctx) {
//        System.out.println(ctx.OPTION().getText() + " " + ctx.optionName().getText() + " = " + ctx.constant().getText());
        if ("java_multiple_files".equals(ctx.optionName().getText())) { //多文件处理
//            System.out.println(ctx.OPTION().getText() + " " + ctx.optionName().getText() + " = " + ctx.constant().getText());
            if ("true".equals(ctx.constant().getText())) {//多文件处理
                Utils.protoInfo.setJava_multiple_files(true);
            }
        } else if ("java_outer_classname".equals(ctx.optionName().getText())) {//java文件名
//            System.out.println(ctx.OPTION().getText() + " " + ctx.optionName().getText() + " = " + ctx.constant().getText());
            Utils.protoInfo.setJava_outer_classname(ctx.constant().getText().replace("\"", ""));
        } else {
//            System.out.println(ctx.OPTION().getText() + " " + ctx.optionName().getText() + " = " + ctx.constant().getText());
        }
    }

    @Override
    public void enterMessageDef(Protobuf3Parser.MessageDefContext ctx) {
        currFields = Utils.protoInfo.getObjMap().getOrDefault(ctx.messageName().getText(), new ArrayList<>());
    }

    @Override
    public void exitMessageDef(Protobuf3Parser.MessageDefContext ctx) {
        Utils.protoInfo.getObjMap().put(ctx.messageName().getText(), currFields);
    }

    //基本类型，数字类型和字符串类型字段
    @Override
    public void enterField(Protobuf3Parser.FieldContext ctx) {
        if (ctx.type_() != null && ctx.fieldName() != null && ctx.fieldNumber() != null) {
            //收集字段
            FieldInfo fieldInfo = new FieldInfo();
            //pb字段
            fieldInfo.setPbType(ctx.type_().getText());
            fieldInfo.setPbName(ctx.fieldName().getText());
            fieldInfo.setPbNumber(ctx.fieldNumber().getText());
            //bean字段
            String beanType = Utils.pb2JavaMap.getOrDefault(fieldInfo.getPbType(), fieldInfo.getPbType());
            if (ctx.fieldLabel() != null && "repeated".equals(ctx.fieldLabel().getText())) {
                fieldInfo.setPbLabel(ctx.fieldLabel().getText());
                String listType = String.format("List<%s>", beanType);
                fieldInfo.setBeanType(listType);
            } else {
                fieldInfo.setBeanType(beanType);
            }
            if (MyPluginSettings.getInstance().isFieldToCamelCase()) {
                fieldInfo.setBeanName(Utils.toCamelCase(fieldInfo.getPbName()));
            } else {
                fieldInfo.setBeanName(fieldInfo.getPbName());
            }
            currFields.add(fieldInfo);
        }
    }

    //map类型
    @Override
    public void enterMapField(Protobuf3Parser.MapFieldContext ctx) {
        if (ctx.MAP() != null && ctx.keyType() != null && ctx.type_() != null && ctx.mapName() != null && ctx.fieldNumber() != null) {
            //收集字段
            FieldInfo fieldInfo = new FieldInfo();
            //pbMap字段
            fieldInfo.setPbMapKeyType(ctx.keyType().getText());
            fieldInfo.setPbMapValType(ctx.type_().getText());
            String pbMapType = String.format("map<%s,%s>", fieldInfo.getPbMapKeyType(), fieldInfo.getPbMapValType());
            fieldInfo.setPbType(pbMapType);
            fieldInfo.setPbName(ctx.mapName().getText());
            fieldInfo.setPbNumber(ctx.fieldNumber().getText());
            //beanMap字段
            String keyType = Utils.pb2JavaMap.getOrDefault(fieldInfo.getPbMapKeyType(), fieldInfo.getPbMapKeyType());
            String valType = Utils.pb2JavaMap.getOrDefault(fieldInfo.getPbMapValType(), fieldInfo.getPbMapValType());
            String beanMapType = String.format("Map<%s,%s>", keyType, valType);
            fieldInfo.setBeanType(beanMapType);
            if (MyPluginSettings.getInstance().isFieldToCamelCase()) {
                fieldInfo.setBeanName(Utils.toCamelCase(fieldInfo.getPbName()));
            } else {
                fieldInfo.setBeanName(fieldInfo.getPbName());
            }
            currFields.add(fieldInfo);
        }
    }

    @Override
    public void enterEnumDef(Protobuf3Parser.EnumDefContext ctx) {
        currFields = Utils.protoInfo.getEnumMap().getOrDefault(ctx.enumName().getText(), new ArrayList<>());
    }


    @Override
    public void exitEnumDef(Protobuf3Parser.EnumDefContext ctx) {
        Utils.protoInfo.getEnumMap().put(ctx.enumName().getText(), currFields);
    }

    //枚举类型
    @Override
    public void enterEnumField(Protobuf3Parser.EnumFieldContext ctx) {
        if (ctx.ident() != null && ctx.intLit() != null) {
            //收集字段
            FieldInfo fieldInfo = new FieldInfo();
            //pbMap字段
            fieldInfo.setPbType("enumField");
            fieldInfo.setPbName(ctx.ident().getText());
            fieldInfo.setPbNumber(ctx.intLit().getText());
            //beanMap字段
            fieldInfo.setBeanType("enumField");
            fieldInfo.setBeanName(fieldInfo.getPbName());
            currFields.add(fieldInfo);
        }
    }

}
