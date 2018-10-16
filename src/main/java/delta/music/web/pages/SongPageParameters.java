package delta.music.web.pages;

import delta.common.framework.web.PageParameters;

/**
 * Parameters for the 'song' page.
 * @author DAM
 */
public class SongPageParameters extends PageParameters
{
  /**
   * Action key.
   */
  public static final String ACTION_VALUE="SONG";
  /**
   * Song primary key parameter name.
   */
  public static final String KEY="KEY";
  private long _key;

  /**
   * Constructor.
   * @param key Song primary key.
   */
  public SongPageParameters(long key)
  {
    super("music");
    setKey(key);
  }

  /**
   * Set the song primary key.
   * @param key Primary key to set.
   */
  public final void setKey(long key)
  {
    _key=key;
    _parameters.put(KEY,Long.valueOf(key));
  }

  /**
   * Get the song primary key parameter value.
   * @return A song primary key.
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
