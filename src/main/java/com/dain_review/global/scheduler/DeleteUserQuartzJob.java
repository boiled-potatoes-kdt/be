// package com.dain_review.global.scheduler;
//
// import lombok.SneakyThrows;
// import org.quartz.JobExecutionContext;
// import org.quartz.JobExecutionException;
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.JobParameters;
// import org.springframework.batch.core.JobParametersBuilder;
// import org.springframework.batch.core.launch.JobLauncher;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.quartz.QuartzJobBean;
// import org.springframework.stereotype.Component;
//
// @Component
// public class DeleteUserQuartzJob extends QuartzJobBean {
//
//	@Autowired
//	private Job deleteUserJob;
//
//	@Autowired
//	private JobLauncher jobLauncher;
//
//	@SneakyThrows
//	@Override
//	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//
//		JobParameters jobParameters = new JobParametersBuilder()
//			.addLong("id", System.currentTimeMillis())
//			.toJobParameters();
//		jobLauncher.run(deleteUserJob, jobParameters);
//
//	}
// }
