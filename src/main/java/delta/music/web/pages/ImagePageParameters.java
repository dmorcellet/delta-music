package delta.music.web.pages;

import delta.common.framework.web.PageParameters;

public class ImagePageParameters extends PageParameters
{
  public static final String ACTION_VALUE="IMAGE";
  public static final String NAME="NAME";
  private String _name;

  public ImagePageParameters(String name)
  {
    super("music");
    setName(name);
  }

  public final void setName(String name)
  {
    _name=name;
    _parameters.put(NAME,_name);
  }

  public String getName()
  {
    return _name;
  }
  @Override
  public String getAction()
  {
    return ACTION_VALUE;
  }
}
