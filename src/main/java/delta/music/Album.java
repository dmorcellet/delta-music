package delta.music;

import delta.common.framework.objects.data.DataObject;
import delta.common.framework.objects.data.DataProxy;
import delta.common.framework.objects.data.ObjectSource;

public class Album extends DataObject<Album>
{
  public static final String CLASS_NAME="ALBUM";
  // Relations
  public static final String ALBUMS_RELATION="ALBUMS_FOR_INTERPRET";

  private String _title;
  private String _image;
  private DataProxy<Interpret> _interpret;

  @Override
  public String getClassName()
  {
    return CLASS_NAME;
  }

  public Album(Long primaryKey, ObjectSource<Album> source)
  {
    super(primaryKey,source);
  }

  public DataProxy<Interpret> getInterpretProxy()
  {
    return _interpret;
  }

  public void setInterpretProxy(DataProxy<Interpret> interpret)
  {
    _interpret=interpret;
  }

  public long getInterpretKey()
  {
    long ret=0;
    if (_interpret!=null)
    {
      ret=_interpret.getPrimaryKey();
    }
    return ret;
  }

  public Interpret getInterpret()
  {
    if(_interpret!=null)
    {
      return _interpret.getDataObject();
    }
    return null;
  }

  public String getTitle()
  {
    return _title;
  }

  public void setTitle(String title)
  {
    _title=title;
  }

  public String getImage()
  {
    return _image;
  }

  public void setImage(String image)
  {
    _image=image;
  }
}
