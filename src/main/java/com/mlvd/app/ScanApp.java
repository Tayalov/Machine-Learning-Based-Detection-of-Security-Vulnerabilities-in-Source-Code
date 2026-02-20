package com.mlvd.app;

import com.mlvd.model.ModelBundle;
import com.mlvd.tool.Pipeline;
import com.mlvd.tool.Pipeline.ScoreResult;
import com.mlvd.tool.VulnerabilityExplainer;
import com.mlvd.tool.VulnerabilityExplainer.Finding;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ScanApp {
  private ScanApp() {
  }

  public static void main(String[] args) throws IOException {
    if (args.length < 1) {
      System.err.println("Usage: java ... com.mlvd.app.ScanApp <target_folder>");
      System.exit(1);
    }

    Path targetDir = Path.of(args[0]).toAbsolutePath().normalize();
    if (!Files.exists(targetDir) || !Files.isDirectory(targetDir)) {
      System.err.println("Target folder not found: " + targetDir);
      System.exit(1);
    }

    ModelBundle model = Pipeline.loadModel();
    List<Path> files;

    try (Stream<Path> stream = Files.walk(targetDir)) {
      files = stream
          .filter(Files::isRegularFile)
          .filter(path -> path.toString().endsWith(".java"))
          .collect(Collectors.toList());
    }

    if (files.isEmpty()) {
      System.out.println("No .java files found.");
      return;
    }

    for (Path file : files) {
      ScoreResult result = Pipeline.scoreFile(file, model);
      String label = result.predicted() == 1 ? "VULNERABLE" : "SAFE";
      System.out.printf(Locale.US, "%s\t%.4f\t%s%n", label, result.score(), result.filePath());

      List<Finding> findings = VulnerabilityExplainer.explain(file);
      if (result.predicted() == 1) {
        if (findings.isEmpty()) {
          System.out.println("  - No direct risky line match found; score is based on aggregate feature signals.");
        } else {
          int limit = Math.min(4, findings.size());
          for (int i = 0; i < limit; i += 1) {
            Finding finding = findings.get(i);
            System.out.printf(
                "  - line %d [%s]: %s%n    code: %s%n",
                finding.line(),
                finding.ruleId(),
                finding.message(),
                finding.snippet());
          }
        }
      }
    }
  }
}
