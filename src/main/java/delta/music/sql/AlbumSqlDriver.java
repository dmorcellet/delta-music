package delta.music.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.framework.objects.data.DataProxy;
import delta.common.framework.objects.sql.ObjectSqlDriver;
import delta.common.utils.jdbc.CleanupManager;
import delta.music.Album;
import delta.music.Interpret;
import delta.music.MusicDataSource;
import delta.music.utils.MusicLoggers;

public class AlbumSqlDriver extends ObjectSqlDriver<Album>
{
  private static final Logger _logger=MusicLoggers.getMusicSqlLogger();

  private PreparedStatement _psGetByPrimaryKey;
  private PreparedStatement _psGetAll;
  private PreparedStatement _psInsert;
  private PreparedStatement _psGetFromInterpret;
  private MusicDataSource _mainDataSource;

  AlbumSqlDriver(MusicDataSource mainDataSource)
  {
    _mainDataSource=mainDataSource;
  }

  @Override
  public void buildPreparedStatements(Connection newConnection)
  {
    try
    {
      String fields="cle_album,nom,image,cle_interprete";
      String sql="SELECT "+fields+" FROM album WHERE cle_album = ?";
      _psGetByPrimaryKey=newConnection.prepareStatement(sql);
      sql="SELECT "+fields+" FROM album";
      _psGetAll=newConnection.prepareStatement(sql);
      sql="INSERT INTO album ("+fields+") VALUES (?,?,?,?)";
      _psInsert=newConnection.prepareStatement(sql,
          Statement.RETURN_GENERATED_KEYS);
      sql="SELECT cle_album FROM album WHERE cle_interprete = ? ORDER BY nom";
      _psGetFromInterpret=newConnection.prepareStatement(sql);
    }
    catch (SQLException sqlException)
    {
      _logger.error("Exception while building prepared statements.",sqlException);
    }
  }

  private void fillAlbum(Album album, ResultSet rs) throws SQLException
  {
    int n=2;
    album.setTitle(rs.getString(n));
    n++;
    album.setImage(rs.getString(n));
    n++;
    DataProxy<Interpret> interpretProxy=null;
    long interpretKey=rs.getLong(n);
    if (!rs.wasNull())
    {
      interpretProxy=new DataProxy<Interpret>(Long.valueOf(interpretKey),_mainDataSource.getInterpretDataSource());
    }
    album.setInterpretProxy(interpretProxy);
    n++;
  }

  @Override
  public List<Album> getAll()
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      ArrayList<Album> ret=new ArrayList<Album>();
      Album album=null;
      ResultSet rs=null;
      try
      {
        rs=_psGetAll.executeQuery();
        while (rs.next())
        {
          album=new Album(rs.getLong(1));
          fillAlbum(album,rs);
          ret.add(album);
        }
      }
      catch (SQLException sqlException)
      {
        _logger.error("",sqlException);
        CleanupManager.cleanup(_psGetAll);
      }
      finally
      {
        CleanupManager.cleanup(rs);
      }
      return ret;
    }
  }

  @Override
  public Album getByPrimaryKey(Long primaryKey)
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      Album ret=null;
      ResultSet rs=null;
      try
      {
        _psGetByPrimaryKey.setLong(1,primaryKey.longValue());
        rs=_psGetByPrimaryKey.executeQuery();
        if (rs.next())
        {
          ret=new Album(primaryKey);
          fillAlbum(ret,rs);
        }
      }
      catch (SQLException sqlException)
      {
        _logger.error("",sqlException);
        CleanupManager.cleanup(_psGetByPrimaryKey);
      }
      finally
      {
        CleanupManager.cleanup(rs);
      }
      return ret;
    }
  }

  @Override
  public void create(Album album)
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      try
      {
        int n=1;
        Long key=album.getPrimaryKey();
        if (key==null) _psInsert.setNull(n,Types.INTEGER);
        else _psInsert.setLong(n,key.longValue());
        n++;
        _psInsert.setString(n,album.getTitle());
        n++;
        _psInsert.setString(n,album.getImage());
        n++;
        DataProxy<Interpret> interpretProxy=album.getInterpretProxy();
        if (interpretProxy!=null)
        {
          _psInsert.setLong(n,interpretProxy.getPrimaryKey().longValue());
        }
        else
        {
          _psInsert.setNull(n,Types.INTEGER);
        }
        n++;
        _psInsert.executeUpdate();
        ResultSet rs=_psInsert.getGeneratedKeys();
        if (rs.next())
        {
          long primaryKey=rs.getLong(1);
          album.setPrimaryKey(Long.valueOf(primaryKey));
        }
      }
      catch (SQLException sqlException)
      {
        _logger.error("",sqlException);
        CleanupManager.cleanup(_psGetByPrimaryKey);
      }
    }
  }

  public List<Long> getFromInterpret(Long primaryKey)
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      ArrayList<Long> ret=new ArrayList<Long>();
      Long union=null;
      ResultSet rs=null;
      try
      {
        _psGetFromInterpret.setLong(1,primaryKey.longValue());
        rs=_psGetFromInterpret.executeQuery();
        while (rs.next())
        {
          union=Long.valueOf(rs.getLong(1));
          ret.add(union);
        }
      }
      catch (SQLException sqlException)
      {
        _logger.error("",sqlException);
        CleanupManager.cleanup(_psGetFromInterpret);
      }
      finally
      {
        CleanupManager.cleanup(rs);
      }
      return ret;
    }
  }

  @Override
  public List<Long> getRelatedObjectIDs(String relationName, Long primaryKey)
  {
    List<Long> ret=new ArrayList<Long>();
    if (relationName.equals(Album.ALBUMS_RELATION))
    {
      ret=getFromInterpret(primaryKey);
    }
    return ret;
  }
}
