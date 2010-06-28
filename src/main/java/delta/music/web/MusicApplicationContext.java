package delta.music.web;

import java.io.File;

import delta.common.framework.web.WebApplicationContext;
import delta.music.misc.MusicCfg;

/**
 * @author DAM
 */
public class MusicApplicationContext extends WebApplicationContext
{
  public MusicApplicationContext()
  {
    // Nothing to do here !
  }

  public File getImagePath(String image)
  {
    File root=MusicCfg.getInstance().getAlbumRootPath();
    return new File(root,image);
  }
}
