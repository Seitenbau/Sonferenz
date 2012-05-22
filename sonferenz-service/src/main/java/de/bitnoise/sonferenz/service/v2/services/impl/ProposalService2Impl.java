package de.bitnoise.sonferenz.service.v2.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.repo.ProposalRepository;
import de.bitnoise.sonferenz.service.v2.Detach;
import de.bitnoise.sonferenz.service.v2.services.AuthenticationService;
import de.bitnoise.sonferenz.service.v2.services.ProposalService;

@Service
public class ProposalService2Impl implements ProposalService
{
  @Autowired
  ProposalRepository proposalRepo;

  @Override
  @Transactional(readOnly=true)
  public List<ProposalModel> getAllProposals()
  {
    return proposalRepo.findAll();
  }

  @Override
  @Transactional 
  public void deleteProposal(ProposalModel talk)
  {
    proposalRepo.delete(talk);
  }

  @Override
  @Transactional 
  public void saveProposal(ProposalModel talk)
  {
    proposalRepo.save(talk);
  }

  @Override
  @Transactional(readOnly=true)
  public ProposalModel getProposalById(int id)
  {
    return proposalRepo.findOne(id);
  }
  @Override
  @Transactional(readOnly=true)
  public int getCount()
  {
    return (int) proposalRepo.count();
  }

  @Override
  @Transactional(readOnly=true)
  public Page<ProposalModel> getProposals(PageRequest request)
  {
    return proposalRepo.findAll(request);
  }

  @Override
  @Transactional(readOnly=true)
  public long getVotableProposalsCount()
  {
    return proposalRepo.countAllVotable();
  }

  @Override
  @Transactional(readOnly=true)
  public Page<ProposalModel> getVotableProposals(PageRequest request)
  {
    Page<ProposalModel> result = proposalRepo.test(request);
    Detach.detachTM(result) ;
    return  result;
  }

  @Autowired
  AuthenticationService authService;
  
  @Override
  public Page<ProposalModel> getMyProposals(PageRequest request) {
	  UserModel current = authService.getCurrentUserOrFail();
	  return proposalRepo.findByOwner(current);
  }

}
