package delta.music.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.common.framework.objects.sql.DatabaseType;
import delta.music.MusicDataSource;

/**
 * SQL driver for a music database.
 * @author DAM
 */
public class MusicSqlDriver
{
  private static final Logger LOGGER=LoggerFactory.getLogger(MusicSqlDriver.class);

  private Connection _dbConnection;
  private AlbumSqlDriver _albumDriver;
  private InterpretSqlDriver _interpretDriver;
  private SongSqlDriver _songDriver;

  private static MusicSqlDriver _instance;

  /**
   * Get the sole instance of this class.
   * @param mainDataSource Data source to use.
   * @return the sole instance of this class.
   */
  public static MusicSqlDriver getInstance(MusicDataSource mainDataSource)
  {
    if (_instance==null)
    {
      _instance=new MusicSqlDriver(mainDataSource);
    }
    return _instance;
  }

  private MusicSqlDriver(MusicDataSource mainDataSource)
  {
    try
    {
      buildConnection();
      buildDrivers(mainDataSource);
    }
    catch(Exception e)
    {
      LOGGER.error("",e);
    }
  }

  private void buildConnection() throws Exception
  {
    String driver="com.mysql.jdbc.Driver";
    String url="jdbc:mysql://localhost:3306/music";
    Class.forName(driver);
    _dbConnection=DriverManager.getConnection(url,"dada","glor4fin3del");
  }

  private void buildDrivers(MusicDataSource mainDataSource)
  {
    DatabaseType dbType=DatabaseType.MYSQL;
    _albumDriver=new AlbumSqlDriver(mainDataSource);
    _albumDriver.setConnection(_dbConnection,dbType);
    _interpretDriver=new InterpretSqlDriver();
    _interpretDriver.setConnection(_dbConnection,dbType);
    _songDriver=new SongSqlDriver(mainDataSource);
    _songDriver.setConnection(_dbConnection,dbType);
  }

  /**
   * Get the SQL driver for albums.
   * @return the SQL driver for albums.
   */
  public AlbumSqlDriver getAlbumDriver()
  {
    return _albumDriver;
  }

  /**
   * Get the SQL driver for interprets.
   * @return the SQL driver for interprets.
   */
  public InterpretSqlDriver getInterpretDriver()
  {
    return _interpretDriver;
  }

  /**
   * Get the SQL driver for songs.
   * @return the SQL driver for songs.
   */
  public SongSqlDriver getSongDriver()
  {
    return _songDriver;
  }

  /**
   * Close all managed resources.
   */
  public void close()
  {
    if (_dbConnection!=null)
    {
      try
      {
        _dbConnection.close();
        _dbConnection=null;
      }
      catch(SQLException e)
      {
        LOGGER.error("",e);
      }
    }
    _songDriver=null;
    _albumDriver=null;
    _interpretDriver=null;
  }
}
