package cn.bigfire.stuff.config;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "MyPluginSettings",
        storages = @Storage("proto_stuff_generate.xml")
)
public class MyPluginSettings implements PersistentStateComponent<MyPluginSettings.State> {
    private State myState = new State();

    public static class State {
        public String outputDirectory = "";
        public boolean protoStuffEnable = true;
        public boolean lombokEnable = false;
        public boolean fieldToCamelCase = true;
    }

    public static MyPluginSettings getInstance() {
        return ApplicationManager.getApplication().getComponent(MyPluginSettings.class);
    }

    @Nullable
    @Override
    public State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        myState.outputDirectory = state.outputDirectory;
    }


    public String getOutputDirectory() {
        return myState.outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        myState.outputDirectory = outputDirectory;
    }

    public boolean isProtoStuffEnable() {
        return myState.protoStuffEnable;
    }

    public void setProtoStuffEnable(boolean protoStuffEnable) {
        myState.protoStuffEnable = protoStuffEnable;
    }

    public boolean isLombokEnable() {
        return myState.lombokEnable;
    }

    public void setLombokEnable(boolean lombokEnable) {
        myState.lombokEnable = lombokEnable;
    }

    public boolean isFieldToCamelCase() {
        return myState.fieldToCamelCase;
    }

    public void setFieldToCamelCase(boolean fieldToCamelCase) {
        myState.fieldToCamelCase = fieldToCamelCase;
    }

}