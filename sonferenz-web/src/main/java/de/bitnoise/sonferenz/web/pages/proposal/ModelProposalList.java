package de.bitnoise.sonferenz.web.pages.proposal;

import java.io.Serializable;

import de.bitnoise.sonferenz.model.ProposalModel;

public class ModelProposalList implements Serializable, RefToProposal
{
	public String title;
	public String author;
	public String owner;
	public String description;
	public boolean editable;
	public int rating;
	public ProposalModel talk;

	@Override
	public ProposalModel getTalk() {
		return talk;
	}
}
