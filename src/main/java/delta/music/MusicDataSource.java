package delta.music;

import org.apache.log4j.Logger;

import delta.common.framework.objects.data.ObjectSource;
import delta.music.sql.MusicSqlDriver;
import delta.music.utils.MusicLoggers;

public class MusicDataSource
{
  private static final Logger _logger=MusicLoggers.getMusicLogger();

  private ObjectSource<Album> _albumDataSource;
  private ObjectSource<Song> _songDataSource;
  private ObjectSource<Interpret> _interpretDataSource;

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

      _albumDataSource=new ObjectSource<Album>(driver.getAlbumDriver());
      _songDataSource=new ObjectSource<Song>(driver.getSongDriver());
      _interpretDataSource=new ObjectSource<Interpret>(driver.getInterpretDriver());
    }
    catch(Exception e)
    {
      _logger.error("",e);
    }
  }

  public ObjectSource<Album> getAlbumDataSource()
  {
    return _albumDataSource;
  }

  public ObjectSource<Song> getSongDataSource()
  {
    return _songDataSource;
  }

  public ObjectSource<Interpret> getInterpretDataSource()
  {
    return _interpretDataSource;
  }

  public void close()
  {
    MusicSqlDriver.getInstance(this).close();
  }
}
