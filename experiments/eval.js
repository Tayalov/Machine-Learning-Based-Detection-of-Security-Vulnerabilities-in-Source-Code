import fs from "fs";
import path from "path";
import { loadModel, scoreFile } from "../tool/pipeline.js";

const labelsPath = path.resolve("data/labels.csv");

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
const model = loadModel();

let tp = 0;
let tn = 0;
let fp = 0;
let fn = 0;

for (const row of rows) {
  const actual = Number(row.label);
  const folder = actual === 1 ? "vulnerable" : "safe";
  const filePath = path.resolve("data/raw", folder, row.file_name);

  if (!fs.existsSync(filePath)) continue;

  const { predicted } = scoreFile(filePath, model);

  if (predicted === 1 && actual === 1) tp += 1;
  if (predicted === 0 && actual === 0) tn += 1;
  if (predicted === 1 && actual === 0) fp += 1;
  if (predicted === 0 && actual === 1) fn += 1;
}

const precision = tp / (tp + fp || 1);
const recall = tp / (tp + fn || 1);
const f1 = (2 * precision * recall) / (precision + recall || 1);
const accuracy = (tp + tn) / (tp + tn + fp + fn || 1);

console.log(`TP=${tp} TN=${tn} FP=${fp} FN=${fn}`);
console.log(`Accuracy:  ${accuracy.toFixed(4)}`);
console.log(`Precision: ${precision.toFixed(4)}`);
console.log(`Recall:    ${recall.toFixed(4)}`);
console.log(`F1-score:  ${f1.toFixed(4)}`);
