package com.mlvd.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlvd.features.FeatureExtractor;
import com.mlvd.model.LogisticRegression;
import com.mlvd.model.ModelBundle;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Pipeline {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private Pipeline() {
  }

  public static ModelBundle loadModel() throws IOException {
    return loadModel(Path.of("models", "model.json"));
  }

  public static ModelBundle loadModel(Path modelPath) throws IOException {
    if (!Files.exists(modelPath)) {
      throw new IllegalStateException("Model not found: " + modelPath.toAbsolutePath());
    }
    return MAPPER.readValue(Files.readString(modelPath), ModelBundle.class);
  }

  public static ScoreResult scoreFile(Path filePath, ModelBundle model) throws IOException {
    double[] features = FeatureExtractor.extractFeaturesFromFile(filePath);
    double score = LogisticRegression.predictProba(model.getWeights(), model.getBias(), features);
    int predicted = score >= 0.5 ? 1 : 0;
    return new ScoreResult(filePath.toString(), score, predicted);
  }

  public record ScoreResult(String filePath, double score, int predicted) {
  }
}
