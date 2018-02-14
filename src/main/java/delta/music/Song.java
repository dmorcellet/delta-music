package delta.music;

import delta.common.framework.objects.data.DataObject;
import delta.common.framework.objects.data.DataProxy;

public class Song extends DataObject<Song>
{
  public static final String CLASS_NAME="SONG";
  public static final String SONGS_RELATION="SONGS_FOR_ALBUM";

  private String _name;
  private String _text;
  private DataProxy<Album> _album;

  @Override
  public String getClassName()
  {
    return CLASS_NAME;
  }

  public Song(Long primaryKey)
  {
    super();
    setPrimaryKey(primaryKey);
  }

  public String getName()
  {
    return _name;
  }

  public void setName(String name)
  {
    _name=name;
  }

  public String getText()
  {
    return _text;
  }

  public void setText(String text)
  {
    _text=text;
  }

  public DataProxy<Album> getAlbumProxy()
  {
    return _album;
  }

  public void setAlbumProxy(DataProxy<Album> album)
  {
    _album=album;
  }

  public long getAlbumKey()
  {
    long ret=0;
    if (_album!=null)
    {
      ret=_album.getPrimaryKey();
    }
    return ret;
  }

  public Album getAlbum()
  {
    if(_album!=null)
    {
      return _album.getDataObject();
    }
    return null;
  }
}
