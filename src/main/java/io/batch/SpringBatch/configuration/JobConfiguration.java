package io.batch.SpringBatch.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import io.batch.SpringBatch.domain.InsurancFieldSetMapper;
import io.batch.SpringBatch.domain.InsuranceModel;

@Configuration
public class JobConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
							throws Exception {
						// TODO Auto-generated method stub
						System.out.println("Hello World!!");
						return RepeatStatus.FINISHED;
					}
					
				}).build();
	}
	
	@Bean
	public Job helloWorldJob() {
		return jobBuilderFactory.get("helloWorldJob")
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.build();
	}
	
	
	@Bean
	public FlatFileItemReader<InsuranceModel> customerItemReader(){
		FlatFileItemReader<InsuranceModel> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new ClassPathResource("/data/FL_insurance_sample.csv"));
		
		DefaultLineMapper<InsuranceModel> customerLineMapper = new DefaultLineMapper<>();
		
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] {"policyId","county","construction"});
		
		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new InsurancFieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		
		reader.setLineMapper(customerLineMapper);
		
		return reader;
		
	}
	
	
	@Bean
	public JdbcBatchItemWriter<InsuranceModel> itemWriter(){
		
		JdbcBatchItemWriter<InsuranceModel> writer = new JdbcBatchItemWriter<>();
		
		writer.setDataSource(this.dataSource);
		writer.setSql("insert into [BatchTables].[dbo].[insurance] "
				+ "values (:policyId,:county,:construction)");
		//dont add :id as it is an auto-increment feature with default set to 1
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		return writer;
		
	}
	
	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
								.<InsuranceModel,InsuranceModel>chunk(500)
								.reader(customerItemReader())
								.writer(itemWriter())
								.build();
	}
	
	@Bean
	public Job insuranceJobWriter() {
		return jobBuilderFactory.get("insuranceJobWriter")
				.incrementer(new RunIdIncrementer())
				.start(step2())
				.build();
	}
	
	
}
