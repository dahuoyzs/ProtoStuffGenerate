package cn.bigfire.stuff.util;



import cn.bigfire.stuff.bo.ProtoInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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


    /**
     * 从输入流中读取UTF-8编码的字符串。
     * @param inputStream 输入流
     * @return 返回读取的字符串
     * @throws IOException 如果发生I/O错误
     */
    public static String readUTF8Str(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
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
