package cn.bigfire.stuff.util;



import cn.bigfire.stuff.bo.ProtoInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

public class Utils {


    public static String beanName = "";
    public static String genDir = "";
    public static ProtoInfo protoInfo = new ProtoInfo();

    public static HashMap<String, String> pb2JavaMap = new HashMap<>();
    static {
        pb2JavaMap.put("double", "double");
        pb2JavaMap.put("float", "float");
        pb2JavaMap.put("bool", "boolean");
        pb2JavaMap.put("string", "String");
        pb2JavaMap.put("bytes", "byte[]");
        pb2JavaMap.put("int32", "int");
        pb2JavaMap.put("int64", "long");
        pb2JavaMap.put("uint32", "int");
        pb2JavaMap.put("uint64", "long");
        pb2JavaMap.put("sint32", "int");
        pb2JavaMap.put("sint64", "long");
        pb2JavaMap.put("fixed32", "int");
        pb2JavaMap.put("fixed64", "long");
        pb2JavaMap.put("sfixed32", "int");
        pb2JavaMap.put("sfixed64", "long");
    }

    public static void write(String filePath, String content) {
        Path path = Paths.get(filePath);
        try {
            // 如果目录不存在，则创建目录
            Files.createDirectories(path.getParent());
            // 将字符串写入文件，如果文件已存在，则覆盖
            Files.write(path, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
