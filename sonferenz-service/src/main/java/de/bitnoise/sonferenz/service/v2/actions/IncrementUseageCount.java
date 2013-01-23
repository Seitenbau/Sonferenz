package de.bitnoise.sonferenz.service.v2.actions;

import java.util.List;

/**
 * Marker to increase the Token usage count after it was used.
 */
public interface IncrementUseageCount
{
  /**
   * ( The Database ID! not the tokenID )
   * 
   * @return List of Tokens
   */
  List<Integer> getTokensToIncrementUseage();
}
