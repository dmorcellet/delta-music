package delta.music;

import org.apache.log4j.Logger;

import delta.common.framework.objects.data.ObjectsManager;
import delta.music.sql.MusicSqlDriver;
import delta.music.utils.MusicLoggers;

/**
 * Data source for the 'music' database.
 * @author DAM
 */
public class MusicDataSource
{
  private static final Logger _logger=MusicLoggers.getMusicLogger();

  private ObjectsManager<Album> _albumDataSource;
  private ObjectsManager<Song> _songDataSource;
  private ObjectsManager<Interpret> _interpretDataSource;

  private static MusicDataSource _instance;

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
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

  /**
   * Get the data source for albums.
   * @return the data source for albums.
   */
  public ObjectsManager<Album> getAlbumDataSource()
  {
    return _albumDataSource;
  }

  /**
   * Get the data source for songs.
   * @return the data source for songs.
   */
  public ObjectsManager<Song> getSongDataSource()
  {
    return _songDataSource;
  }

  /**
   * Get the data source for interprets.
   * @return the data source for interprets.
   */
  public ObjectsManager<Interpret> getInterpretDataSource()
  {
    return _interpretDataSource;
  }

  /**
   * Close all managed resources.
   */
  public void close()
  {
    MusicSqlDriver.getInstance(this).close();
  }
}
