package cn.bigfire.stuff.util;



import cn.bigfire.stuff.bo.ProtoInfo;

import java.io.*;
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

    public static String getPackageStr() {
        String packStr = "package " + Utils.protoInfo.getPackageName() + ";\n";
        if (Utils.isNotBlank(Utils.protoInfo.getPackageName())) {
            packStr = "\n";
        }
        return packStr;
    }

    /**
     * 检查字符串是否不为空白。
     * @param str 要检查的字符串
     * @return 如果字符串不为空且不仅包含空格，则返回 true，否则返回 false
     */
    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * 返回字符串中指定分隔符之前的部分。
     * @param str 要处理的字符串
     * @param delimiter 分隔符
     * @param inclusive 是否包含分隔符
     * @return 返回分隔符之前的部分
     */
    public static String subBefore(String str, String delimiter, boolean inclusive) {
        int index = str.indexOf(delimiter);
        if (index != -1) {
            if (inclusive) {
                return str.substring(0, index + delimiter.length());
            } else {
                return str.substring(0, index);
            }
        }
        return str;
    }

    /**
     * 将字符串的首字母大写。
     * @param str 要处理的字符串
     * @return 返回首字母大写的字符串
     */
    public static String upperFirst(String str) {
        if (str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 将字符串转换为驼峰命名风格。
     * @param str 要处理的字符串
     * @return 返回驼峰命名风格的字符串
     */
    public static String toCamelCase(String str) {
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        for (char c : str.toCharArray()) {
            if (c == '_' || c == '-' || c == ' ') {
                nextUpper = true;
            } else if (nextUpper) {
                result.append(Character.toUpperCase(c));
                nextUpper = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }

    /**
     * 如果字符串不以指定的后缀结尾，则在末尾追加该后缀。
     * @param str 要处理的字符串
     * @param suffix 后缀
     * @return 返回追加后缀后的字符串
     */
    public static String appendIfMissing(String str, String suffix) {
        if (!str.endsWith(suffix)) {
            return str + suffix;
        }
        return str;
    }

    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096]; // 缓冲区大小，可以根据需要调整
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        // 将ByteArrayOutputStream的内容转换为byte数组
        byte[] result = byteArrayOutputStream.toByteArray();

        // 关闭流并释放资源
        byteArrayOutputStream.close();

        // 返回结果
        return result;
    }

    /**
     * 从InputStream中读取文本内容并返回为一个String
     *
     * @param inputStream 要读取的InputStream
     * @return 读取到的文本内容
     * @throws IOException 如果读取过程中发生错误
     */
    public static String readUTF8Str(InputStream inputStream) throws IOException {
        byte[] bytes = readAllBytes(inputStream);
        return new String(bytes, "UTF-8");
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
