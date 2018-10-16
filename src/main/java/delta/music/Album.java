package delta.music;

import delta.common.framework.objects.data.DataObject;
import delta.common.framework.objects.data.DataProxy;

/**
 * Album.
 * <p>
 * An album has:
 * <ul>
 * <li>a title,
 * <li>an icon,
 * <li>a link to an interpret.
 * </ul>
 * @author DAM
 */
public class Album extends DataObject<Album>
{
  /**
   * Class name.
   */
  public static final String CLASS_NAME="ALBUM";
  // Relations
  /**
   * Albums for an interpret.
   */
  public static final String ALBUMS_RELATION="ALBUMS_FOR_INTERPRET";

  private String _title;
  private String _image;
  private DataProxy<Interpret> _interpret;

  @Override
  public String getClassName()
  {
    return CLASS_NAME;
  }

  /**
   * Constructor.
   * @param primaryKey Primary key.
   */
  public Album(Long primaryKey)
  {
    super();
    setPrimaryKey(primaryKey);
  }

  /**
   * Get the proxy to the interpret of this album.
   * @return An interpret proxy.
   */
  public DataProxy<Interpret> getInterpretProxy()
  {
    return _interpret;
  }

  /**
   * Set the proxy to the interpret of this album.
   * @param interpret Proxy to set.
   */
  public void setInterpretProxy(DataProxy<Interpret> interpret)
  {
    _interpret=interpret;
  }

  /**
   * Get the interpret of this album.
   * @return An interpret or <code>null</code>.
   */
  public Interpret getInterpret()
  {
    if(_interpret!=null)
    {
      return _interpret.getDataObject();
    }
    return null;
  }

  /**
   * Get the title of this album.
   * @return A title.
   */
  public String getTitle()
  {
    return _title;
  }

  /**
   * Set the title of this album.
   * @param title Title to set.
   */
  public void setTitle(String title)
  {
    _title=title;
  }

  /**
   * Get the image for the cover of this album.
   * @return An image name.
   */
  public String getImage()
  {
    return _image;
  }

  /**
   * Set the image name for the cover of this album.
   * @param image Image name to set.
   */
  public void setImage(String image)
  {
    _image=image;
  }
}
