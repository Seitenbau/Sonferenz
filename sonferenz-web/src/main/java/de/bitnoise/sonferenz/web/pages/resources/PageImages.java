package de.bitnoise.sonferenz.web.pages.resources;

import org.apache.wicket.request.resource.DynamicImageResource;

import de.bitnoise.sonferenz.model.FileResourceModel;

public class PageImages extends DynamicImageResource {

  private byte[] data;

  public PageImages(FileResourceModel picture) {
    data = picture.getContent();
  }

  @Override
  protected byte[] getImageData(Attributes attributes) {
	return data;
  }
  
}
