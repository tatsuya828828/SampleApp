package com.kuma;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	// パスワードエンコーダーのBean定義
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	// データソース
	@Autowired
	private DataSource dataSource;
	// ユーザーIDとパスワードを取得するSQL
	private static final String USER_SQL = "SELECT "+"self_id, "+"password, "+"true "+
						"FROM "+"user "+"WHERE "+"self_id=?";

	@Override
	public void configure(WebSecurity web) throws Exception {
		// 静的リソースへのアクセスには、セキュリティを適用しない
		web.ignoring().antMatchers("/webjars/**", "/static/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/webjars/**").permitAll()
		.antMatchers("/css/**").permitAll()
		.antMatchers("/").permitAll() // ルートページの直リンクを許可
		.antMatchers("/login").permitAll() // ログインページへの直リンクを許可
		.antMatchers("/signup").permitAll() // 新規登録ページへの直リンクを許可
		.anyRequest().authenticated(); // それ以外のページは直リンク不可

		// ログイン処理
		http.formLogin().loginProcessingUrl("/login") // ログイン処理のパス
						.loginPage("/login") // ログインページの指定
						.failureUrl("/login") // ログイン失敗時の遷移先
						.usernameParameter("self_id") // ユーザー名欄の値指定
						.passwordParameter("password") // パスワード欄の値指定
						.defaultSuccessUrl("/loginTime", true); // ログイン成功後の遷移先

		// ログアウト処理
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.logoutUrl("/logout") // ログアウトページの指定
					.logoutSuccessUrl("/login"); // ログアウト成功後の遷移先
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery(USER_SQL).authoritiesByUsernameQuery(USER_SQL).passwordEncoder(passwordEncoder());
	}
}
