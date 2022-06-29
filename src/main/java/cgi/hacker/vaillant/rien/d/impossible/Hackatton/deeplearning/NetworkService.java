package cgi.hacker.vaillant.rien.d.impossible.Hackatton.deeplearning;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import cgi.hacker.vaillant.rien.d.impossible.Hackatton.Constants;

public class NetworkService {
	
	// TODO : configure
	MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder()
	    .iterations(1000)
	    .activation(Activation.TANH)
	    .weightInit(WeightInit.XAVIER)
	    .learningRate(0.1)
	    .regularization(true).l2(0.0001)
	    .list()
	    .layer(0, new DenseLayer.Builder().nIn(Constants.FEATURES_COUNT).nOut(3).build())
	    .layer(1, new DenseLayer.Builder().nIn(3).nOut(3).build())
	    .layer(2, new OutputLayer.Builder(
	      LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
	        .activation(Activation.SOFTMAX)
	        .nIn(3).nOut(Constants.CLASSES_COUNT).build())
	    .backprop(true).pretrain(false)
	    .build();

}
