package delta.music;

import delta.common.framework.objects.data.DataObject;
import delta.common.framework.objects.data.DataProxy;

/**
 * A song.
 * <p>
 * A song has:
 * <ul>
 * <li>a name,
 * <li>some lyrics,
 * <li>may belong to an album.
 * </ul>
 * @author DAM
 */
public class Song extends DataObject<Song>
{
  /**
   * Class name.
   */
  public static final String CLASS_NAME="SONG";
  /**
   * Song for an album.
   */
  public static final String SONGS_RELATION="SONGS_FOR_ALBUM";

  private String _name;
  private String _text;
  private DataProxy<Album> _album;

  @Override
  public String getClassName()
  {
    return CLASS_NAME;
  }

  /**
   * Constructor.
   * @param primaryKey Primary key.
   */
  public Song(Long primaryKey)
  {
    super();
    setPrimaryKey(primaryKey);
  }

  /**
   * Get the name of this song.
   * @return a song name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Set the name of this song.
   * @param name Name to set.
   */
  public void setName(String name)
  {
    _name=name;
  }

  /**
   * Get the lyrics of this song.
   * @return some lyrics.
   */
  public String getText()
  {
    return _text;
  }

  /**
   * Set the lyrics of this song.
   * @param text Lyrics to set.
   */
  public void setText(String text)
  {
    _text=text;
  }

  /**
   * Get the proxy to the parent album.
   * @return An album proxy.
   */
  public DataProxy<Album> getAlbumProxy()
  {
    return _album;
  }

  /**
   * Set the proxy to the parent album.
   * @param album Proxy to set.
   */
  public void setAlbumProxy(DataProxy<Album> album)
  {
    _album=album;
  }

  /**
   * Get the parent album.
   * @return An album or <code>null</code>.
   */
  public Album getAlbum()
  {
    if(_album!=null)
    {
      return _album.getDataObject();
    }
    return null;
  }
}
