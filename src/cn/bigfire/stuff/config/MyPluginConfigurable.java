package cn.bigfire.stuff.config;

import cn.bigfire.stuff.util.Utils;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBCheckBox;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class MyPluginConfigurable implements Configurable {


    private TextFieldWithBrowseButton myDirectoryField;
    private JBCheckBox stuffCheckBox;
    private JBCheckBox lombokCheckBox;
    private JBCheckBox fieldToCamelCaseCheckBox;

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel panel = new JPanel(new GridBagLayout()); // 使用 GridBagLayout 以更好地控制布局
        GridBagConstraints constraints = new GridBagConstraints();


        // 第一行
        JLabel label = new JLabel("Select Output Directory:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        panel.add(label, constraints);
        myDirectoryField = new TextFieldWithBrowseButton();
        // ... 初始化 myDirectoryField 的代码 ...
        myDirectoryField.setText(MyPluginSettings.getInstance().getOutputDirectory() == null ? "" : MyPluginSettings.getInstance().getOutputDirectory());
        JButton button = myDirectoryField.getButton();
        JTextField textField = myDirectoryField.getTextField();
        textField.setColumns(40);
        textField.setPreferredSize(new Dimension(200, textField.getPreferredSize().height));
        button.addActionListener(e -> {
            VirtualFile selectedFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFolderDescriptor(), null, null);
            if (selectedFile != null) {
                // 确保所选的确实是一个目录
                if (selectedFile.isDirectory()) {
                    myDirectoryField.setText(selectedFile.getPath());
                } else {
                    // 可以在这里显示一个错误消息
                }
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        panel.add(myDirectoryField, constraints);



        // 第二行  添加 @Tag 选项
        stuffCheckBox = new JBCheckBox("ProtoStuff @Tag Annotation");
        stuffCheckBox.setSelected(MyPluginSettings.getInstance().isProtoStuffEnable());
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2; // 横跨两列
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        panel.add(stuffCheckBox, constraints);
        stuffCheckBox.addActionListener((e)->{
            NotificationGroup notificationGroup = new NotificationGroup("ProtoStuff Generate Config Change", NotificationDisplayType.BALLOON, true);
            Notification notification = notificationGroup.createNotification("ProtoStuff Generate Config  @Tag! Enable:" + stuffCheckBox.isSelected(), MessageType.INFO);
            Notifications.Bus.notify(notification);
        });

        // 第三行  添加 fieldToCamelCase 选项
        fieldToCamelCaseCheckBox = new JBCheckBox("FieldToCamelCase");
        fieldToCamelCaseCheckBox.setSelected(MyPluginSettings.getInstance().isLombokEnable());
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2; // 横跨两列
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        panel.add(fieldToCamelCaseCheckBox, constraints);
        fieldToCamelCaseCheckBox.addActionListener((e)->{
            NotificationGroup notificationGroup = new NotificationGroup("ProtoStuff Generate Config Change", NotificationDisplayType.BALLOON, true);
            Notification notification = notificationGroup.createNotification("ProtoStuff Generate Config  fieldToCamelCase! Enable:" + fieldToCamelCaseCheckBox.isSelected(), MessageType.INFO);
            Notifications.Bus.notify(notification);
        });

        // 第四行  添加 Lombok 选项
        lombokCheckBox = new JBCheckBox("Use Lombok Format");
        lombokCheckBox.setSelected(MyPluginSettings.getInstance().isLombokEnable());
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2; // 横跨两列
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        panel.add(lombokCheckBox, constraints);
        lombokCheckBox.addActionListener((e)->{
            NotificationGroup notificationGroup = new NotificationGroup("ProtoStuff Generate Config Change", NotificationDisplayType.BALLOON, true);
            Notification notification = notificationGroup.createNotification("ProtoStuff Generate Config  lombokEnable! Enable:" + lombokCheckBox.isSelected(), MessageType.INFO);
            Notifications.Bus.notify(notification);
        });



        // ... 其他组件和布局 ...

        return panel;
    }


    @Override
    public boolean isModified() {
        return !myDirectoryField.getText().equals(MyPluginSettings.getInstance().getOutputDirectory())
                || stuffCheckBox.isSelected() != MyPluginSettings.getInstance().isProtoStuffEnable()
                || lombokCheckBox.isSelected() != MyPluginSettings.getInstance().isLombokEnable()
                || fieldToCamelCaseCheckBox.isSelected() != MyPluginSettings.getInstance().isFieldToCamelCase()
                ;
    }

    @Override
    public void apply() {
        MyPluginSettings.getInstance().setOutputDirectory(myDirectoryField.getText());
        MyPluginSettings.getInstance().setProtoStuffEnable(stuffCheckBox.isSelected());
        MyPluginSettings.getInstance().setLombokEnable(lombokCheckBox.isSelected());
        MyPluginSettings.getInstance().setFieldToCamelCase(fieldToCamelCaseCheckBox.isSelected());
    }

    @Override
    public void reset() {
        myDirectoryField.setText(MyPluginSettings.getInstance().getOutputDirectory());
        stuffCheckBox.setSelected(MyPluginSettings.getInstance().isProtoStuffEnable());
        lombokCheckBox.setSelected(MyPluginSettings.getInstance().isLombokEnable());
        fieldToCamelCaseCheckBox.setSelected(MyPluginSettings.getInstance().isFieldToCamelCase());
    }

    @Override
    public void disposeUIResources() {
        // Nothing to dispose of
    }

    @Override
    public String getDisplayName() {
        return "ProtoStuff Generate Config";
    }


}