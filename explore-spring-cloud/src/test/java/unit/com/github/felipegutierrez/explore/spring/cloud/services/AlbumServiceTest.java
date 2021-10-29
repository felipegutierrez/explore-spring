package com.github.felipegutierrez.explore.spring.cloud.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {"server.port=0"})
@ExtendWith(SpringExtension.class)
@DirtiesContext
class AlbumServiceTest {

    @Autowired
    AlbumService albumService;

    @Test
    void getAlbumListFallBack() {
        String result = albumService.getAlbumList(null);
        assertNotNull(result);
        assertTrue(result.length() > 0);
        assertEquals(
                "[\n" +
                        "  {\n" +
                        "    \"userId\": 1,\n" +
                        "    \"id\": 1,\n" +
                        "    \"title\": \"quidem molestiae enim\"\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"userId\": 1,\n" +
                        "    \"id\": 2,\n" +
                        "    \"title\": \"sunt qui excepturi placeat culpa\"\n" +
                        "  }\n" +
                        "]",
                result);
    }
}