package delta.music.web.pages;

import delta.common.framework.web.PageParameters;

/**
 * Parameters for the main page of the 'music' site.
 * @author DAM
 */
public class MusicMainPageParameters extends PageParameters
{
  /**
   * Action key.
   */
  public static final String ACTION_VALUE="MAIN";

  /**
   * Constructor.
   */
  public MusicMainPageParameters()
  {
    super("music");
  }

  @Override
  public String getAction()
  {
    return ACTION_VALUE;
  }
}
