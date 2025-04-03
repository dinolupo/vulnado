package com.scalesec.vulnado;

import java.util.List;
import java.io.IOException;

@RestController
@EnableAutoConfiguration
public class LinksController {
  @RequestMapping(value = "/links", produces = "application/json")
  List<String> links(@RequestParam String url) throws IOException{
    return LinkLister.getLinks(url);
  @RequestMapping(value = "/links", produces = "application/json", method = RequestMethod.GET)
  @RequestMapping(value = "/links-v2", produces = "application/json")
  List<String> linksV2(@RequestParam String url) throws BadRequest{
    return LinkLister.getLinksV2(url);
  @RequestMapping(value = "/links-v2", produces = "application/json", method = RequestMethod.GET)
}
