package org.example.nazar.repository;

import org.example.nazar.model.Site;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SiteRepositoryTest {

    @Autowired
    private SiteRepository siteRepository;

    private Site site;

    @BeforeEach
    public void setUp() {
        site = new Site();
        site.setUrl("www.abc.com");
        site.setName("ABC");
    }

    @Test
    public void testExistsByUrl_WhenSiteExists_ReturnsTrue() {
        // given
        siteRepository.save(site);

        // when
        boolean exists = siteRepository.existsByUrl("www.abc.com");

        // then
        assertThat(exists)
                .as("Site with URL '%s' should exist", site.getUrl())
                .isTrue();
    }

    @Test
    public void testExistsByUrl_WhenSiteDoesNotExist_ReturnsFalse() {
        // when
        boolean exists = siteRepository.existsByUrl("https://nonexistent.com");

        // then
        assertThat(exists).as("Site with URL 'https://nonexistent.com' should not exist").isFalse();
    }

    @Test
    public void testFindByUrl_WhenSiteExists_ReturnsSite() {
        // given
        siteRepository.save(site);

        // when
        Site foundSite = siteRepository.findByUrl("www.abc.com");

        // then
        assertThat(foundSite).as("Site with URL '%s' should exist", site.getUrl()).isNotNull();
        assertThat(foundSite.getUrl()).as("URL of found site equals '%s'", site.getUrl())
                .isEqualTo("www.abc.com");
    }

    @Test
    public void testFindByUrl_WhenSiteDoesNotExist_ReturnsNull() {
        // when
        Site foundSite = siteRepository.findByUrl("https://nonexistent.com");

        // then
        assertThat(foundSite).as("Site with URL 'https://nonexistent.com' should not exist")
                .isNull();
    }

    @Test
    public void testExistsByUrl_WithNullUrl_ShouldReturnFalse() {
        // when
        boolean exists = siteRepository.existsByUrl(null);

        // then
        assertThat(exists).as("Site with null URL should not exist").isFalse();
    }
}
