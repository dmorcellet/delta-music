package delta.music.cddb;

import java.util.ArrayList;
import java.util.List;

/**
 * CD info.
 * @author DAM
 */
public class CDInfo
{
  private String _discID;
  private String _discTitle;
  private List<String> _trackTitles;

  /**
   * Constructor.
   */
  public CDInfo()
  {
    _discID="";
    _discTitle="";
    _trackTitles=new ArrayList<String>();
  }

  /**
   * Get the disc identifier.
   * @return A disc identifier.
   */
  public String getDiscID()
  {
    return _discID;
  }

  /**
   * Get the disc title.
   * @return a disc title.
   */
  public String getDiscTitle()
  {
    return _discTitle;
  }

  /**
   * Set the disc identifier.
   * @param discID Disc identifier to set.
   */
  public void setDiscID(String discID)
  {
    _discID=discID;
  }

  /**
   * Set the disc title.
   * @param discTitle Disc title to set.
   */
  public void setDiscTitle(String discTitle)
  {
    _discTitle=discTitle;
  }

  /**
   * Add a track title.
   * @param trackTitle Track title to add.
   */
  public void addTrackTitle(String trackTitle)
  {
    _trackTitles.add(trackTitle);
  }

  /**
   * Get the track titles.
   * @return A list of track titles.
   */
  public List<String> getTrackTitles()
  {
    return _trackTitles;
  }

  /**
   * Get the tracks count.
   * @return A tracks count.
   */
  public int getTracksCount()
  {
    return _trackTitles.size();
  }
}

