package launcher;

import java.io.File;

import javax.xml.bind.Unmarshaller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public Jaxb2Marshaller unmarshaller() {
		Jaxb2Marshaller unmarchaller = new Jaxb2Marshaller();
		unmarchaller.setClassesToBeBound(Report.class);
		return unmarchaller;
	}

	@Bean
	public StaxEventItemReader<Report> reader() {
		StaxEventItemReader<Report> reader = new StaxEventItemReader<Report>();
		reader.setFragmentRootElementName("record");
		reader.setResource(new ClassPathResource("report.xml"));
		reader.setUnmarshaller(unmarshaller());
		return reader;
	}

	@Bean
	public FlatFileItemWriter<Report> writer() {
		FlatFileItemWriter<Report> writer = new FlatFileItemWriter<Report>();
		writer.setResource(new FileSystemResource("C:/Users/Dev/Documents/Zahir KALI/workspace/xmlConverter/src/main/resources/report.csv"));
		writer.setShouldDeleteIfExists(true);
		BeanWrapperFieldExtractor<Report> extractor = new BeanWrapperFieldExtractor<Report>();
		String[] properties = {"refId", "name", "age", "csvDob", "income"};
		extractor.setNames(properties);
		DelimitedLineAggregator<Report> delimitedLineAgreator = new DelimitedLineAggregator<Report>();
		delimitedLineAgreator.setDelimiter(";");
		delimitedLineAgreator.setFieldExtractor(extractor);
		writer.setLineAggregator(delimitedLineAgreator);
		return writer;
	}
	
	@Bean 
	public ReportItemProcessor processor(){
		System.err.println("PROCESSOR");
		return new ReportItemProcessor();
	}
	
	@Bean 
	public Job convertToCsvJob(){
		System.out.print("Job de conversion de XML en CSV");
		return jobBuilderFactory.get("convertToCsvJob")
				.incrementer(new RunIdIncrementer())
				.flow(step1())
				.end()
				.build();
	}
	
	@Bean
	public Step step1(){
		return stepBuilderFactory.get("step1")
				.<Report, Report> chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}
}
