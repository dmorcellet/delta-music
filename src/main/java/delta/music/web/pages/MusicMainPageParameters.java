package delta.music.web.pages;

import delta.common.framework.web.PageParameters;

public class MusicMainPageParameters extends PageParameters
{
  public static final String ACTION_VALUE="MAIN";

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
