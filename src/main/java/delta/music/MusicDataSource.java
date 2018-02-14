package delta.music;

import org.apache.log4j.Logger;

import delta.common.framework.objects.data.ObjectsManager;
import delta.music.sql.MusicSqlDriver;
import delta.music.utils.MusicLoggers;

public class MusicDataSource
{
  private static final Logger _logger=MusicLoggers.getMusicLogger();

  private ObjectsManager<Album> _albumDataSource;
  private ObjectsManager<Song> _songDataSource;
  private ObjectsManager<Interpret> _interpretDataSource;

  private static MusicDataSource _instance;

  public static MusicDataSource getInstance()
  {
    if (_instance==null)
    {
      _instance=new MusicDataSource();
    }
    return _instance;
  }

  private MusicDataSource()
  {
    buildDrivers();
  }

  private void buildDrivers()
  {
    try
    {
      MusicSqlDriver driver=MusicSqlDriver.getInstance(this);

      _albumDataSource=new ObjectsManager<Album>();
      _albumDataSource.setDriver(driver.getAlbumDriver());
      _songDataSource=new ObjectsManager<Song>();
      _songDataSource.setDriver(driver.getSongDriver());
      _interpretDataSource=new ObjectsManager<Interpret>();
      _interpretDataSource.setDriver(driver.getInterpretDriver());
    }
    catch(Exception e)
    {
      _logger.error("",e);
    }
  }

  public ObjectsManager<Album> getAlbumDataSource()
  {
    return _albumDataSource;
  }

  public ObjectsManager<Song> getSongDataSource()
  {
    return _songDataSource;
  }

  public ObjectsManager<Interpret> getInterpretDataSource()
  {
    return _interpretDataSource;
  }

  public void close()
  {
    MusicSqlDriver.getInstance(this).close();
  }
}
