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

import delta.common.framework.objects.sql.ObjectSqlDriver;
import delta.common.utils.jdbc.CleanupManager;
import delta.music.Interpret;
import delta.music.MusicDataSource;
import delta.music.utils.MusicLoggers;

public class InterpretSqlDriver extends ObjectSqlDriver<Interpret>
{
  private static final Logger _logger=MusicLoggers.getMusicSqlLogger();

  private PreparedStatement _psGetByPrimaryKey;
  private PreparedStatement _psGetAll;
  private PreparedStatement _psInsert;
  private PreparedStatement _psGetByName;
  private MusicDataSource _mainDataSource;

  InterpretSqlDriver(MusicDataSource mainDataSource)
  {
    _mainDataSource=mainDataSource;
  }

  @Override
  public void buildPreparedStatements(Connection newConnection)
  {
    try
    {
      String fields="cle_interprete,nom";
      String sql="SELECT "+fields+" FROM interprete WHERE cle_interprete = ?";
      _psGetByPrimaryKey=newConnection.prepareStatement(sql);
      sql="SELECT "+fields+" FROM interprete ORDER BY nom";
      _psGetAll=newConnection.prepareStatement(sql);
      sql="INSERT INTO interprete ("+fields+") VALUES (?,?)";
      _psInsert=newConnection.prepareStatement(sql,
          Statement.RETURN_GENERATED_KEYS);
      sql="SELECT DISTINCT cle_interprete FROM interprete WHERE nom like ?";
      _psGetByName=newConnection.prepareStatement(sql);
    }
    catch (SQLException sqlException)
    {
      _logger.error("Exception while building prepared statements.",sqlException);
    }
  }

  private void fillInterpret(Interpret interpret, ResultSet rs)
      throws SQLException
  {
    int n=2;
    interpret.setName(rs.getString(n));
  }

  @Override
  public List<Interpret> getAll()
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      ArrayList<Interpret> ret=new ArrayList<Interpret>();
      Interpret interpret=null;
      ResultSet rs=null;
      try
      {
        rs=_psGetAll.executeQuery();
        while (rs.next())
        {
          interpret=new Interpret(rs.getLong(1),_mainDataSource
              .getInterpretDataSource());
          fillInterpret(interpret,rs);
          ret.add(interpret);
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
  public Interpret getByPrimaryKey(long primaryKey)
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      Interpret ret=null;
      ResultSet rs=null;
      try
      {
        _psGetByPrimaryKey.setLong(1,primaryKey);
        rs=_psGetByPrimaryKey.executeQuery();
        if (rs.next())
        {
          ret=new Interpret(primaryKey,_mainDataSource.getInterpretDataSource());
          fillInterpret(ret,rs);
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
  public void create(Interpret interpret)
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      try
      {
        int n=1;
        long key=interpret.getPrimaryKey();
        if (key==0) _psInsert.setNull(n,Types.INTEGER);
        else _psInsert.setLong(n,key);
        n++;
        _psInsert.setString(n,interpret.getName());
        n++;
        _psInsert.executeUpdate();
        ResultSet rs=_psInsert.getGeneratedKeys();
        if (rs.next())
        {
          long primaryKey=rs.getLong(1);
          interpret.setPrimaryKey(primaryKey);
        }
      }
      catch (SQLException sqlException)
      {
        _logger.error("",sqlException);
        CleanupManager.cleanup(_psGetByPrimaryKey);
      }
    }
  }

  public ArrayList<Long> getByName(String name)
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      ArrayList<Long> ret=new ArrayList<Long>();
      ResultSet rs=null;
      try
      {
        _psGetByName.setString(1,name);
        rs=_psGetByName.executeQuery();
        while (rs.next())
        {
          ret.add(Long.valueOf(rs.getLong(1)));
        }
      }
      catch (SQLException sqlException)
      {
        _logger.error("",sqlException);
        CleanupManager.cleanup(_psGetByName);
      }
      finally
      {
        CleanupManager.cleanup(rs);
      }
      return ret;
    }
  }

  @Override
  public List<Long> getObjectIDsSet(String setID, Object[] parameters)
  {
    List<Long> ret=new ArrayList<Long>();
    if (setID.equals(Interpret.NAME_SET))
    {
      String name=(String)parameters[0];
      ret=getByName(name);
    }
    return ret;
  }
}
