package com.digiup.sandbox.config;

import com.digiup.sandbox.listener.PersonWriterListener;
import com.digiup.sandbox.model.Person;
import com.digiup.sandbox.processor.PersonProcessor;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final static int BATCH_SIZE = 2;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job1")
                .flow(step())
                .end()
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(BATCH_SIZE)
                .reader(reader())
                .writer(writer())
                //.processor(processor())
                .listener(listener())
                .build();
    }

    @Bean
    public ItemProcessor<Person, Person> processor() {
        return new PersonProcessor();
    }

    @Bean
    public ItemWriteListener<Person> listener() {
        return new PersonWriterListener();
    }

    // tag::readerwriterprocessor[]
    @Bean
    @StepScope
    public FlatFileItemReader<Person> reader() {
        PropertyEditorSupport dateEditor = new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true);
        Map<Object, PropertyEditorSupport> customEditorTargets = new HashMap<>();
        customEditorTargets.put(java.util.Date.class, dateEditor);

        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
        reader.setResource(new ClassPathResource("person-data.csv"));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<Person>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "firstName", "lastName", "countryOfBirth", "dateOfBirth" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                setTargetType(Person.class);
                setCustomEditors(customEditorTargets);
            }});
        }});
        return reader;
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Person> writer() {
        FlatFileItemWriter<Person> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("./person-enriched.psv"));
        writer.setShouldDeleteIfExists(true);
        DelimitedLineAggregator aggregator = new DelimitedLineAggregator();
        aggregator.setDelimiter("|");
        aggregator.setFieldExtractor(new BeanWrapperFieldExtractor<Person>(){{
            setNames(new String[] { "firstName", "lastName", "countryOfBirth", "age" });
        }});
        writer.setLineAggregator(aggregator);
        return writer;
    }

}
