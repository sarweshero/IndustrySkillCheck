package com.college.SkillCheck;

import com.college.SkillCheck.repository.CompetencyRepository;
import com.college.SkillCheck.repository.EvidenceRepository;
import com.college.SkillCheck.repository.EvidenceSkillRepository;
import com.college.SkillCheck.repository.SkillRepository;
import com.college.SkillCheck.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ImportAutoConfiguration(exclude = {
		DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class,
		DataJpaRepositoriesAutoConfiguration.class
})
class SkillCheckApplicationTests {

	@MockitoBean
	private UserRepository userRepository;

	@MockitoBean
	private SkillRepository skillRepository;

	@MockitoBean
	private EvidenceRepository evidenceRepository;

	@MockitoBean
	private EvidenceSkillRepository evidenceSkillRepository;

	@MockitoBean
	private CompetencyRepository competencyRepository;

	@Test
	void contextLoads() {
	}

}
