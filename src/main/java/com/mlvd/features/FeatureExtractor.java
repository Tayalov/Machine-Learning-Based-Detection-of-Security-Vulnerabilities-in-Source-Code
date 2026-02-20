package com.mlvd.features;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.regex.Pattern;

public final class FeatureExtractor {
  public static final String[] FEATURE_NAMES = {
      "sql_concat_unsafe",
      "command_exec_unsafe",
      "eval_unsafe",
      "hardcoded_secret",
      "path_traversal_risk",
      "tainted_input_used",
      "safe_sql_parameterized",
      "safe_execfile_args"
  };

  private static final Pattern SQL_KEYWORDS = Pattern.compile("select|insert|update|delete");
  private static final Pattern SQL_STRING_CONCAT = Pattern.compile("\"\\s*\\+\\s*[a-zA-Z_][\\w.\\[\\]()]*");
  private static final Pattern EXEC_CALL =
      Pattern.compile("(runtime\\.getruntime\\(\\)\\.exec\\s*\\(|new\\s+processbuilder\\s*\\()", Pattern.CASE_INSENSITIVE);
  private static final Pattern EVAL_CALL =
      Pattern.compile("(scriptengine\\s*\\.\\s*eval\\s*\\(|groovyshell\\s*\\.\\s*evaluate\\s*\\()", Pattern.CASE_INSENSITIVE);
  private static final Pattern HARDCODED_SECRET =
      Pattern.compile("(api[_-]?key|secret|token|password)\\s*[:=]\\s*[\"'][^\"']+[\"']", Pattern.CASE_INSENSITIVE);
  private static final Pattern PATH_TRAVERSAL =
      Pattern.compile("(paths\\.get\\s*\\(|new\\s+file\\s*\\().*(request\\.getparameter|request\\.getheader|args\\[|system\\.getenv)",
          Pattern.CASE_INSENSITIVE);
  private static final Pattern TAINTED_INPUT =
      Pattern.compile("(request\\.getparameter|request\\.getheader|request\\.getquerystring|args\\[|system\\.getenv|scanner\\.nextline)",
          Pattern.CASE_INSENSITIVE);
  private static final Pattern SAFE_SQL_PARAM =
      Pattern.compile("preparestatement\\s*\\(\\s*\"[^\"]*\\?[^\\\"]*\"\\s*\\)", Pattern.CASE_INSENSITIVE);
  private static final Pattern SAFE_EXECFILE_ARGS =
      Pattern.compile("new\\s+processbuilder\\s*\\(\\s*\"[^\"]+\"\\s*,\\s*\"[^\"]+\"", Pattern.CASE_INSENSITIVE);

  private FeatureExtractor() {
  }

  public static double[] extractFeaturesFromCode(String code) {
    String lower = code.toLowerCase(Locale.ROOT);

    int sqlConcatUnsafe = SQL_KEYWORDS.matcher(lower).find() && SQL_STRING_CONCAT.matcher(code).find() && TAINTED_INPUT.matcher(code).find()
        ? 1 : 0;
    int commandExecUnsafe = EXEC_CALL.matcher(code).find() && TAINTED_INPUT.matcher(code).find() ? 1 : 0;
    int evalUnsafe = EVAL_CALL.matcher(code).find() ? 1 : 0;
    int hardcodedSecret = HARDCODED_SECRET.matcher(code).find() ? 1 : 0;
    int pathTraversalRisk = PATH_TRAVERSAL.matcher(code).find() ? 1 : 0;
    int taintedInputUsed = TAINTED_INPUT.matcher(code).find() ? 1 : 0;
    int safeSqlParameterized = SAFE_SQL_PARAM.matcher(code).find() ? 1 : 0;
    int safeExecfileArgs = SAFE_EXECFILE_ARGS.matcher(code).find() ? 1 : 0;

    return new double[] {
        sqlConcatUnsafe,
        commandExecUnsafe,
        evalUnsafe,
        hardcodedSecret,
        pathTraversalRisk,
        taintedInputUsed,
        safeSqlParameterized,
        safeExecfileArgs
    };
  }

  public static double[] extractFeaturesFromFile(Path filePath) throws IOException {
    String code = Files.readString(filePath);
    return extractFeaturesFromCode(code);
  }
}
