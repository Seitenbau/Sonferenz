package de.bitnoise.sonferenz.web.pages.suggestion;

import java.io.Serializable;

import de.bitnoise.sonferenz.model.SuggestionModel;

public class ModelWhishList implements Serializable, ReftoWhish 
{
    public String title;
    public String owner;
    public String description;
    public SuggestionModel whish;
    public Integer like;
    public Integer sumLike;
    public Integer  id;
	@Override
    public SuggestionModel getWhish() {
	    return whish;
    }

}
