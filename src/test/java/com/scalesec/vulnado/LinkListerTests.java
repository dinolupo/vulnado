package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkListerTests {

    // Test for getLinks method
    @Test
    public void getLinks_ValidUrl_ShouldReturnLinks() throws IOException {
        String testUrl = "https://example.com";
        List<String> links = LinkLister.getLinks(testUrl);
        assertNotNull("Links should not be null", links);
        assertTrue("Links should contain elements", links.size() > 0);
    }

    @Test(expected = IOException.class)
    public void getLinks_InvalidUrl_ShouldThrowIOException() throws IOException {
        String invalidUrl = "invalid-url";
        LinkLister.getLinks(invalidUrl);
    }

    // Test for getLinksV2 method
    @Test
    public void getLinksV2_ValidUrl_ShouldReturnLinks() throws BadRequest {
        String testUrl = "https://example.com";
        List<String> links = LinkLister.getLinksV2(testUrl);
        assertNotNull("Links should not be null", links);
        assertTrue("Links should contain elements", links.size() > 0);
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_PrivateIp_ShouldThrowBadRequest() throws BadRequest {
        String privateIpUrl = "http://192.168.1.1";
        LinkLister.getLinksV2(privateIpUrl);
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_InvalidUrl_ShouldThrowBadRequest() throws BadRequest {
        String invalidUrl = "invalid-url";
        LinkLister.getLinksV2(invalidUrl);
    }

    @Test
    public void getLinksV2_ValidUrl_ShouldLogHost() throws BadRequest {
        Logger mockLogger = mock(Logger.class);
        String testUrl = "https://example.com";
        LinkLister.getLinksV2(testUrl);
        verify(mockLogger).info("example.com");
    }
}

