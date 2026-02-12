import fs from "fs";
import path from "path";
import { extractFeaturesFromFile } from "../features/extract.js";
import { predictProba } from "../model/logreg.js";

export function loadModel(modelPath = path.resolve("models/model.json")) {
  if (!fs.existsSync(modelPath)) {
    throw new Error(`Model not found: ${modelPath}`);
  }
  return JSON.parse(fs.readFileSync(modelPath, "utf8"));
}

export function scoreFile(filePath, model) {
  const features = extractFeaturesFromFile(filePath);
  const score = predictProba(model.weights, model.bias, features);
  return { filePath, score, predicted: score >= 0.5 ? 1 : 0 };
}
