package delta.music.web.pages;

import delta.common.framework.web.PageParameters;

/**
 * Parameters for the 'interpret' page.
 * @author DAM
 */
public class InterpretPageParameters extends PageParameters
{
  /**
   * Action key.
   */
  public static final String ACTION_VALUE="INTERPRET";
  /**
   * Interpret primary key parameter name.
   */
  public static final String KEY="KEY";
  private long _key;

  /**
   * Constructor.
   * @param key Interpret primary key.
   */
  public InterpretPageParameters(long key)
  {
    super("music");
    setKey(key);
  }

  /**
   * Set the interpret primary key.
   * @param key Primary key to set.
   */
  public final void setKey(long key)
  {
    _key=key;
    _parameters.put(KEY,Long.valueOf(key));
  }

  /**
   * Get the interpret primary key parameter value.
   * @return A interpret primary key.
   */
  public long getKey()
  {
    return _key;
  }

  @Override
  public String getAction()
  {
    return ACTION_VALUE;
  }
}
