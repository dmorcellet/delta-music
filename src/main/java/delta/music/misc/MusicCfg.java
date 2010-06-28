package delta.music.misc;

import java.io.File;

import delta.common.utils.configuration.Configuration;
import delta.common.utils.configuration.Configurations;

/**
 * Provides acces to configuration information for the MUSIC application.
 * @author DAM
 */
public class MusicCfg
{
  /**
   * Section name used to look for configuration data.
   */
  private static final String SECTION_NAME="MUSIC";

  /**
   * Root path for albums images storage.
   */
  private static final String ALBUM_ROOT="ALBUM_ROOT";

  private File _albumRootPath;

  /**
   * Reference to the sole instance of this class.
   */
  private static MusicCfg _instance;

  /**
   * Get the sole instance of this class.
   * @return The sole instance of this class.
   */
  public static MusicCfg getInstance()
  {
    if (_instance==null)
    {
      _instance=new MusicCfg();
    }
    return _instance;
  }

  /**
   * Private constructor.
   */
  private MusicCfg()
  {
    Configuration cfg=Configurations.getConfiguration();
    _albumRootPath=null;
    String path=cfg.getStringValue(SECTION_NAME,ALBUM_ROOT,null);
    if (path!=null)
    {
      _albumRootPath=new File(path);
    }
  }

  /**
   * Get the root path for act images storage.
   * @return the root path for act images storage.
   */
  public File getAlbumRootPath()
  {
    return _albumRootPath;
  }
}
