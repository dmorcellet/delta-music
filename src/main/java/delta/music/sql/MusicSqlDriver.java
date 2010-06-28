package delta.music.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import delta.common.framework.objects.sql.DatabaseType;
import delta.music.MusicDataSource;
import delta.music.utils.MusicLoggers;

public class MusicSqlDriver
{
  private static final Logger _logger=MusicLoggers.getMusicSqlLogger();

  private Connection _dbConnection;
  private AlbumSqlDriver _albumDriver;
  private InterpretSqlDriver _interpretDriver;
  private SongSqlDriver _songDriver;

  private static MusicSqlDriver _instance;

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
      _logger.error("",e);
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
    _interpretDriver=new InterpretSqlDriver(mainDataSource);
    _interpretDriver.setConnection(_dbConnection,dbType);
    _songDriver=new SongSqlDriver(mainDataSource);
    _songDriver.setConnection(_dbConnection,dbType);
  }

  public AlbumSqlDriver getAlbumDriver()
  {
    return _albumDriver;
  }

  public InterpretSqlDriver getInterpretDriver()
  {
    return _interpretDriver;
  }

  public SongSqlDriver getSongDriver()
  {
    return _songDriver;
  }

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
        _logger.error("",e);
      }
    }
    _songDriver=null;
    _albumDriver=null;
    _interpretDriver=null;
  }
}
