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
            String before = Utils.subBefore(name, ".proto", true);
            Utils.beanName = Utils.upperFirst(before);
            String protoStr = Utils.readUTF8Str(virtualFile.getInputStream());

            //lexer parser
            CodePointCharStream input = CharStreams.fromString(protoStr);
            Protobuf3Lexer lexer = new Protobuf3Lexer(input);
            CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
            Protobuf3Parser parser = new Protobuf3Parser(commonTokenStream);
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(new ProtoListener(), parser.proto());

            //outputDir
            String packPath = Utils.appendIfMissing(Utils.protoInfo.getPackageName().replace(".", "/"), "/");
            String outputDir = Utils.appendIfMissing(fileDirectory.getPath(), "/");
            if (Utils.isNotBlank(MyPluginSettings.getInstance().getOutputDirectory())) {
                outputDir = Utils.appendIfMissing(MyPluginSettings.getInstance().getOutputDirectory(), "/");
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
