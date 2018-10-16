package delta.music.web.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import delta.common.framework.web.WebPage;
import delta.common.utils.ParameterFinder;
import delta.common.utils.io.StreamTools;
import delta.music.utils.MusicLoggers;
import delta.music.web.MusicApplicationContext;

/**
 * Builder for the 'image' action.
 * @author DAM
 */
public class ImagePage extends WebPage
{
  private static final Logger _logger=MusicLoggers.getMusicLogger();

  private String _image;

  @Override
  public void parseParameters() throws Exception
  {
    _image=ParameterFinder.getStringParameter(_request,"NAME","myImage");
  }

  @Override
  public String getMIMEType()
  {
    return "image/jpeg";
  }

  @Override
  public boolean isBinary()
  {
    return true;
  }

  @Override
  public void generate(OutputStream os)
  {
    FileInputStream fis=null;
    byte[] buffer=new byte[10000];
    try
    {
      File file=getFile();
      fis=new FileInputStream(file);
      while (true)
      {
        int bytesRead=fis.read(buffer);
        if (bytesRead<=0) break;
        os.write(buffer,0,bytesRead);
      }
    }
    catch(IOException e)
    {
      _logger.error("",e);
    }
    finally
    {
      StreamTools.close(fis);
    }
  }

  private File getFile()
  {
    MusicApplicationContext context=(MusicApplicationContext)getAppContext();
    return context.getImagePath(_image);
  }
}
