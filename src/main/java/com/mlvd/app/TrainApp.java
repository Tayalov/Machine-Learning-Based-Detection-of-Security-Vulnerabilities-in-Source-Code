package com.mlvd.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlvd.features.FeatureExtractor;
import com.mlvd.model.LogisticRegression;
import com.mlvd.model.ModelBundle;
import com.mlvd.util.CsvUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class TrainApp {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private TrainApp() {
  }

  public static void main(String[] args) throws IOException {
    Path labelsPath = Path.of("data", "labels.csv");
    Path modelOutPath = Path.of("models", "model.json");

    if (!Files.exists(labelsPath)) {
      System.err.println("labels.csv not found: " + labelsPath.toAbsolutePath());
      System.exit(1);
    }

    List<Map<String, String>> rows = CsvUtils.parseSimpleCsv(labelsPath);
    List<double[]> features = new ArrayList<>();
    List<Integer> labels = new ArrayList<>();

    for (Map<String, String> row : rows) {
      int label = Integer.parseInt(row.getOrDefault("label", "0"));
      String folder = label == 1 ? "vulnerable" : "safe";
      Path filePath = Path.of("data", "raw", folder, row.get("file_name"));

      if (!Files.exists(filePath)) {
        System.err.println("Skipping missing file: " + filePath.toAbsolutePath());
        continue;
      }

      features.add(FeatureExtractor.extractFeaturesFromFile(filePath));
      labels.add(label);
    }

    if (features.size() < 4) {
      System.err.println("Not enough samples. Add more files to data/raw and labels.csv.");
      System.exit(1);
    }

    double[][] x = features.toArray(new double[0][]);
    int[] y = labels.stream().mapToInt(Integer::intValue).toArray();
    LogisticRegression.TrainedModel trained = LogisticRegression.train(x, y, 0.01, 500);

    ModelBundle bundle = new ModelBundle();
    bundle.setModelType("logistic_regression");
    bundle.setFeatureNames(FeatureExtractor.FEATURE_NAMES);
    bundle.setTrainedAt(Instant.now().toString());
    bundle.setWeights(trained.weights());
    bundle.setBias(trained.bias());

    Files.createDirectories(modelOutPath.getParent());
    Files.writeString(modelOutPath, MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(bundle));
    System.out.println("Model saved: " + modelOutPath.toAbsolutePath());
    System.out.println("Samples used: " + features.size());
  }
}
