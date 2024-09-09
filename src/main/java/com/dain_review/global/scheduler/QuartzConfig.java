// package com.dain_review.global.scheduler;
//
// import org.quartz.CronScheduleBuilder;
// import org.quartz.JobBuilder;
// import org.quartz.JobDetail;
// import org.quartz.Scheduler;
// import org.quartz.SchedulerException;
// import org.quartz.Trigger;
// import org.quartz.TriggerBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// @Configuration
// public class QuartzConfig {
//
//	@Bean
//	public JobDetail deleteUserJobDetail() {
//		return JobBuilder.newJob(DeleteUserQuartzJob.class)
//			.withIdentity("deleteUserJob")
//			.storeDurably()
//			.build();
//	}
//
//	@Bean
//	public Trigger deleteUserJobTrigger() {
//		return TriggerBuilder.newTrigger()
//			.forJob(deleteUserJobDetail())
//			.withIdentity("deleteUserTrigger")
//			.withSchedule(CronScheduleBuilder.cronSchedule("0 0 3 * * ?"))
//			.build();
//	}
//
// }
