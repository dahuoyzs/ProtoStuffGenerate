package cn.bigfire.stuff.config;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ProtoFileType implements FileType {
    public static final ProtoFileType INSTANCE = new ProtoFileType();


    @NotNull
    @Override
    public String getName() {
        return "PROTO";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Protocol Buffers file";
    }

    @Override
    public String getDefaultExtension() {
        return "proto";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return AllIcons.FileTypes.Java;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Nullable
    @Override
    public String getCharset(@NotNull VirtualFile virtualFile, @NotNull byte[] bytes) {
        return null;
    }

    // 可以添加更多的方法实现，比如图标、高亮等
}
