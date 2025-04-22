package com.example.HK.security.config;

import com.example.HK.security.authentication.CustomAuthenticationEntryPoint;
import com.example.HK.security.authentication.CustomAuthenticationFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


//WebSecurity関連の設定であるということを示すアノテーション
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http, CustomAuthenticationFailureHandler failureHandler) throws Exception {
        http
                // ログインページの許可設定
                .formLogin(login -> login // フォーム認証を使う
                        .loginProcessingUrl("/toLogin")//ログイン処理時のパス
                        .loginPage("/toLogin")// ログインページの設定
                        .usernameParameter("account")//認証項目のユーザー名をアカウント名に変更
                        .failureHandler(failureHandler) // カスタムハンドラーを設定
                        .defaultSuccessUrl("/") // 認証成功時のデフォルトの遷移先
                        .permitAll())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                // リクエストの許可設定
                .authorizeHttpRequests(authz -> authz
                        // index.html の参照権限
                        //ログイン無しでアクセスOK
                        .requestMatchers("/toLogin").permitAll()
                        .requestMatchers("/css/*").permitAll()
                        .anyRequest().authenticated())
                //ログアウト処理
                .logout((logout) -> logout
                        .logoutSuccessUrl("/toLogin"));
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}