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
import delta.music.MusicDataSource;
import delta.music.Song;
import delta.music.utils.MusicLoggers;

public class SongSqlDriver extends ObjectSqlDriver<Song>
{
  private static final Logger _logger=MusicLoggers.getMusicSqlLogger();

  private PreparedStatement _psGetByPrimaryKey;
  private PreparedStatement _psGetAll;
  private PreparedStatement _psInsert;
  private PreparedStatement _psGetFromAlbum;
  private MusicDataSource _mainDataSource;

  public SongSqlDriver(MusicDataSource mainDataSource)
  {
    _mainDataSource=mainDataSource;
  }

  @Override
  public void buildPreparedStatements(Connection newConnection)
  {
    try
    {
      String fields="cle_chanson,cle_album,nom,texte";
      String sql="SELECT "+fields+" FROM chanson WHERE cle_chanson = ?";
      _psGetByPrimaryKey=newConnection.prepareStatement(sql);
      sql="SELECT "+fields+" FROM chanson";
      _psGetAll=newConnection.prepareStatement(sql);
      sql="INSERT INTO chanson ("+fields+") VALUES (?,?,?,?)";
      _psInsert=newConnection.prepareStatement(sql,
          Statement.RETURN_GENERATED_KEYS);
      sql="SELECT cle_chanson FROM chanson WHERE cle_album = ?";
      _psGetFromAlbum=newConnection.prepareStatement(sql);
    }
    catch (SQLException sqlException)
    {
      _logger.error("Exception while building prepared statements.",sqlException);
    }
  }

  private void fillSong(Song song, ResultSet rs) throws SQLException
  {
    int n=2;
    DataProxy<Album> albumProxy=null;
    long albumKey=rs.getLong(n);
    if (!rs.wasNull())
    {
      albumProxy=new DataProxy<Album>(albumKey,_mainDataSource.getAlbumDataSource());
    }
    song.setAlbumProxy(albumProxy);
    n++;
    song.setName(rs.getString(n));
    n++;
    song.setText(rs.getString(n));
    n++;
  }

  @Override
  public List<Song> getAll()
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      ArrayList<Song> ret=new ArrayList<Song>();
      Song song=null;
      ResultSet rs=null;
      try
      {
        rs=_psGetAll.executeQuery();
        while (rs.next())
        {
          song=new Song(rs.getLong(1),_mainDataSource.getSongDataSource());
          fillSong(song,rs);
          ret.add(song);
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
  public Song getByPrimaryKey(long primaryKey)
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      Song ret=null;
      ResultSet rs=null;
      try
      {
        _psGetByPrimaryKey.setLong(1,primaryKey);
        rs=_psGetByPrimaryKey.executeQuery();
        if (rs.next())
        {
          ret=new Song(primaryKey,_mainDataSource.getSongDataSource());
          fillSong(ret,rs);
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
  public void create(Song song)
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      try
      {
        int n=1;
        long key=song.getPrimaryKey();
        if (key==0) _psInsert.setNull(n,Types.INTEGER);
        else _psInsert.setLong(n,key);
        n++;
        DataProxy<Album> albumProxy=song.getAlbumProxy();
        if (albumProxy!=null) _psInsert.setLong(n,albumProxy.getPrimaryKey());
        else _psInsert.setNull(n,Types.INTEGER);
        n++;
        _psInsert.setString(n,song.getName());
        n++;
        _psInsert.setString(n,song.getText());
        n++;
        _psInsert.executeUpdate();
        ResultSet rs=_psInsert.getGeneratedKeys();
        if (rs.next())
        {
          long primaryKey=rs.getLong(1);
          song.setPrimaryKey(primaryKey);
        }
      }
      catch (SQLException sqlException)
      {
        _logger.error("",sqlException);
        CleanupManager.cleanup(_psGetByPrimaryKey);
      }
    }
  }

  public List<Long> getFromAlbum(long primaryKey)
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      ArrayList<Long> ret=new ArrayList<Long>();
      Long union=null;
      ResultSet rs=null;
      try
      {
        _psGetFromAlbum.setLong(1,primaryKey);
        rs=_psGetFromAlbum.executeQuery();
        while (rs.next())
        {
          union=Long.valueOf(rs.getLong(1));
          ret.add(union);
        }
      }
      catch (SQLException sqlException)
      {
        _logger.error("",sqlException);
        CleanupManager.cleanup(_psGetFromAlbum);
      }
      finally
      {
        CleanupManager.cleanup(rs);
      }
      return ret;
    }
  }

  @Override
  public List<Long> getRelatedObjectIDs(String relationName, long primaryKey)
  {
    List<Long> ret=new ArrayList<Long>();
    if (relationName.equals(Song.SONGS_RELATION))
    {
      ret=getFromAlbum(primaryKey);
    }
    return ret;
  }
}
