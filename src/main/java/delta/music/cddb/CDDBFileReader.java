package delta.music.cddb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.utils.NumericTools;
import delta.common.utils.files.TextFileReader;
import delta.common.utils.files.iterator.AbstractFileIteratorCallback;
import delta.common.utils.files.iterator.FileIterator;
import delta.common.utils.files.iterator.FileIteratorCallback;

/**
 * A reader for CDDB database files.
 * @author DAM
 */
public class CDDBFileReader
{
  private static final Logger LOGGER=Logger.getLogger(CDDBFileReader.class);

  private static final String TRACK_FRAME_OFFSETS="# Track frame offsets:";
  private static final String DISCID="DISCID=";
  private static final String DTITLE="DTITLE=";
  // Could use DYEAR= too
  private static final String TTITLE="TTITLE";
  private static final String PLAYORDER="PLAYORDER";

  private File _f;

  /**
   * Constructor.
   * @param f
   */
  public CDDBFileReader(File f)
  {
    _f=f;
  }

  /**
   * Read file.
   */
  public void read()
  {
    TextFileReader fp=new TextFileReader(_f);
    if (fp.start())
    {
      int nb=0;
      while(true)
      {
        String line=fp.getNextLine();
        if (line==null) break;
        if (line.startsWith(TRACK_FRAME_OFFSETS))
        {
          // Track offsets
          readTrackOffsets(fp);
          // Disk ID & title & track titles
          CDInfo cd=readTracks(fp);
          String discTitle=cd.getDiscTitle();
          if ((discTitle.indexOf("Pink Floyd")!=-1)&&(discTitle.indexOf("Ummagumma")!=-1))
          {
            LOGGER.info("ID="+cd.getDiscID());
            LOGGER.info("Disc title="+discTitle);
            LOGGER.info("NB tracks = "+cd.getTracksCount());
            LOGGER.info("Tracks titles = "+cd.getTrackTitles());
          }
          nb++;
        }
      }
      fp.terminate();
      LOGGER.info("Nb albums dans fichiers : "+nb);
    }
  }

  private void readTrackOffsets(TextFileReader fp)
  {
    List<Integer> trackOffsets=new ArrayList<Integer>();
    while (true)
    {
      String line=fp.getNextLine();
      if (line==null) break;
      if (line.length()==0) break;
      if (line.charAt(0)=='#')
      {
        String offsetStr=line.substring(1).trim();
        int offset=NumericTools.parseInt(offsetStr,-1);
        trackOffsets.add(Integer.valueOf(offset));
      }
    }
  }

  private CDInfo readTracks(TextFileReader fp)
  {
    CDInfo ret=new CDInfo();
    while (true) // NOSONAR
    {
      String line=fp.getNextLine();
      if (line==null)
      {
        break;
      }
      if (line.startsWith("#"))
      {
        continue;
      }
      if (line.startsWith(PLAYORDER))
      {
        break;
      }
      if (line.startsWith(DISCID))
      {
        String discId=line.substring(DISCID.length()).trim();
        ret.setDiscID(discId);
      }
      else if (line.startsWith(DTITLE))
      {
        String discTitle=line.substring(DTITLE.length()).trim();
        ret.setDiscTitle(discTitle);
      }
      else if (line.startsWith(TTITLE))
      {
        int indexOfEgal=line.indexOf('=');
        if (indexOfEgal!=-1)
        {
          String trackTitle=line.substring(indexOfEgal+1).trim();
          ret.addTrackTitle(trackTitle);
        }
      }
    }
    return ret;
  }

  /**
   * Main method for this class.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    File f=new File("C:\\dada\\atrier\\cddb\\freedb-win-20050605\\rock");
    FileIteratorCallback fic=new AbstractFileIteratorCallback()
    {
      @Override
      public void handleFile(File absolute, File relative)
      {
        new CDDBFileReader(absolute).read();
      }
    };
    FileIterator fi=new FileIterator(f,true,fic);
    fi.run();
  }
}
