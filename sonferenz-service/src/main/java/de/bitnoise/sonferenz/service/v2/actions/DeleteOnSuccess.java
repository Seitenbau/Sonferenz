package de.bitnoise.sonferenz.service.v2.actions;

import java.util.List;

/**
 * Marker to invalidate the State/Token after it was successfully
 * used.
 */
public interface DeleteOnSuccess
{
  /**
   * ( The Database ID! not the tokenID )
   * 
   * @return List of Tokens
   */
  List<Integer> getTokensToDelete();
}
