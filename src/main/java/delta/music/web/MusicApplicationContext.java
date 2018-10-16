package delta.music.web;

import java.io.File;

import delta.common.framework.web.WebApplicationContext;
import delta.music.misc.MusicCfg;

/**
 * Context of the 'music' web application.
 * @author DAM
 */
public class MusicApplicationContext extends WebApplicationContext
{
  /**
   * Constructor.
   */
  public MusicApplicationContext()
  {
    // Nothing to do here !
  }

  /**
   * Get the local file for an image.
   * @param image Image name.
   * @return A path.
   */
  public File getImagePath(String image)
  {
    File root=MusicCfg.getInstance().getAlbumRootPath();
    return new File(root,image);
  }
}
