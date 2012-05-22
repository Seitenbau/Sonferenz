package de.bitnoise.sonferenz.service.v2.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.model.ProposalModel;

public interface ProposalService
{

  List<ProposalModel> getAllProposals();

  void deleteProposal(ProposalModel talk);

  void saveProposal(ProposalModel talk);

  ProposalModel getProposalById(int id);

  int getCount();

  Page<ProposalModel> getProposals(PageRequest request);

  long getVotableProposalsCount();

  Page<ProposalModel> getVotableProposals(PageRequest request);

  Page<ProposalModel> getMyProposals(PageRequest request);

}
