package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LinksControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LinkLister linkLister;

    // Test for /links endpoint
    @Test
    public void links_ShouldReturnLinks() throws Exception {
        // Mocking the behavior of LinkLister.getLinks
        Mockito.when(linkLister.getLinks("http://example.com"))
                .thenReturn(Arrays.asList("http://example.com/link1", "http://example.com/link2"));

        mockMvc.perform(get("/links")
                .param("url", "http://example.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value("http://example.com/link1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").value("http://example.com/link2"));
    }

    // Test for /links endpoint with invalid URL
    @Test
    public void links_ShouldReturnBadRequestForInvalidUrl() throws Exception {
        // Mocking the behavior of LinkLister.getLinks to throw IOException
        Mockito.when(linkLister.getLinks("invalid-url"))
                .thenThrow(new IOException("Invalid URL"));

        mockMvc.perform(get("/links")
                .param("url", "invalid-url")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(""));
    }

    // Test for /links-v2 endpoint
    @Test
    public void linksV2_ShouldReturnLinks() throws Exception {
        // Mocking the behavior of LinkLister.getLinksV2
        Mockito.when(linkLister.getLinksV2("http://example.com"))
                .thenReturn(Arrays.asList("http://example.com/link1", "http://example.com/link2"));

        mockMvc.perform(get("/links-v2")
                .param("url", "http://example.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value("http://example.com/link1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").value("http://example.com/link2"));
    }

    // Test for /links-v2 endpoint with invalid URL
    @Test
    public void linksV2_ShouldReturnBadRequestForInvalidUrl() throws Exception {
        // Mocking the behavior of LinkLister.getLinksV2 to throw BadRequest
        Mockito.when(linkLister.getLinksV2("invalid-url"))
                .thenThrow(new BadRequest("Invalid URL"));

        mockMvc.perform(get("/links-v2")
                .param("url", "invalid-url")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid URL"));
    }
}

