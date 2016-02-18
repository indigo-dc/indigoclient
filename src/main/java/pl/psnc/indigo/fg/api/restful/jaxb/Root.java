/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.psnc.indigo.fg.api.restful.jaxb;

import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author michalo
 */
public class Root {

  List<Link> links;
  List<Version> versions;

  @JsonProperty("_links")
  public List<Link> getLinks() {
    return links;
  }

  @JsonProperty("_links")
  public void setLinks(List<Link> links) {
    this.links = links;
  }

  public List<Version> getVersions() {
    return versions;
  }

  public void setVersions(List<Version> versions) {
    this.versions = versions;
  }

}
