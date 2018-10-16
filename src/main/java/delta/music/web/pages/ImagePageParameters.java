package delta.music.web.pages;

import delta.common.framework.web.PageParameters;

/**
 * Parameters for the 'image' action.
 * @author DAM
 */
public class ImagePageParameters extends PageParameters
{
  /**
   * Action key.
   */
  public static final String ACTION_VALUE="IMAGE";
  /**
   * Image name parameter name.
   */
  public static final String NAME="NAME";
  private String _name;

  /**
   * Constructor.
   * @param name Image name.
   */
  public ImagePageParameters(String name)
  {
    super("music");
    setName(name);
  }

  /**
   * Set the value of the 'image name' parameter.
   * @param name Value to set.
   */
  public final void setName(String name)
  {
    _name=name;
    _parameters.put(NAME,_name);
  }

  /**
   * Get the value of the 'image name' parameter.
   * @return an image name.
   */
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
