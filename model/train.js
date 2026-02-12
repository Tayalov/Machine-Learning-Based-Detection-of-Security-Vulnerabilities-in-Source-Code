import fs from "fs";
import path from "path";
import { extractFeaturesFromFile, FEATURE_NAMES } from "../features/extract.js";
import { trainLogisticRegression } from "./logreg.js";

const labelsPath = path.resolve("data/labels.csv");
const modelOutPath = path.resolve("models/model.json");

function parseCsv(csvText) {
  const lines = csvText.trim().split(/\r?\n/);
  const header = lines[0].split(",").map((s) => s.trim());
  const rows = [];

  for (let i = 1; i < lines.length; i += 1) {
    if (!lines[i].trim()) continue;
    const cols = lines[i].split(",").map((s) => s.trim());
    const row = {};
    for (let j = 0; j < header.length; j += 1) row[header[j]] = cols[j] ?? "";
    rows.push(row);
  }
  return rows;
}

if (!fs.existsSync(labelsPath)) {
  console.error(`labels.csv not found: ${labelsPath}`);
  process.exit(1);
}

const rows = parseCsv(fs.readFileSync(labelsPath, "utf8"));
const X = [];
const y = [];

for (const row of rows) {
  const label = Number(row.label);
  const folder = label === 1 ? "vulnerable" : "safe";
  const filePath = path.resolve("data/raw", folder, row.file_name);

  if (!fs.existsSync(filePath)) {
    console.warn(`Skipping missing file: ${filePath}`);
    continue;
  }

  X.push(extractFeaturesFromFile(filePath));
  y.push(label);
}

if (X.length < 4) {
  console.error("Not enough samples. Add more files to data/raw and labels.csv.");
  process.exit(1);
}

const model = trainLogisticRegression(X, y, { learningRate: 0.01, epochs: 500 });

const bundle = {
  modelType: "logistic_regression",
  featureNames: FEATURE_NAMES,
  trainedAt: new Date().toISOString(),
  ...model
};

fs.mkdirSync(path.dirname(modelOutPath), { recursive: true });
fs.writeFileSync(modelOutPath, JSON.stringify(bundle, null, 2));
console.log(`Model saved: ${modelOutPath}`);
console.log(`Samples used: ${X.length}`);
