package know_wave.comma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/* Todo
  * 엔드포인트 관리
  * 패키지 디렉토리 구성 변경
 */
@SpringBootApplication
public class CommaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommaBackendApplication.class, args);
	}

}
