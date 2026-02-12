export function sigmoid(z) {
  return 1 / (1 + Math.exp(-z));
}

export function predictProba(weights, bias, x) {
  let z = bias;
  for (let i = 0; i < x.length; i += 1) z += weights[i] * x[i];
  return sigmoid(z);
}

export function trainLogisticRegression(X, y, options = {}) {
  const learningRate = options.learningRate ?? 0.01;
  const epochs = options.epochs ?? 400;

  const nSamples = X.length;
  const nFeatures = X[0].length;

  let weights = new Array(nFeatures).fill(0);
  let bias = 0;

  for (let e = 0; e < epochs; e += 1) {
    const gradW = new Array(nFeatures).fill(0);
    let gradB = 0;

    for (let i = 0; i < nSamples; i += 1) {
      const p = predictProba(weights, bias, X[i]);
      const err = p - y[i];

      for (let j = 0; j < nFeatures; j += 1) {
        gradW[j] += err * X[i][j];
      }
      gradB += err;
    }

    for (let j = 0; j < nFeatures; j += 1) {
      weights[j] -= (learningRate * gradW[j]) / nSamples;
    }
    bias -= (learningRate * gradB) / nSamples;
  }

  return { weights, bias };
}
