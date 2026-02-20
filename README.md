# ML Vulnerability Detection (Java)

This project now has a Java implementation of the training, evaluation, and scanning pipeline.

## Requirements

- Java 17+
- Maven 3.9+

## Train model

```bash
mvn -q -Dexec.mainClass=com.mlvd.app.TrainApp exec:java
```

Model is saved to `models/model.json`.

## Evaluate model

```bash
mvn -q -Dexec.mainClass=com.mlvd.app.EvalApp exec:java
```

## Scan Java files

```bash
mvn -q -Dexec.mainClass=com.mlvd.app.ScanApp -Dexec.args="test_samples" exec:java
```

`scan` now prints line-level findings for vulnerable files, including:
- line number
- rule id
- short explanation
- code snippet

## Add more training samples

1. Add vulnerable samples to `data/raw/vulnerable/*.java`.
2. Add safe samples to `data/raw/safe/*.java`.
3. Add one row per file in `data/labels.csv`:
   - Vulnerable format: `vuln_03_xss.java,XSS,1`
   - Safe format: `safe_03_validated_input.java,None,0`
4. Retrain the model:

```bash
mvn -q -Dexec.mainClass=com.mlvd.app.TrainApp exec:java
```

## Java source layout

- `src/main/java/com/mlvd/app/TrainApp.java`
- `src/main/java/com/mlvd/app/EvalApp.java`
- `src/main/java/com/mlvd/app/ScanApp.java`
- `src/main/java/com/mlvd/features/FeatureExtractor.java`
- `src/main/java/com/mlvd/model/LogisticRegression.java`
- `src/main/java/com/mlvd/tool/Pipeline.java`
