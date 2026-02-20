package com.mlvd.app;

public final class Main {
  private Main() {
  }

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      printUsage();
      System.exit(1);
    }

    switch (args[0]) {
      case "train" -> TrainApp.main(new String[0]);
      case "eval" -> EvalApp.main(new String[0]);
      case "scan" -> {
        if (args.length < 2) {
          System.err.println("Usage: mlvd scan <path>");
          System.exit(1);
        }
        ScanApp.main(new String[] {args[1]});
      }
      default -> {
        System.err.println("Unknown command: " + args[0]);
        printUsage();
        System.exit(1);
      }
    }
  }

  private static void printUsage() {
    System.out.println("Usage: mlvd <train|eval|scan <path>>");
  }
}
