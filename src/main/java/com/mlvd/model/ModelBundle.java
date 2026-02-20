package com.mlvd.model;

public class ModelBundle {
  private String modelType;
  private String[] featureNames;
  private String trainedAt;
  private double[] weights;
  private double bias;

  public String getModelType() {
    return modelType;
  }

  public void setModelType(String modelType) {
    this.modelType = modelType;
  }

  public String[] getFeatureNames() {
    return featureNames;
  }

  public void setFeatureNames(String[] featureNames) {
    this.featureNames = featureNames;
  }

  public String getTrainedAt() {
    return trainedAt;
  }

  public void setTrainedAt(String trainedAt) {
    this.trainedAt = trainedAt;
  }

  public double[] getWeights() {
    return weights;
  }

  public void setWeights(double[] weights) {
    this.weights = weights;
  }

  public double getBias() {
    return bias;
  }

  public void setBias(double bias) {
    this.bias = bias;
  }
}
