package net.tickmc.lccutils.documentation;

import net.tickmc.lccutils.LccUtils;
import net.tickmc.lccutils.components.ComponentCategory;
import net.tickmc.lccutils.components.LccComponent;
import net.tickmc.lccutils.managers.ComponentManager;
import net.tickmc.lccutils.utilities.Debug;
import net.tickmc.lccutils.utilities.MarkdownUtilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DocumentationManager {
    public static final String DOCUMENTATION_PATH = "documentation.md";
    public static final String DOCUMENTATION_HEADER = "# LccUtils - Auto-Generated Documentation";

    public static String generateDocumentation() {
        StringBuilder documentation = new StringBuilder();
        String sep = MarkdownUtilities.SEPARATOR;
        documentation.append(DOCUMENTATION_HEADER).append(sep);
        for (Map.Entry<ComponentCategory, List<LccComponent<?>>> entry : ComponentManager.getComponents().entrySet()) {
            documentation.append("## Category: ").append(entry.getKey().getReadableName()).append(sep);
            documentation.append(entry.getValue().stream().map(LccComponent::generateMarkdownEntry).reduce((s1, s2) -> s1 + sep + s2).orElse("").trim()).append(sep);
        }
        return documentation.toString();
    }

    public static void writeDocumentation() throws IOException {
        File path = LccUtils.getPlugin().getDataFolder();
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path, DOCUMENTATION_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                Debug.error(e);
            }
        }
        FileWriter writer = new FileWriter(file);
        writer.write(generateDocumentation());
        writer.flush();
        writer.close();
    }
}
