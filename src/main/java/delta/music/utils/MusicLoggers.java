package delta.music.utils;

import org.apache.log4j.Logger;

import delta.common.utils.traces.LoggersRegistry;
import delta.common.utils.traces.LoggingConstants;


/**
 * Management class for all Music loggers.
 * @author DAM
 */
public abstract class MusicLoggers
{
  /**
   * Name of the "MUSIC" logger.
   */
  public static final String MUSIC="APPS.MUSIC";

  /**
   * Name of the SQL related Music logger.
   */
  public static final String MUSIC_SQL=MUSIC+LoggingConstants.SEPARATOR+"SQL";

  /**
   * Name of the data related Music logger.
   */
  public static final String MUSIC_DATA=MUSIC+LoggingConstants.SEPARATOR+"DATA";

  private static final Logger _musicLogger=LoggersRegistry.getLogger(MUSIC);
  private static final Logger _musicSqlLogger=LoggersRegistry.getLogger(MUSIC_SQL);
  private static final Logger _musicDataLogger=LoggersRegistry.getLogger(MUSIC_DATA);

  /**
   * Get the logger used for Music (MUSIC).
   * @return the logger used for Music.
   */
  public static Logger getMusicLogger()
  {
    return _musicLogger;
  }

  /**
   * Get the logger used for Music/SQL.
   * @return the logger used for Music/SQL.
   */
  public static Logger getMusicSqlLogger()
  {
    return _musicSqlLogger;
  }

  /**
   * Get the logger used for Music/Data.
   * @return the logger used for Music/Data.
   */
  public static Logger getMusicDataLogger()
  {
    return _musicDataLogger;
  }
}
