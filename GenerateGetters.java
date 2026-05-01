import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class GenerateGetters {
    public static void main(String[] args) throws Exception {
        List<String> dirs = Arrays.asList(
            "c:/Users/vmandge/Downloads/MediMoiz/MedBillBackend/src/main/java/com/medbill/backend/models",
            "c:/Users/vmandge/Downloads/MediMoiz/MedBillBackend/src/main/java/com/medbill/backend/dto"
        );

        for (String dirPath : dirs) {
            File dir = new File(dirPath);
            if (!dir.exists()) continue;
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(".java")) {
                    processFile(file);
                }
            }
        }
    }

    private static void processFile(File file) throws Exception {
        String content = new String(Files.readAllBytes(file.toPath()));
        if (!content.contains("@Data") && !content.contains("@Getter")) return;

        // Remove imports
        content = content.replaceAll("import lombok\\..*?;\r?\n", "");
        // Remove annotations
        content = content.replaceAll("@Data\r?\n", "");
        content = content.replaceAll("@NoArgsConstructor\r?\n", "");
        content = content.replaceAll("@AllArgsConstructor\r?\n", "");

        Matcher classMatcher = Pattern.compile("public\\s+(static\\s+)?class\\s+(\\w+)").matcher(content);
        if (!classMatcher.find()) return;
        String className = classMatcher.group(2);

        Matcher fieldMatcher = Pattern.compile("private\\s+([\\w<>,\\s]+?)\\s+(\\w+)(?:\\s*=\\s*[^;]+)?\\s*;").matcher(content);
        StringBuilder methods = new StringBuilder();
        
        methods.append("    public ").append(className).append("() {}\n\n");

        while (fieldMatcher.find()) {
            String type = fieldMatcher.group(1).trim();
            String name = fieldMatcher.group(2).trim();
            String cap = name.substring(0, 1).toUpperCase() + name.substring(1);

            methods.append("    public ").append(type).append(" get").append(cap).append("() {\n");
            methods.append("        return ").append(name).append(";\n    }\n\n");

            methods.append("    public void set").append(cap).append("(").append(type).append(" ").append(name).append(") {\n");
            methods.append("        this.").append(name).append(" = ").append(name).append(";\n    }\n\n");
        }

        // Deal with nested classes recursively if needed, but for our DTO it's simple enough to just do manual or string replacement.
        // Actually for nested static class PurchaseItemDto inside PurchaseRequest, we need to process it specifically.
        
        int lastBrace = content.lastIndexOf('}');
        if (lastBrace != -1) {
            content = content.substring(0, lastBrace) + "\n" + methods.toString() + "\n}\n";
            Files.write(file.toPath(), content.getBytes());
            System.out.println("Processed " + file.getName());
        }
    }
}
