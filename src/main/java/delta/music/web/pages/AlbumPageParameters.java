package delta.music.web.pages;

import delta.common.framework.web.PageParameters;

public class AlbumPageParameters extends PageParameters
{
  public static final String ACTION_VALUE="ALBUM";
  public static final String KEY="KEY";
  private long _key;

  public AlbumPageParameters(long key)
  {
    super("music");
    setKey(key);
 }

  public final void setKey(long key)
  {
    _key=key;
    _parameters.put(KEY,Long.valueOf(key));
  }

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
