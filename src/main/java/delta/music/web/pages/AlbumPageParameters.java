package delta.music.web.pages;

import delta.common.framework.web.PageParameters;

/**
 * Parameters for the 'album' page.
 * @author DAM
 */
public class AlbumPageParameters extends PageParameters
{
  /**
   * Action key.
   */
  public static final String ACTION_VALUE="ALBUM";
  /**
   * Album primary key parameter name.
   */
  public static final String KEY="KEY";
  private long _key;

  /**
   * Constructor.
   * @param key Album primary key.
   */
  public AlbumPageParameters(long key)
  {
    super("music");
    setKey(key);
 }

  /**
   * Set the album primary key.
   * @param key Primary key to set.
   */
  public final void setKey(long key)
  {
    _key=key;
    _parameters.put(KEY,Long.valueOf(key));
  }

  /**
   * Get the album primary key parameter value.
   * @return An album primary key.
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
