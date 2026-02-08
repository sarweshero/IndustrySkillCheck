package com.college.SkillCheck.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConstraintUpdater implements CommandLineRunner {

    // private final JdbcTemplate jdbcTemplate;

    public DatabaseConstraintUpdater(JdbcTemplate jdbcTemplate) {
        // this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        // jdbcTemplate.execute("ALTER TABLE users DROP CONSTRAINT IF EXISTS users_year_of_study_check");
        // jdbcTemplate.execute("ALTER TABLE users ADD CONSTRAINT users_year_of_study_check CHECK (year_of_study IS NULL OR (year_of_study BETWEEN 1 AND 20))");
    }
}
