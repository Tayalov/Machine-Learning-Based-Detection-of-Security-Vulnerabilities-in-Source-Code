import fs from "fs";
import path from "path";
import { loadModel, scoreFile } from "./pipeline.js";

function walk(dir, out = []) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const full = path.join(dir, entry.name);
    if (entry.isDirectory()) walk(full, out);
    else if (entry.isFile() && full.endsWith(".js")) out.push(full);
  }
  return out;
}

const target = process.argv[2];
if (!target) {
  console.error("Usage: node tool/scan.js <target_folder>");
  process.exit(1);
}

const targetDir = path.resolve(target);
if (!fs.existsSync(targetDir)) {
  console.error(`Target folder not found: ${targetDir}`);
  process.exit(1);
}

const model = loadModel();
const files = walk(targetDir);

if (files.length === 0) {
  console.log("No .js files found.");
  process.exit(0);
}

for (const file of files) {
  const result = scoreFile(file, model);
  const label = result.predicted === 1 ? "VULNERABLE" : "SAFE";
  console.log(`${label}\t${result.score.toFixed(4)}\t${file}`);
}
