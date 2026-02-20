package com.mlvd.app;

import com.mlvd.model.ModelBundle;
import com.mlvd.tool.Pipeline;
import com.mlvd.util.CsvUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class EvalApp {
  private EvalApp() {
  }

  public static void main(String[] args) throws IOException {
    Path labelsPath = Path.of("data", "labels.csv");
    if (!Files.exists(labelsPath)) {
      System.err.println("labels.csv not found: " + labelsPath.toAbsolutePath());
      System.exit(1);
    }

    List<Map<String, String>> rows = CsvUtils.parseSimpleCsv(labelsPath);
    ModelBundle model = Pipeline.loadModel();

    int tp = 0;
    int tn = 0;
    int fp = 0;
    int fn = 0;

    for (Map<String, String> row : rows) {
      int actual = Integer.parseInt(row.getOrDefault("label", "0"));
      String folder = actual == 1 ? "vulnerable" : "safe";
      Path filePath = Path.of("data", "raw", folder, row.get("file_name"));

      if (!Files.exists(filePath)) {
        continue;
      }

      int predicted = Pipeline.scoreFile(filePath, model).predicted();
      if (predicted == 1 && actual == 1) {
        tp += 1;
      }
      if (predicted == 0 && actual == 0) {
        tn += 1;
      }
      if (predicted == 1 && actual == 0) {
        fp += 1;
      }
      if (predicted == 0 && actual == 1) {
        fn += 1;
      }
    }

    double precision = (double) tp / ((tp + fp) == 0 ? 1 : (tp + fp));
    double recall = (double) tp / ((tp + fn) == 0 ? 1 : (tp + fn));
    double f1 = (2 * precision * recall) / ((precision + recall) == 0 ? 1 : (precision + recall));
    double accuracy = (double) (tp + tn) / ((tp + tn + fp + fn) == 0 ? 1 : (tp + tn + fp + fn));

    System.out.println("TP=" + tp + " TN=" + tn + " FP=" + fp + " FN=" + fn);
    System.out.printf(Locale.US, "Accuracy:  %.4f%n", accuracy);
    System.out.printf(Locale.US, "Precision: %.4f%n", precision);
    System.out.printf(Locale.US, "Recall:    %.4f%n", recall);
    System.out.printf(Locale.US, "F1-score:  %.4f%n", f1);
  }
}
