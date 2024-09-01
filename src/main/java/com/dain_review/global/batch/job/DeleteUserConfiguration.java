// package com.dain_review.global.batch.job;
//
// import com.dain_review.domain.comment.repository.CommentRepository;
// import com.dain_review.domain.post.repository.PostRepository;
// import com.dain_review.domain.user.repository.UserRepository;
// import java.time.LocalDateTime;
// import java.util.Map;
// import javax.sql.DataSource;
// import lombok.RequiredArgsConstructor;
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.Step;
// import org.springframework.batch.core.job.builder.JobBuilder;
// import org.springframework.batch.core.repository.JobRepository;
// import org.springframework.batch.core.step.builder.StepBuilder;
// import org.springframework.batch.item.ItemWriter;
// import org.springframework.batch.item.database.JdbcPagingItemReader;
// import org.springframework.batch.item.database.Order;
// import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.transaction.PlatformTransactionManager;
//
// @Configuration
// @RequiredArgsConstructor
// public class DeleteUserConfiguration {
//
//	private final JobRepository jobRepository;
//	private final PlatformTransactionManager transactionManager;
//	private final UserRepository userRepository;
//	private final CommentRepository commentRepository;
//	private final PostRepository postRepository;
//
//	@Bean
//	public Job deleteUserJob() throws Exception {
//		return new JobBuilder("deleteUserJob", jobRepository)
//			.start(deleteUserStep())
//			.build();
//	}
//
//	@Bean
//	public Step deleteUserStep() throws Exception {
//		return new StepBuilder("deleteUserStep", jobRepository)
//			.<Long, Long>chunk(100, transactionManager)
//			.reader(userItemReader(null))
//			.writer(userItemWriter())
//			.build();
//	}
//
//	@Bean
//	public JdbcPagingItemReader<Long> userItemReader(DataSource dataSource) throws Exception {
//		JdbcPagingItemReader<Long> reader = new JdbcPagingItemReader<>();
//		reader.setDataSource(dataSource);
//		reader.setPageSize(10);
//
//		// queryProvider 설정
//		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
//		queryProvider.setSelectClause("SELECT id");
//		queryProvider.setFromClause("FROM user");
//		queryProvider.setWhereClause("WHERE is_deleted = true AND deleted_at < :deletedAt");
//		queryProvider.setSortKeys(Map.of("id", Order.ASCENDING));
//
//		reader.setQueryProvider(queryProvider);
//		reader.setParameterValues(Map.of("deletedAt", LocalDateTime.now().minusYears(1)));
//
//		// RowMapper 설정
//		reader.setRowMapper((rs, rowNum) -> rs.getLong("id"));
//
//		return reader;
//	}
//
//	@Bean
//	public ItemWriter<Long> userItemWriter() {
//		return userIds -> {
//			for (Long userId : userIds) {
//				// 댓글 삭제
//				commentRepository.deleteByUserId(userId);
//				// 게시글 삭제
//				postRepository.deleteByUserId(userId);
//				// 사용자 삭제
//				userRepository.deleteById(userId);
//			}
//		};
//	}
//
// }
