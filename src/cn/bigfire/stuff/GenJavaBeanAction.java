package cn.bigfire.stuff;

import cn.bigfire.stuff.antlr.gen.Protobuf3Lexer;
import cn.bigfire.stuff.antlr.gen.Protobuf3Parser;
import cn.bigfire.stuff.config.MyPluginSettings;
import cn.bigfire.stuff.gentlp.GenJavaBean;
import cn.bigfire.stuff.gentlp.GenJavaBeanLomBok;
import cn.bigfire.stuff.gentlp.GenStuffBean;
import cn.bigfire.stuff.gentlp.GenStuffBeanLomBok;
import cn.bigfire.stuff.util.ProtoListener;
import cn.bigfire.stuff.util.Utils;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vfs.VirtualFile;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GenJavaBeanAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            Project project = e.getProject();
            VirtualFile virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
            if (project == null || virtualFile == null || !virtualFile.getName().endsWith(".proto")) {
                return;
            }
            //read proto str
            VirtualFile fileDirectory = virtualFile.getParent();
            String name = virtualFile.getName();
            String before = StrUtil.subBefore(name, ".proto", true);
            Utils.beanName = StrUtil.upperFirst(before);
            byte[] bytes = IoUtil.readBytes(virtualFile.getInputStream());
            String protoStr = new String(bytes, "UTF-8");

            //lexer parser
            CodePointCharStream input = CharStreams.fromString(protoStr);
            Protobuf3Lexer lexer = new Protobuf3Lexer(input);
            CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
            Protobuf3Parser parser = new Protobuf3Parser(commonTokenStream);
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(new ProtoListener(), parser.proto());

            //outputDir
            String packPath = StrUtil.appendIfMissing(Utils.protoInfo.getPackageName().replace(".", "/"), "/");
            String outputDir = StrUtil.appendIfMissing(fileDirectory.getPath(), "/");
            if (StrUtil.isNotBlank(MyPluginSettings.getInstance().getOutputDirectory())) {
                outputDir = StrUtil.appendIfMissing(MyPluginSettings.getInstance().getOutputDirectory(), "/");
            }

            //genDir
            Utils.genDir = outputDir + packPath;

            //genCode
            if (!MyPluginSettings.getInstance().isProtoStuffEnable() && !MyPluginSettings.getInstance().isLombokEnable()) {
                GenJavaBean.gen(Utils.protoInfo);
            } else if (MyPluginSettings.getInstance().isProtoStuffEnable() && !MyPluginSettings.getInstance().isLombokEnable()) {
                GenStuffBean.gen(Utils.protoInfo);
            } else if (!MyPluginSettings.getInstance().isProtoStuffEnable() && MyPluginSettings.getInstance().isLombokEnable()) {
                GenJavaBeanLomBok.gen(Utils.protoInfo);
            } else if (MyPluginSettings.getInstance().isProtoStuffEnable() && MyPluginSettings.getInstance().isLombokEnable()) {
                GenStuffBeanLomBok.gen(Utils.protoInfo);
            }

            //notification
            NotificationGroup notificationGroup = new NotificationGroup("ProtoStuff Generate", NotificationDisplayType.BALLOON, true);
            Notification notification = notificationGroup.createNotification("ProtoStuff Generate ok! at:" + Utils.genDir, MessageType.INFO);
            Notifications.Bus.notify(notification);

        } catch (IOException ex) {
            ex.printStackTrace();
            NotificationGroup notificationGroup = new NotificationGroup("ProtoStuff Generate", NotificationDisplayType.BALLOON, true);
            Notification notification = notificationGroup.createNotification("ProtoStuff Generate err:" + ex.getMessage(), MessageType.ERROR);
            Notifications.Bus.notify(notification);
        }

    }


    @Override
    public void update(@NotNull AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        Project project = e.getProject();
        VirtualFile file = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        boolean isVisible = project != null && file != null && file.getExtension() != null && "proto".equals(file.getExtension().toLowerCase());
        presentation.setEnabledAndVisible(isVisible);
    }

}
