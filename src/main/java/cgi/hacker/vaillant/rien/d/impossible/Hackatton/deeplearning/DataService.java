package cgi.hacker.vaillant.rien.d.impossible.Hackatton.deeplearning;

import java.io.IOException;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cgi.hacker.vaillant.rien.d.impossible.Hackatton.Constants;

public class DataService {
	
	// EDE : Deso, j'ai pas réussi a faire fonctionner lombok dans mon IDE... :(
	private static final Logger log = LoggerFactory.getLogger(DataService.class);

	public void prepareDataSet() {
		try (RecordReader recordReader = new CSVRecordReader(0, ',')) {
			
		    try {
				recordReader.initialize(new FileSplit(new ClassPathResource("iris.txt").getFile()));
			} catch (IOException | InterruptedException e) {
				log.error("Error : ", e);
			}

			DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, 150, Constants.FEATURES_COUNT, Constants.CLASSES_COUNT);
			DataSet allData = iterator.next();
			allData.shuffle(42);
			
			DataNormalization normalizer = new NormalizerStandardize();
			normalizer.fit(allData);
			normalizer.transform(allData);
			
			SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.65);
			DataSet trainingData = testAndTrain.getTrain();
			DataSet testData = testAndTrain.getTest();
			
		} catch (IOException e) {
			log.error("Error : ", e);
		}
	}

}
