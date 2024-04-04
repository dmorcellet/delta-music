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

/**
 * SQL driver for songs.
 * @author DAM
 */
public class SongSqlDriver extends ObjectSqlDriver<Song>
{
  private static final Logger LOGGER=Logger.getLogger(SongSqlDriver.class);

  private PreparedStatement _psGetByPrimaryKey;
  private PreparedStatement _psGetAll;
  private PreparedStatement _psInsert;
  private PreparedStatement _psGetFromAlbum;
  private MusicDataSource _mainDataSource;

  /**
   * Constructor.
   * @param mainDataSource Parent data source.
   */
  SongSqlDriver(MusicDataSource mainDataSource)
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
      LOGGER.error("Exception while building prepared statements.",sqlException);
    }
  }

  private void fillSong(Song song, ResultSet rs) throws SQLException
  {
    int n=2;
    DataProxy<Album> albumProxy=null;
    long albumKey=rs.getLong(n);
    if (!rs.wasNull())
    {
      albumProxy=new DataProxy<Album>(Long.valueOf(albumKey),_mainDataSource.getAlbumDataSource());
    }
    song.setAlbumProxy(albumProxy);
    n++;
    song.setName(rs.getString(n));
    n++;
    song.setText(rs.getString(n));
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
          long key=rs.getLong(1);
          song=new Song(Long.valueOf(key));
          fillSong(song,rs);
          ret.add(song);
        }
      }
      catch (SQLException sqlException)
      {
        LOGGER.error("",sqlException);
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
  public Song getByPrimaryKey(Long primaryKey)
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      Song ret=null;
      ResultSet rs=null;
      try
      {
        _psGetByPrimaryKey.setLong(1,primaryKey.longValue());
        rs=_psGetByPrimaryKey.executeQuery();
        if (rs.next())
        {
          ret=new Song(primaryKey);
          fillSong(ret,rs);
        }
      }
      catch (SQLException sqlException)
      {
        LOGGER.error("",sqlException);
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
        Long key=song.getPrimaryKey();
        if (key==null) _psInsert.setNull(n,Types.INTEGER);
        else _psInsert.setLong(n,key.longValue());
        n++;
        DataProxy<Album> albumProxy=song.getAlbumProxy();
        if (albumProxy!=null) _psInsert.setLong(n,albumProxy.getPrimaryKey().longValue());
        else _psInsert.setNull(n,Types.INTEGER);
        n++;
        _psInsert.setString(n,song.getName());
        n++;
        _psInsert.setString(n,song.getText());
        _psInsert.executeUpdate();
        ResultSet rs=_psInsert.getGeneratedKeys();
        if (rs.next())
        {
          long primaryKey=rs.getLong(1);
          song.setPrimaryKey(Long.valueOf(primaryKey));
        }
      }
      catch (SQLException sqlException)
      {
        LOGGER.error("",sqlException);
        CleanupManager.cleanup(_psGetByPrimaryKey);
      }
    }
  }

  /**
   * Get the primary keys of the songs that are included in the
   * album designated by the given primary key.
   * @param albumPrimaryKey Primary of the album to use.
   * @return A possibly empty, but not <code>null</code> list of song primary keys.
   */
  public List<Long> getFromAlbum(long albumPrimaryKey)
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      ArrayList<Long> ret=new ArrayList<Long>();
      Long union=null;
      ResultSet rs=null;
      try
      {
        _psGetFromAlbum.setLong(1,albumPrimaryKey);
        rs=_psGetFromAlbum.executeQuery();
        while (rs.next())
        {
          union=Long.valueOf(rs.getLong(1));
          ret.add(union);
        }
      }
      catch (SQLException sqlException)
      {
        LOGGER.error("",sqlException);
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
  public List<Long> getRelatedObjectIDs(String relationName, Long primaryKey)
  {
    List<Long> ret=new ArrayList<Long>();
    if (relationName.equals(Song.SONGS_RELATION))
    {
      ret=getFromAlbum(primaryKey.longValue());
    }
    return ret;
  }
}
