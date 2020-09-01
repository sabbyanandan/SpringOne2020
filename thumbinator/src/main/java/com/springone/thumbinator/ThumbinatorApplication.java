package com.springone.thumbinator;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@EnableTask
@EnableBatchProcessing
@SpringBootApplication
public class ThumbinatorApplication {

	String ORIGINAL_IMG = "/tmp/dataflow-icon.png";

	String DRAFT_THUMBNAIL_IMG = "/tmp/draft/dataflow-icon-thumb-draft.png";

	String READY_THUMBNAIL_IMG = "/tmp/ready/dataflow-icon-thumb-ready.png";

	static List<Integer> SLEEP_TIMERS = Arrays
			.asList(11000, 20000, 15000, 7000, 8000, 10000, 10000, 12000, 9900);

	Random random = new Random();

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	public static void main(String[] args) {
		SpringApplication.run(ThumbinatorApplication.class, args);
	}

	@Bean
	public Job extractImage() {
		// job1step1: extract image
		return this.jobBuilderFactory.get("job1")
				.incrementer(new RunIdIncrementer())
				.start(this.stepBuilderFactory.get("job1step1")
						.tasklet(new Tasklet() {
							@Override
							public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
									throws Exception {

								URL url = new URL(
										"https://raw.githubusercontent.com/spring-io/dataflow.spring.io/master/src/images/dataflow-icon.png");
								File file = new File(ORIGINAL_IMG);
								ImageIO.write(ImageIO.read(url), "png", file);

								System.out.println("##### ORIGINAL IMAGE is: [" + file.length() + "] bytes #####");

								System.out.println("Job-1 + Step-1 FINISHED");
								return RepeatStatus.FINISHED;
							}
						})
						.build())
				.build();
	}

	@Bean
	public Job transformImage() {
		// job1step2: transform image to a thumbnail
		return this.jobBuilderFactory.get("job1")
				.incrementer(new RunIdIncrementer())
				.start(this.stepBuilderFactory.get("job1step2")
						.tasklet(new Tasklet() {
							@Override
							public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
									throws Exception {

								boolean createDraftDirectory = new File("/tmp/draft").mkdirs();
								File draftThumbnail = new File(DRAFT_THUMBNAIL_IMG);

								Thumbnails.of(ORIGINAL_IMG).size(50, 50)
										.toFile(draftThumbnail);

								System.out
										.println("##### THUMBNAIL DRAFT is: [" + draftThumbnail.length()
												+ "] bytes #####");

								// simulate variable wait timer, so metrics can be captured in Prometheus
								Thread.sleep(SLEEP_TIMERS.get(random.nextInt(SLEEP_TIMERS.size())));

								System.out.println("Job-1 + Step-2 FINISHED");
								return RepeatStatus.FINISHED;
							}
						})
						.build())
				.build();
	}

	@Bean
	public Job loadImage() {
		// job1step3: load the thumbnail to a different directory
		return this.jobBuilderFactory.get("job1")
				.incrementer(new RunIdIncrementer())
				.start(this.stepBuilderFactory.get("job1step3")
						.tasklet(new Tasklet() {
							@Override
							public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
									throws Exception {

								File thumbnailFrom = new File(DRAFT_THUMBNAIL_IMG);
								long thumbnailFromSize = thumbnailFrom.length();

								boolean createReadyDirectory = new File("/tmp/ready").mkdirs();
								File thumbnailTo = new File(READY_THUMBNAIL_IMG);

								thumbnailFrom.renameTo(thumbnailTo);

								// simulate variable wait timer, so metrics can be captured in Prometheus
								Thread.sleep(SLEEP_TIMERS.get(random.nextInt(SLEEP_TIMERS.size())));

								System.out.println("##### THUMBNAIL READY is: ["
										+ thumbnailTo.length() + "] bytes #####");

								System.out.println("Job-1 + Step-3 FINISHED");
								return RepeatStatus.FINISHED;
							}
						})
						.build())
				.build();
	}

	@Bean
	public Job statusImage() {
		// job2step1: query the status of original and thumbnail images; print results
		return this.jobBuilderFactory.get("job2")
				.incrementer(new RunIdIncrementer())
				.start(this.stepBuilderFactory.get("job2step1")
						.tasklet(new Tasklet() {
							@Override
							public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
									throws Exception {

								// simulate variable wait timer, so metrics can be captured in Prometheus
								Thread.sleep(SLEEP_TIMERS.get(random.nextInt(SLEEP_TIMERS.size())));

								System.out.println(
										"##### ORIGINAL IMAGE is: [" + new File(ORIGINAL_IMG).length()
												+ "] bytes / THUMBNAIL is: ["
												+ new File(READY_THUMBNAIL_IMG).length() + "] bytes #####");

								System.out.println("Job-2 + Step-1 FINISHED");
								return RepeatStatus.FINISHED;
							}
						})
						.build())
				.build();
	}
}
