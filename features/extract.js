import fs from "fs";

export const FEATURE_NAMES = [
  "has_sql_keywords",
  "has_exec",
  "has_subprocess",
  "has_eval",
  "has_hardcoded_secret",
  "has_path_join_user_input",
  "has_user_input",
  "string_concat_count",
  "line_count",
  "char_count"
];

export function extractFeaturesFromCode(code) {
  const lower = code.toLowerCase();

  const hasSql = /\b(select|insert|update|delete|where|from)\b/.test(lower) ? 1 : 0;
  const hasExec = /\bexec\s*\(/.test(code) ? 1 : 0;
  const hasSubprocess = /\bspawn\s*\(|\bexecFile\s*\(/.test(code) ? 1 : 0;
  const hasEval = /\beval\s*\(/.test(code) ? 1 : 0;
  const hasSecret = /(api[_-]?key|secret|token|password)\s*[:=]\s*["'`]/i.test(code) ? 1 : 0;
  const hasPathJoinInput = /path\.join\s*\(.*(req\.query|req\.body|argv|process\.env)/i.test(code) ? 1 : 0;
  const hasInput = /(req\.query|req\.body|req\.params|argv|process\.env|prompt\s*\()/i.test(code) ? 1 : 0;
  const concatCount = (code.match(/\+\s*(req\.query|req\.body|argv|process\.env|["'`])/g) || []).length;
  const lineCount = code.split(/\r?\n/).length;
  const charCount = code.length;

  return [
    hasSql,
    hasExec,
    hasSubprocess,
    hasEval,
    hasSecret,
    hasPathJoinInput,
    hasInput,
    concatCount,
    lineCount,
    charCount
  ];
}

export function extractFeaturesFromFile(filePath) {
  const code = fs.readFileSync(filePath, "utf8");
  return extractFeaturesFromCode(code);
}
