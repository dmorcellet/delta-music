package delta.music.web;

import delta.common.framework.web.WebApplication;
import delta.common.framework.web.WebRequestDispatcher;
import delta.music.MusicDataSource;

/**
 * 'music' web application.
 * @author DAM
 */
public class MusicWebApplication extends WebApplication
{
  private MusicWebRequestDispatcher _dispatcher;

  /**
   * Constructor.
   */
  public MusicWebApplication()
  {
    super("music");
    setAppContext(new MusicApplicationContext());
    _dispatcher=new MusicWebRequestDispatcher();
  }

  @Override
  public void initApplication() throws Exception
  {
    MusicDataSource.getInstance();
  }

  @Override
  public void closeApplication() throws Exception
  {
    MusicDataSource.getInstance().close();
  }

  @Override
  public WebRequestDispatcher getDispatcher()
  {
    return _dispatcher;
  }
}
