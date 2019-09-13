package com.javadeeplearningcookbook.app;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.records.reader.impl.transform.TransformProcessRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.schema.Schema;
import org.datavec.api.transform.transform.doubletransform.ConvertToDouble;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class NormalizationExample {
    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            Schema schema  =  new Schema.Builder()
                    .addColumnsString("Name", "Subject")
                    .addColumnInteger("Score")
                    .addColumnCategorical("Grade", Arrays.asList("A","B","C","D"))
                    .addColumnInteger("Passed").build();

            TransformProcess transformProcess = new TransformProcess.Builder(schema)
                    .removeColumns("Name","Subject")
                    .transform(new ConvertToDouble("Score"))
                    .categoricalToInteger("Grade").build();
            RecordReader recordReader = new CSVRecordReader(1, ',');
            recordReader.initialize(new FileSplit(new File("D:\\code\\packt\\Java-Deep-Learning-Cookbook\\02_Data_Extraction_Transform_and_Loading\\transform-data.csv")));
            RecordReader transformRecordReader = new TransformProcessRecordReader(recordReader,transformProcess);
            DataSetIterator iterator = new RecordReaderDataSetIterator(transformRecordReader,2);
            System.out.println("Before Applying Normalization");
            System.out.println(iterator.next().getFeatures());
            iterator.reset();
            DataNormalization normalization = new NormalizerStandardize();
            normalization.fit(iterator);
            iterator.setPreProcessor(normalization);
            System.out.println("After Applying Normalization");
            System.out.println(iterator.next().getFeatures());



        } catch(RuntimeException e){
            System.out.println("Please provide proper directory path to transform-data.csv in place of: Path/to/transform-data.csv");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
